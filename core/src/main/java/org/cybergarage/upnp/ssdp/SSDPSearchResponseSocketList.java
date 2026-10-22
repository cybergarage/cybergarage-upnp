/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: SSDPSearchResponseSocketList.java
 *
 *	Revision;
 *
 *	05/08/03
 *		- first revision.
 *	05/28/03
 *		- Added post() to send a SSDPSearchRequest.
 *
 ******************************************************************/

package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.*;

import org.cybergarage.net.*;

import org.cybergarage.upnp.*;
import org.cybergarage.util.Debug;

public class SSDPSearchResponseSocketList extends Vector {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  private InetAddress[] binds = null;

  public SSDPSearchResponseSocketList() {}

  /**
   * @param binds The host to bind.Use <tt>null</tt> for the default behavior
   */
  public SSDPSearchResponseSocketList(InetAddress[] binds) {
    this.binds = binds;
  }

  ////////////////////////////////////////////////
  //	ControlPoint

  ////////////////////////////////////////////////
  //	ControlPoint
  ////////////////////////////////////////////////

  public void setControlPoint(ControlPoint ctrlPoint) {
    int nSockets = size();
    for (int n = 0; n < nSockets; n++) {
      SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
      sock.setControlPoint(ctrlPoint);
    }
  }

  ////////////////////////////////////////////////
  //	get
  ////////////////////////////////////////////////

  public SSDPSearchResponseSocket getSSDPSearchResponseSocket(int n) {
    return (SSDPSearchResponseSocket) get(n);
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  public boolean open(int port) {
    InetAddress[] binds = this.binds;
    String[] bindAddresses;
    if (binds != null) {
      bindAddresses = new String[binds.length];
      for (int i = 0; i < binds.length; i++) {
        bindAddresses[i] = binds[i].getHostAddress();
      }
    } else {
      bindAddresses = HostInterface.getHostAddresses();
    }
    // Skip addresses that cannot be bound (e.g. mobile data interfaces on Android) instead of
    // failing on every address, so that discovery still works on the usable interfaces (#30).
    // Fail only when no address could be bound, so that the caller retries with another port.
    for (int j = 0; j < bindAddresses.length; j++) {
      if (bindAddresses[j] == null || bindAddresses[j].length() == 0) continue;
      try {
        SSDPSearchResponseSocket socket = new SSDPSearchResponseSocket(bindAddresses[j], port);
        if (socket.getDatagramSocket() == null) continue;
        add(socket);
      } catch (Exception e) {
        Debug.warning("Unable to open SSDP search response socket on " + bindAddresses[j]);
      }
    }
    if (size() == 0) return false;
    return true;
  }

  public boolean open() {
    return open(SSDP.PORT);
  }

  public void close() {
    int nSockets = size();
    for (int n = 0; n < nSockets; n++) {
      SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
      sock.close();
    }
    clear();
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  public void start() {
    int nSockets = size();
    for (int n = 0; n < nSockets; n++) {
      SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
      sock.start();
    }
  }

  public void stop() {
    int nSockets = size();
    for (int n = 0; n < nSockets; n++) {
      SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
      sock.stop();
    }
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  public boolean post(SSDPSearchRequest req) {
    boolean ret = true;
    int nSockets = size();
    for (int n = 0; n < nSockets; n++) {
      SSDPSearchResponseSocket sock = getSSDPSearchResponseSocket(n);
      String bindAddr = sock.getLocalAddress();
      req.setLocalAddress(bindAddr);
      String ssdpAddr = SSDP.ADDRESS;
      if (HostInterface.isIPv6Address(bindAddr) == true) ssdpAddr = SSDP.getIPv6Address();
      // sock.joinGroup(ssdpAddr, SSDP.PORT, bindAddr);
      if (sock.post(ssdpAddr, SSDP.PORT, req) == false) ret = false;
      // sock.leaveGroup(ssdpAddr, SSDP.PORT, bindAddr);
    }
    return ret;
  }
}
