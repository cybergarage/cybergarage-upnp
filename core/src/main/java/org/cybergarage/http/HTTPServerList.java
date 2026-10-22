/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: HTTPServerList.java
 *
 *	Revision;
 *
 *	05/08/03
 *		- first revision.
 *	24/03/06
 *		- Stefano Lenzi:added debug information as request by Stephen More
 *
 ******************************************************************/

package org.cybergarage.http;

import java.net.InetAddress;
import java.util.Vector;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Device;
import org.cybergarage.util.Debug;

public class HTTPServerList extends Vector {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  private InetAddress[] binds = null;
  private int port = Device.HTTP_DEFAULT_PORT;

  public HTTPServerList() {}

  public HTTPServerList(InetAddress[] list, int port) {
    this.binds = list;
    this.port = port;
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  public void addRequestListener(HTTPRequestListener listener) {
    int nServers = size();
    for (int n = 0; n < nServers; n++) {
      HTTPServer server = getHTTPServer(n);
      server.addRequestListener(listener);
    }
  }

  public HTTPServer getHTTPServer(int n) {
    return (HTTPServer) get(n);
  }

  ////////////////////////////////////////////////
  //	open/close
  ////////////////////////////////////////////////

  public void close() {
    int nServers = size();
    for (int n = 0; n < nServers; n++) {
      HTTPServer server = getHTTPServer(n);
      server.close();
    }
  }

  public int open() {
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
    // discarding the servers that were already opened on the other addresses (#30).
    for (int i = 0; i < bindAddresses.length; i++) {
      if (bindAddresses[i] == null || bindAddresses[i].length() == 0) continue;
      HTTPServer httpServer = new HTTPServer();
      if (httpServer.open(bindAddresses[i], port) == false) {
        Debug.warning("Unable to open HTTP server on " + bindAddresses[i] + ":" + port);
        continue;
      }
      add(httpServer);
    }
    return size();
  }

  public boolean open(int port) {
    this.port = port;
    return open() != 0;
  }

  ////////////////////////////////////////////////
  //	start/stop
  ////////////////////////////////////////////////

  public void start() {
    int nServers = size();
    for (int n = 0; n < nServers; n++) {
      HTTPServer server = getHTTPServer(n);
      server.start();
    }
  }

  public void stop() {
    int nServers = size();
    for (int n = 0; n < nServers; n++) {
      HTTPServer server = getHTTPServer(n);
      server.stop();
    }
  }
}
