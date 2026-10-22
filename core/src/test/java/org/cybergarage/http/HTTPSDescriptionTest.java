package org.cybergarage.http;

import static org.junit.Assert.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.KeyStore;
import java.util.concurrent.*;
import javax.net.ssl.*;
import org.cybergarage.xml.ParserException;
import org.cybergarage.xml.parser.JaxpParser;
import org.junit.Test;

public class HTTPSDescriptionTest {
  @Test(timeout = 20000)
  public void httpsPreservesCertificateAndHostnameVerification() throws Exception {
    Path directory = Files.createTempDirectory("upnp-tls");
    Path keystore = directory.resolve("test.p12");
    SSLContext original = SSLContext.getDefault();
    try {
      // Ephemeral localhost-only certificate. This password protects test material only.
      Process process =
          new ProcessBuilder(
                  Paths.get(System.getProperty("java.home"), "bin", "keytool").toString(),
                  "-genkeypair",
                  "-alias",
                  "localhost",
                  "-keyalg",
                  "RSA",
                  "-keysize",
                  "2048",
                  "-validity",
                  "1",
                  "-dname",
                  "CN=localhost",
                  "-ext",
                  "SAN=dns:localhost",
                  "-storetype",
                  "PKCS12",
                  "-storepass",
                  "test-only",
                  "-keystore",
                  keystore.toString())
              .redirectErrorStream(true)
              .redirectOutput(directory.resolve("keytool.log").toFile())
              .start();
      assertTrue(process.waitFor(10, TimeUnit.SECONDS));
      assertEquals(0, process.exitValue());
      KeyStore store = KeyStore.getInstance("PKCS12");
      try (InputStream in = Files.newInputStream(keystore)) {
        store.load(in, "test-only".toCharArray());
      }
      KeyManagerFactory keys =
          KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keys.init(store, "test-only".toCharArray());
      TrustManagerFactory trust =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trust.init(store);
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(keys.getKeyManagers(), trust.getTrustManagers(), null);
      SSLContext.setDefault(context);
      for (String host : new String[] {"localhost", "127.0.0.1"}) {
        try (SSLServerSocket listener =
            (SSLServerSocket)
                context
                    .getServerSocketFactory()
                    .createServerSocket(0, 10, InetAddress.getByName("127.0.0.1"))) {
          listener.setSoTimeout(3000);
          ExecutorService executor = Executors.newSingleThreadExecutor();
          Future<?> peer =
              executor.submit(
                  () -> {
                    try (SSLSocket socket = (SSLSocket) listener.accept()) {
                      socket.setSoTimeout(3000);
                      socket.startHandshake();
                      InputStream in = socket.getInputStream();
                      int matched = 0;
                      while (matched < 4) {
                        int value = in.read();
                        if (value < 0) return;
                        matched = value == "\r\n\r\n".charAt(matched) ? matched + 1 : 0;
                      }
                      socket
                          .getOutputStream()
                          .write(
                              "HTTP/1.0 200 OK\r\n\r\n<root/>".getBytes(StandardCharsets.US_ASCII));
                      socket.getOutputStream().flush();
                    } catch (IOException expected) {
                      /* Mismatched hostname rejects the handshake. */
                    }
                  });
          try {
            URL url =
                new URL("https://" + host + ":" + listener.getLocalPort() + "/description.xml");
            if (host.equals("localhost"))
              assertEquals("root", new JaxpParser().parse(url).getName());
            else assertThrows(ParserException.class, () -> new JaxpParser().parse(url));
            peer.get(5, TimeUnit.SECONDS);
          } finally {
            executor.shutdownNow();
          }
        }
      }
    } finally {
      SSLContext.setDefault(original);
      try (java.util.stream.Stream<Path> files = Files.list(directory)) {
        for (Path path : (Iterable<Path>) files::iterator) Files.deleteIfExists(path);
      }
      Files.deleteIfExists(directory);
    }
  }
}
