/******************************************************************
 *
 *	CyberHTTP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: HTTPServerThread.java
 *
 *	Revision;
 *
 *	10/10/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.http;

import java.net.Socket;

public class HTTPServerThread extends Thread {
  private HTTPServer httpServer;
  private Socket sock;
  private boolean admitted;

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  public HTTPServerThread(HTTPServer httpServer, Socket sock) {
    super("Cyber.HTTPServerThread");
    this.httpServer = httpServer;
    this.sock = sock;
  }

  ////////////////////////////////////////////////
  //	run
  ////////////////////////////////////////////////

  HTTPServerThread(HTTPServer server, Socket socket, boolean admitted) {
    this(server, socket);
    this.admitted = admitted;
  }

  public void run() {
    HTTPSocket httpSock = new HTTPSocket(sock);
    try {
      if (!httpSock.open()) return;
      HTTPRequest httpReq = new HTTPRequest();
      httpReq.setSocket(httpSock);
      while (httpReq.read()) {
        httpServer.performRequestListener(httpReq);
        if (!httpReq.isKeepAlive()) break;
      }
    } finally {
      httpSock.close();
      if (admitted) httpServer.connectionClosed(sock);
    }
  }
}
