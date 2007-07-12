/******************************************************************
*
*	CyberFlash for Java
*
*	Copyright (C) Satoshi Konno 2005
*
*	File: XMLSocketServer.java
*
*	Revision;
*
*	09/06/05
*		- first revision.
*	
******************************************************************/

package org.cybergarage.flash;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.cybergarage.util.*;
import org.cybergarage.xml.*;

public class XMLSocketServer implements Runnable
{
	////////////////////////////////////////////////
	//	Constants
	////////////////////////////////////////////////
	
	public static final String VERSION = "1.0";
	public static final int DEFAULT_PORT = 36720;

	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public XMLSocketServer()
	{
		this(DEFAULT_PORT);
	}
	
	public XMLSocketServer(int port)
	{
		open(port);
	}

	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////

	private ServerSocket serverSock = null;
	private int bindPort = 0;
	
	public ServerSocket getServerSock()
	{
		return serverSock;
	}
	
	public int getBindPort()
	{
		return bindPort;
	}
	
	////////////////////////////////////////////////
	//	open/close
	////////////////////////////////////////////////
	
	public boolean open(int port)
	{
		if (serverSock != null)
			return true;
		try {
			bindPort = port;
			serverSock = new ServerSocket(bindPort);
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean close()
	{
		if (serverSock == null)
			return true;
		try {
			serverSock.close();
			serverSock = null;
			bindPort = 0;
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		return true;
	}

	public Socket accept()
	{
		if (serverSock == null)
			return null;
		try {
			Socket sock = serverSock.accept();
			return sock;
		}
		catch (Exception e) {
			return null;
		}
	}

	public boolean isOpened()
	{
		return (serverSock != null) ? true : false;
	}
	
	////////////////////////////////////////////////
	//	httpRequest
	////////////////////////////////////////////////

	private XMLSocketListener xmlSockListener = null;
	 	
	public void setXmlSocketListener(XMLSocketListener listener)
	{
		xmlSockListener = listener;
	}		

	public XMLSocketListener getXmlSocketListener()
	{
		return xmlSockListener;
	}		

	public Node performRequestListener(Node reqNode)
	{
		if (reqNode == null)
			return null;
		if (xmlSockListener == null)
			return null;
		return xmlSockListener.xmlRequestRecieved(reqNode);
	}		
	
	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread xmlSockThread = null;
		
	public void run()
	{
		if (isOpened() == false)
			return;
			
		Thread thisThread = Thread.currentThread();
		
		while (xmlSockThread == thisThread) {
			Thread.yield();
			Socket sock;
			try {
				Debug.message("accept ...");
				sock = accept();
				if (sock != null)
					Debug.message("sock = " + sock.getRemoteSocketAddress());
			}
			catch (Exception e){
				Debug.warning(e);
				break;
			}
			XMLSocketRequestThread xmlSockReqThread = new XMLSocketRequestThread(this, sock);
			xmlSockReqThread.start(); 
			Debug.message("xmlSockReqThread ...");
		}
	}
	
	public boolean start()
	{
		xmlSockThread = new Thread(this);
		xmlSockThread.start();
		return true;
	}
	
	public boolean stop()
	{
		xmlSockThread = null;
		return true;
	}

	////////////////////////////////////////////////
	//	main
	////////////////////////////////////////////////
	
	public static void main(String args[]) 
	{
		Debug.on();
		
		XMLSocketServer xmlSockServer = new XMLSocketServer();
		xmlSockServer.start();
	}
}
