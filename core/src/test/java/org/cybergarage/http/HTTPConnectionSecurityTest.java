package org.cybergarage.http;

import static org.junit.Assert.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;

public class HTTPConnectionSecurityTest {
  private void request(Socket socket, String text) throws IOException {
    socket.getOutputStream().write(text.getBytes(StandardCharsets.US_ASCII));
    socket.getOutputStream().flush();
  }

  private int active(HTTPServer server) throws Exception {
    Field field = HTTPServer.class.getDeclaredField("activeConnections");
    field.setAccessible(true);
    synchronized (server) {
      return ((Set<?>) field.get(server)).size();
    }
  }

  private void awaitActive(HTTPServer server, int count) throws Exception {
    long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
    while (active(server) != count && System.nanoTime() < end) Thread.sleep(5);
    assertEquals(count, active(server));
  }

  @Test(timeout = 10000)
  public void capsConnectionsAndReleasesThemOnCloseAndRestart() throws Exception {
    HTTPServer server = new HTTPServer();
    List<Socket> clients = new ArrayList<>();
    try {
      assertTrue(server.open("127.0.0.1", 0));
      assertTrue(server.start());
      int port = server.getServerSock().getLocalPort();
      for (int n = 0; n < HTTPServer.MAX_CONNECTIONS; n++) {
        Socket client = new Socket("127.0.0.1", port);
        clients.add(client);
        request(client, "GET / HTTP/1.1\r\nX: ");
      }
      awaitActive(server, HTTPServer.MAX_CONNECTIONS);
      try (Socket excess = new Socket("127.0.0.1", port)) {
        excess.setSoTimeout(1000);
        assertEquals(-1, excess.getInputStream().read());
      }
      assertTrue(server.close());
      awaitActive(server, 0);
      assertTrue(server.open("127.0.0.1", 0));
      server.addRequestListener(req -> req.returnOK());
      assertTrue(server.start());
      HTTPRequest req = new HTTPRequest();
      req.setMethod("GET");
      req.setURI("/");
      assertTrue(req.post("127.0.0.1", server.getServerSock().getLocalPort()).isSuccessful());
    } finally {
      server.close();
      for (Socket client : clients) client.close();
    }
  }

  @Test(timeout = 5000)
  public void listenerExceptionsReleaseSocketAndCapacity() throws Exception {
    HTTPServer server = new HTTPServer();
    server.addRequestListener(
        req -> {
          throw new IllegalArgumentException("test listener failure");
        });
    try {
      assertTrue(server.open("127.0.0.1", 0));
      server.start();
      try (Socket client = new Socket("127.0.0.1", server.getServerSock().getLocalPort())) {
        request(client, "GET / HTTP/1.0\r\n\r\n");
        client.setSoTimeout(1000);
        assertEquals(-1, client.getInputStream().read());
      }
      awaitActive(server, 0);
    } finally {
      server.close();
    }
  }

  @Test(timeout = 5000)
  public void acceptedIdleClientsTimeOut() throws Exception {
    HTTPServer server = new HTTPServer();
    server.setTimeout(100);
    try {
      assertTrue(server.open("127.0.0.1", 0));
      server.start();
      try (Socket client = new Socket("127.0.0.1", server.getServerSock().getLocalPort())) {
        client.setSoTimeout(1000);
        assertEquals(-1, client.getInputStream().read());
      }
      awaitActive(server, 0);
    } finally {
      server.close();
    }
  }

  @Test(timeout = 5000)
  public void responseTimeoutAndMalformedKeepaliveFailClosed() throws Exception {
    for (boolean malformed : new boolean[] {false, true}) {
      try (ServerSocket listener = new ServerSocket(0, 10, InetAddress.getByName("127.0.0.1"))) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> peer =
            executor.submit(
                () -> {
                  try (Socket socket = listener.accept()) {
                    if (malformed) request(socket, "HTTP/1.1 200 OK\r\nContent-Length: 3\r\n\r\nx");
                    else {
                      socket.setSoTimeout(1000);
                      while (socket.getInputStream().read() != -1) {}
                    }
                  } catch (IOException e) {
                    throw new UncheckedIOException(e);
                  }
                });
        try {
          HTTPRequest req = new HTTPRequest();
          req.setMethod("GET");
          req.setURI("/");
          req.setResponseTimeout(100);
          assertFalse(req.post("127.0.0.1", listener.getLocalPort(), true).isSuccessful());
          peer.get(2, TimeUnit.SECONDS);
        } finally {
          executor.shutdownNow();
        }
      }
    }
  }

  @Test(timeout = 5000)
  public void absoluteDeadlineStopsTrickledResponseHeaders() throws Exception {
    try (ServerSocket listener = new ServerSocket(0, 10, InetAddress.getByName("127.0.0.1"))) {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Future<?> peer =
          executor.submit(
              () -> {
                try (Socket socket = listener.accept()) {
                  request(socket, "HTTP/1.1 200 OK\r\nX: ");
                  for (int n = 0; n < 200; n++) {
                    request(socket, "x");
                    Thread.sleep(10);
                  }
                } catch (IOException expected) {
                  // The transaction deadline closes the socket while the peer is still active.
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              });
      try {
        HTTPRequest request = new HTTPRequest();
        request.setMethod("GET");
        request.setURI("/");
        request.setResponseTimeout(100);
        long start = System.nanoTime();
        assertFalse(request.post("127.0.0.1", listener.getLocalPort()).isSuccessful());
        assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < 1000);
        peer.get(2, TimeUnit.SECONDS);
      } finally {
        executor.shutdownNow();
      }
    }
  }

  @Test(timeout = 5000)
  public void descriptionRedirectPreservesQueryAndUsesBoundedParser() throws Exception {
    HTTPServer server = new HTTPServer();
    java.util.concurrent.atomic.AtomicReference<String> finalPath =
        new java.util.concurrent.atomic.AtomicReference<>();
    server.addRequestListener(
        request -> {
          HTTPResponse response = new HTTPResponse();
          if (request.getURI().equals("/start")) {
            response.setStatusCode(302);
            response.setHeader(HTTP.LOCATION, "/final?version=1");
            response.setContent("");
          } else {
            finalPath.set(request.getURI());
            response.setStatusCode(200);
            response.setContent("<root><value>ok</value></root>");
          }
          request.post(response);
        });
    try {
      assertTrue(server.open("127.0.0.1", 0));
      server.start();
      URL url = new URL("http://127.0.0.1:" + server.getServerSock().getLocalPort() + "/start");
      assertEquals(
          "ok", new org.cybergarage.xml.parser.JaxpParser().parse(url).getNode("value").getValue());
      assertEquals("/final?version=1", finalPath.get());
    } finally {
      server.close();
    }
  }
}
