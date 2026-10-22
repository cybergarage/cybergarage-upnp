/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2026
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.cybergarage.http.HTTPHeader;
import org.cybergarage.upnp.ssdp.SSDPPacket;

public class DeviceSearchResponseTest extends TestCase {

  private static final String UDN = "uuid:cybergarage-test-device-1";

  private DatagramSocket deviceSock;
  private DatagramSocket cpSock;

  protected void setUp() throws Exception {
    InetAddress loopback = InetAddress.getByName("127.0.0.1");
    deviceSock = new DatagramSocket(0, loopback);
    cpSock = new DatagramSocket(0, loopback);
    cpSock.setSoTimeout(3000);
  }

  protected void tearDown() {
    deviceSock.close();
    cpSock.close();
  }

  private SSDPPacket newSearchPacket(String st) throws Exception {
    String req =
        "M-SEARCH * HTTP/1.1\r\n"
            + "HOST: 239.255.255.250:1900\r\n"
            + "MAN: \"ssdp:discover\"\r\n"
            + "MX: 1\r\n"
            + "ST: "
            + st
            + "\r\n\r\n";
    byte[] buf = req.getBytes();
    SSDPPacket packet = new SSDPPacket(buf, buf.length);
    packet.getDatagramPacket().setAddress(cpSock.getLocalAddress());
    packet.getDatagramPacket().setPort(cpSock.getLocalPort());
    packet.setLocalAddress("127.0.0.1");
    packet.setSocket(deviceSock);
    return packet;
  }

  private List<DatagramPacket> receiveResponses() throws Exception {
    List<DatagramPacket> responses = new ArrayList<DatagramPacket>();
    try {
      while (true) {
        byte[] buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        cpSock.receive(packet);
        responses.add(packet);
        cpSock.setSoTimeout(500);
      }
    } catch (SocketTimeoutException e) {
    }
    return responses;
  }

  private static String header(DatagramPacket packet, String name) {
    String data = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
    return HTTPHeader.getValue(data, name);
  }

  public void testAllDeviceSearchResponses() throws Exception {
    // ssdp:all must be answered with upnp:rootdevice, the UUID and the device type,
    // sent from the socket that received the M-SEARCH (#17).
    Device dev = TestDevice.create();
    dev.deviceSearchResponse(newSearchPacket("ssdp:all"));

    List<DatagramPacket> responses = receiveResponses();
    assertEquals(3, responses.size());
    List<String> sts = new ArrayList<String>();
    for (DatagramPacket res : responses) {
      assertEquals(deviceSock.getLocalPort(), res.getPort());
      sts.add(header(res, "ST") + " " + header(res, "USN"));
    }
    assertTrue(sts.toString(), sts.contains("upnp:rootdevice " + UDN + "::upnp:rootdevice"));
    assertTrue(sts.toString(), sts.contains(UDN + " " + UDN));
    assertTrue(
        sts.toString(),
        sts.contains(TestDevice.DEVICE_TYPE + " " + UDN + "::" + TestDevice.DEVICE_TYPE));
  }

  public void testNonAsciiFriendlyNameIsNotTruncated() throws Exception {
    Device dev = TestDevice.create();
    dev.setFriendlyName("テスト機器");
    dev.deviceSearchResponse(newSearchPacket("upnp:rootdevice"));

    List<DatagramPacket> responses = receiveResponses();
    assertEquals(1, responses.size());
    String res =
        new String(
            responses.get(0).getData(),
            0,
            responses.get(0).getLength(),
            StandardCharsets.UTF_8);
    assertTrue(res, res.endsWith("\r\n\r\n"));
  }
}
