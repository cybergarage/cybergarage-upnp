/******************************************************************
 *
 *	CyberHTTP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: HostInterface.java
 *
 *	Revision;
 *
 *	05/12/03
 *		- first revision.
 *	05/13/03
 *		- Added support for IPv6 and loopback address.
 *	02/15/04
 *		- Added the following methods to set only a interface.
 *		- setInterface(), getInterfaces(), hasAssignedInterface()
 *	06/30/04
 *		- Moved the package from org.cybergarage.http to org.cybergarage.net.
 *	06/30/04
 *		- Theo Beisch <theo.beisch@gmx.de>
 *		- Changed isUseAddress() to isUsableAddress().
 *
 ******************************************************************/

package org.cybergarage.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import org.cybergarage.util.Debug;

public class HostInterface {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  public static boolean USE_LOOPBACK_ADDR = false;
  public static boolean USE_ONLY_IPV4_ADDR = false;
  public static boolean USE_ONLY_IPV6_ADDR = false;

  ////////////////////////////////////////////////
  //	Network Interfaces
  ////////////////////////////////////////////////

  private static String ifAddress = "";
  public static final int IPV4_BITMASK = 0x0001;
  public static final int IPV6_BITMASK = 0x0010;
  public static final int LOCAL_BITMASK = 0x0100;

  public static final void setInterface(String ifaddr) {
    ifAddress = ifaddr;
  }

  public static final String getInterface() {
    return ifAddress;
  }

  private static final boolean hasAssignedInterface() {
    return (0 < ifAddress.length()) ? true : false;
  }

  ////////////////////////////////////////////////
  //	Network Interfaces
  ////////////////////////////////////////////////

  // Thanks for Theo Beisch (10/27/04)

  private static final boolean isUsableAddress(InetAddress addr) {
    if (USE_LOOPBACK_ADDR == false) {
      if (addr.isLoopbackAddress() == true) return false;
    }
    if (USE_ONLY_IPV4_ADDR == true) {
      if (addr instanceof Inet6Address) return false;
    }
    if (USE_ONLY_IPV6_ADDR == true) {
      if (addr instanceof Inet4Address) return false;
    }
    return true;
  }

  // Interfaces that are down cannot be bound or reached, and loopback interfaces are only
  // usable when loopback addresses are enabled (#44).
  private static final boolean isUsableInterface(NetworkInterface ni) {
    try {
      if (ni.isUp() == false) return false;
      if (USE_LOOPBACK_ADDR == false && ni.isLoopback() == true) return false;
    } catch (SocketException e) {
      return false;
    }
    return true;
  }

  /**
   * Returns a snapshot of the usable host addresses.
   *
   * <p>Callers that need several addresses should use this method instead of combining {@link
   * #getNHostAddresses()} and {@link #getHostAddress(int)}, so that the count and the addresses
   * come from the same enumeration even if the network configuration changes in between.
   *
   * @return usable host addresses, or an empty array if none are available
   */
  public static final String[] getHostAddresses() {
    if (hasAssignedInterface() == true) return new String[] {getInterface()};

    ArrayList hostAddrs = new ArrayList();
    try {
      Enumeration nis = NetworkInterface.getNetworkInterfaces();
      while (nis != null && nis.hasMoreElements()) {
        NetworkInterface ni = (NetworkInterface) nis.nextElement();
        if (isUsableInterface(ni) == false) continue;
        Enumeration addrs = ni.getInetAddresses();
        while (addrs.hasMoreElements()) {
          InetAddress addr = (InetAddress) addrs.nextElement();
          if (isUsableAddress(addr) == false) continue;
          String host = addr.getHostAddress();
          // if (addr instanceof Inet6Address)
          //	host = "[" + host + "]";
          hostAddrs.add(host);
        }
      }
    } catch (Exception e) {
      Debug.warning(e);
    }
    return (String[]) hostAddrs.toArray(new String[0]);
  }

  public static final int getNHostAddresses() {
    return getHostAddresses().length;
  }

  /**
   * @param ipfilter
   * @param interfaces
   * @return
   * @since 1.8.0
   * @author Stefano "Kismet" Lenzi &lt;kismet.sl@gmail.com&gt;
   */
  public static final InetAddress[] getInetAddress(int ipfilter, String[] interfaces) {
    Enumeration nis;
    if (interfaces != null) {
      Vector iflist = new Vector();
      for (int i = 0; i < interfaces.length; i++) {
        NetworkInterface ni;
        try {
          ni = NetworkInterface.getByName(interfaces[i]);
        } catch (SocketException e) {
          continue;
        }
        if (ni != null) iflist.add(ni);
      }
      nis = iflist.elements();
    } else {
      try {
        nis = NetworkInterface.getNetworkInterfaces();
      } catch (SocketException e) {
        return null;
      }
    }
    ArrayList addresses = new ArrayList();
    while (nis.hasMoreElements()) {
      NetworkInterface ni = (NetworkInterface) nis.nextElement();
      try {
        if (ni.isUp() == false) continue;
      } catch (SocketException e) {
        continue;
      }
      Enumeration addrs = ni.getInetAddresses();
      while (addrs.hasMoreElements()) {
        InetAddress addr = (InetAddress) addrs.nextElement();
        if (((ipfilter & LOCAL_BITMASK) == 0) && addr.isLoopbackAddress()) continue;

        if (((ipfilter & IPV4_BITMASK) != 0) && addr instanceof Inet4Address) {
          addresses.add(addr);
        } else if (((ipfilter & IPV6_BITMASK) != 0) && addr instanceof InetAddress) {
          addresses.add(addr);
        }
      }
    }
    return (InetAddress[]) addresses.toArray(new InetAddress[] {});
  }

  public static final String getHostAddress(int n) {
    String[] hostAddrs = getHostAddresses();
    if (n < 0 || hostAddrs.length <= n) return "";
    return hostAddrs[n];
  }

  ////////////////////////////////////////////////
  //	isIPv?Address
  ////////////////////////////////////////////////

  public static final boolean isIPv6Address(String host) {
    try {
      InetAddress addr = InetAddress.getByName(host);
      if (addr instanceof Inet6Address) return true;
      return false;
    } catch (Exception e) {
    }
    return false;
  }

  public static final boolean isIPv4Address(String host) {
    try {
      InetAddress addr = InetAddress.getByName(host);
      if (addr instanceof Inet4Address) return true;
      return false;
    } catch (Exception e) {
    }
    return false;
  }

  ////////////////////////////////////////////////
  //	hasIPv?Interfaces
  ////////////////////////////////////////////////

  public static final boolean hasIPv4Addresses() {
    String[] hostAddrs = getHostAddresses();
    for (int n = 0; n < hostAddrs.length; n++) {
      String addr = hostAddrs[n];
      if (isIPv4Address(addr) == true) return true;
    }
    return false;
  }

  public static final boolean hasIPv6Addresses() {
    String[] hostAddrs = getHostAddresses();
    for (int n = 0; n < hostAddrs.length; n++) {
      String addr = hostAddrs[n];
      if (isIPv6Address(addr) == true) return true;
    }
    return false;
  }

  ////////////////////////////////////////////////
  //	hasIPv?Interfaces
  ////////////////////////////////////////////////

  public static final String getIPv4Address() {
    String[] hostAddrs = getHostAddresses();
    for (int n = 0; n < hostAddrs.length; n++) {
      String addr = hostAddrs[n];
      if (isIPv4Address(addr) == true) return addr;
    }
    return "";
  }

  public static final String getIPv6Address() {
    String[] hostAddrs = getHostAddresses();
    for (int n = 0; n < hostAddrs.length; n++) {
      String addr = hostAddrs[n];
      if (isIPv6Address(addr) == true) return addr;
    }
    return "";
  }

  ////////////////////////////////////////////////
  //	getHostURL
  ////////////////////////////////////////////////

  public static final String getHostURL(String host, int port, String uri) {
    String hostAddr = host;
    if (isIPv6Address(host) == true) hostAddr = "[" + host + "]";
    return "http://" + hostAddr + ":" + Integer.toString(port) + uri;
  }
}
