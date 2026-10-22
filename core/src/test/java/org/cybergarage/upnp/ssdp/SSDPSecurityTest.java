package org.cybergarage.upnp.ssdp;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class SSDPSecurityTest {
  private SSDPPacket packet(String host) {
    byte[] bytes =
        ("NOTIFY * HTTP/1.1\r\nHOST: " + host + "\r\n\r\n").getBytes(StandardCharsets.US_ASCII);
    return new SSDPPacket(bytes, bytes.length);
  }

  @Test
  public void rejectsMalformedHostAndPreservesMulticastAddresses() throws Exception {
    for (String host :
        new String[] {
          "",
          ":1900",
          "[]:1900",
          "[:1900",
          "[::1:1900",
          "239.255.255.250:x",
          "239.255.255.250:1",
          "example.invalid:1900",
          "cafe:1900",
          "deadbeef:1900",
          "a.b.c.d:1900"
        }) assertNull(host, packet(host).getHostInetAddress());
    assertEquals(
        InetAddress.getByName("239.255.255.250"),
        packet("239.255.255.250:1900").getHostInetAddress());
    assertEquals(InetAddress.getByName("ff02::c"), packet("[ff02::c]:1900").getHostInetAddress());
  }

  private static class FakeSocket extends SSDPSearchSocket {
    final BlockingQueue<SSDPPacket> incoming = new LinkedBlockingQueue<>();
    final AtomicInteger received = new AtomicInteger();

    FakeSocket() throws Exception {
      super((Inet4Address) InetAddress.getByName("127.0.0.1"));
    }

    public boolean open(Inet4Address address) {
      return true;
    }

    public SSDPPacket receive() throws IOException {
      try {
        SSDPPacket p = incoming.take();
        received.incrementAndGet();
        if (p.getData().length == 0) throw new IOException("closed");
        return p;
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
    }

    public boolean close() {
      incoming.offer(new SSDPPacket(new byte[0], 0));
      return true;
    }
  }

  @Test(timeout = 5000)
  public void receiverDoesNotWaitForSlowSearchListenersAndQueueIsBounded() throws Exception {
    FakeSocket socket = new FakeSocket();
    CountDownLatch started = new CountDownLatch(1), release = new CountDownLatch(1);
    AtomicInteger handled = new AtomicInteger();
    socket.addSearchListener(
        p -> {
          handled.incrementAndGet();
          started.countDown();
          try {
            release.await();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        });
    byte[] bytes =
        "M-SEARCH * HTTP/1.1\r\nMAN: \"ssdp:discover\"\r\nMX: 2147483647\r\nST: ssdp:all\r\n\r\n"
            .getBytes(StandardCharsets.US_ASCII);
    SSDPPacket request = new SSDPPacket(bytes, bytes.length);
    try {
      socket.start();
      socket.incoming.offer(request);
      assertTrue(started.await(1, TimeUnit.SECONDS));
      for (int n = 0; n < 100; n++) socket.incoming.offer(request);
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
      while (socket.received.get() < 101 && System.nanoTime() < deadline) Thread.sleep(5);
      assertEquals(101, socket.received.get());
      socket.stop();
      release.countDown();
      Thread.sleep(50);
      assertEquals(1, handled.get());
    } finally {
      release.countDown();
      socket.stop();
    }
  }
}
