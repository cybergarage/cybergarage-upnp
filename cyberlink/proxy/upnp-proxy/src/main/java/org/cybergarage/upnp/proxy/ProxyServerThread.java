/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ProxyServerThread.java
*
*	Revision:
*
*	05/05/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.net.*;

public class ProxyServerThread extends Thread
{
	private Proxy proxy;
	private Socket sock;
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public ProxyServerThread(Proxy proxy, Socket sock)
	{
		this.proxy = proxy;
		this.sock = sock;
	}

	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread recvThread = null;
		
	public void run()
	{
		if (proxy == null || sock == null)
			return;
		
		Thread thisThread = Thread.currentThread();
		
		try {
			while (recvThread == thisThread) {
				Command cmd = new Command();
				if (cmd.recv(sock) == false)
					break;
				proxy.commandReceived(cmd);
			}
		}
		catch (Exception e) {}
	}
	
	public void start()
	{
		recvThread = new Thread(this);
		recvThread.start();
	}

/*
	public void stop()
	{
		recvThread = null;
	}
*/
}
