package org.cybergarage.upnp;

import static org.junit.Assert.*;
import java.net.*;
import org.cybergarage.upnp.event.Subscriber;
import org.junit.Test;

public class CallbackSecurityTest {
  @Test
  public void permitsOnlyTheSubscriberAddress() throws Exception {
    InetAddress peer = InetAddress.getByName("192.0.2.10");
    Device.validateCallback("http://192.0.2.10/events?x=1", peer);
    for (String url :
        new String[] {
          "http://127.0.0.1:80/events",
          "http://192.0.2.11:80/",
          "http://[::1]:80/",
          "https://192.0.2.10/",
          "http://user@192.0.2.10/",
          "http://192.0.2.10/#fragment",
          "http://192.0.2.10:0/",
          "http://192.0.2.10:65536/",
          "http://192.0.2.10/events?x=1\r\nInjected: yes"
        }) assertThrows(url, Exception.class, () -> Device.validateCallback(url, peer));
  }

  @Test
  public void supportsLocalhostAndIpv6Peers() throws Exception {
    Device.validateCallback("http://localhost:80/events", InetAddress.getByName("127.0.0.1"));
    Device.validateCallback("http://[::1]:80/events", InetAddress.getByName("::1"));
  }

  @Test
  public void pinsDeliveryAndPreservesQueryAndDefaultPort() throws Exception {
    Subscriber sub = new Subscriber();
    sub.setDeliveryURL("http://localhost/events?x=1");
    sub.setDeliveryAddress(InetAddress.getByName("127.0.0.1"));
    assertEquals("127.0.0.1", sub.getDeliveryHost());
    assertEquals("/events?x=1", sub.getDeliveryPath());
    assertEquals(80, sub.getDeliveryPort());
  }

  @Test(timeout = 8000)
  public void excessiveMxIsBoundedAndInvalidMxIsRejected() throws Exception {
    Device device = TestDevice.create();
    for (String mx : new String[] {"0", "-1", "2147483647"}) {
      byte[] bytes =
          ("M-SEARCH * HTTP/1.1\r\nMX: " + mx + "\r\n\r\n")
              .getBytes(java.nio.charset.StandardCharsets.US_ASCII);
      org.cybergarage.upnp.ssdp.SSDPPacket packet =
          new org.cybergarage.upnp.ssdp.SSDPPacket(bytes, bytes.length);
      packet.setLocalAddress("127.0.0.1");
      packet.getDatagramPacket().setAddress(InetAddress.getByName("127.0.0.1"));
      packet.getDatagramPacket().setPort(32123);
      long start = System.nanoTime();
      boolean sent = device.postSearchResponse(packet, "upnp:rootdevice", "uuid:test");
      if (mx.equals("2147483647")) {
        assertTrue(sent);
        assertTrue(
            java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < 6000);
      } else assertFalse(sent);
    }
  }
}
