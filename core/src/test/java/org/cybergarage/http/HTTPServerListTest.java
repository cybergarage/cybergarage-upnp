/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2026
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.http;

import java.net.InetAddress;
import junit.framework.TestCase;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocketList;

public class HTTPServerListTest extends TestCase {

  // TEST-NET-1 (RFC 5737): never assigned to a local interface, so binding to it fails.
  private static InetAddress unbindableAddress() throws Exception {
    return InetAddress.getByName("192.0.2.123");
  }

  private static InetAddress loopbackAddress() throws Exception {
    return InetAddress.getByName("127.0.0.1");
  }

  public void testHTTPServerListSkipsUnbindableAddresses() throws Exception {
    // A single unbindable address must not discard servers opened on other addresses (#30).
    InetAddress[] binds = {loopbackAddress(), unbindableAddress()};
    HTTPServerList servers = new HTTPServerList(binds, 0);
    try {
      assertTrue(servers.open(0));
      assertEquals(1, servers.size());
      assertTrue(servers.getHTTPServer(0).isOpened());
    } finally {
      servers.close();
      servers.clear();
    }
  }

  public void testHTTPServerListFailsWithoutBindableAddress() throws Exception {
    HTTPServerList servers = new HTTPServerList(new InetAddress[] {unbindableAddress()}, 0);
    assertFalse(servers.open(0));
    assertEquals(0, servers.size());
  }

  public void testSSDPSearchResponseSocketListSkipsUnbindableAddresses() throws Exception {
    InetAddress[] binds = {unbindableAddress(), loopbackAddress()};
    SSDPSearchResponseSocketList sockets = new SSDPSearchResponseSocketList(binds);
    try {
      assertTrue(sockets.open(0));
      assertEquals(1, sockets.size());
    } finally {
      sockets.close();
      sockets.clear();
    }
  }

  public void testSSDPSearchResponseSocketListFailsWithoutBindableAddress() throws Exception {
    SSDPSearchResponseSocketList sockets =
        new SSDPSearchResponseSocketList(new InetAddress[] {unbindableAddress()});
    assertFalse(sockets.open(0));
    assertEquals(0, sockets.size());
  }
}
