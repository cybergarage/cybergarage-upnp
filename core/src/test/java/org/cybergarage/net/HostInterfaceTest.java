/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2026
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import junit.framework.TestCase;

public class HostInterfaceTest extends TestCase {

  public void testHostAddressesAreConsistent() {
    String[] hostAddrs = HostInterface.getHostAddresses();
    assertNotNull(hostAddrs);
    assertEquals(hostAddrs.length, HostInterface.getNHostAddresses());
    for (int n = 0; n < hostAddrs.length; n++) {
      assertNotNull(hostAddrs[n]);
      assertTrue(0 < hostAddrs[n].length());
      assertEquals(hostAddrs[n], HostInterface.getHostAddress(n));
    }
    assertEquals("", HostInterface.getHostAddress(hostAddrs.length));
    assertEquals("", HostInterface.getHostAddress(-1));
  }

  public void testHostAddressesSkipDownAndLoopbackInterfaces() throws Exception {
    // Addresses of interfaces that are down or loopback must not be used (#44).
    String[] hostAddrs = HostInterface.getHostAddresses();
    Enumeration nis = NetworkInterface.getNetworkInterfaces();
    while (nis != null && nis.hasMoreElements()) {
      NetworkInterface ni = (NetworkInterface) nis.nextElement();
      boolean usable = ni.isUp() && (HostInterface.USE_LOOPBACK_ADDR || !ni.isLoopback());
      if (usable) continue;
      Enumeration addrs = ni.getInetAddresses();
      while (addrs.hasMoreElements()) {
        String addr = ((InetAddress) addrs.nextElement()).getHostAddress();
        for (int n = 0; n < hostAddrs.length; n++) {
          assertFalse(addr + " of " + ni.getName(), addr.equals(hostAddrs[n]));
        }
      }
    }
  }

  public void testAssignedInterface() {
    String ifAddr = HostInterface.getInterface();
    try {
      HostInterface.setInterface("192.168.0.1");
      String[] hostAddrs = HostInterface.getHostAddresses();
      assertEquals(1, hostAddrs.length);
      assertEquals("192.168.0.1", hostAddrs[0]);
      assertEquals(1, HostInterface.getNHostAddresses());
      assertEquals("192.168.0.1", HostInterface.getHostAddress(0));
    } finally {
      HostInterface.setInterface(ifAddr);
    }
  }
}
