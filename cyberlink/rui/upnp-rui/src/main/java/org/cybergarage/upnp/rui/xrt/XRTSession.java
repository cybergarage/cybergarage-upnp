/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : Session.java
*
*	03/14/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.xrt;

import org.cybergarage.upnp.rui.*;
import org.cybergarage.util.Debug;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

public class XRTSession extends Session implements Runnable
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public XRTSession()
	{
		super();
		setID(-1);
		setListener(null);
	}

	////////////////////////////////////////////////
	//	ID
	////////////////////////////////////////////////
	
	private long id;
	
	public void setID(long value)
	{
		id = value;
	}
	
	public long getID()
	{
		return id;
	}

	////////////////////////////////////////////////
	//	Command Lintener
	////////////////////////////////////////////////
	
	private XRTCommandListener cmdListener;
	
	public void setListener(XRTCommandListener value)
	{
		cmdListener = value;
	}
	
	public XRTCommandListener getListener()
	{
		return cmdListener;
	}

	////////////////////////////////////////////////
	// Socket (abstract)
	////////////////////////////////////////////////

	public boolean open()
	{
		try {
			String protoURIStr = getProtocolURI();
			URI protoURI = new URI(protoURIStr);
			
			String scheme = protoURI.getScheme();
			String host = protoURI.getHost();
			int port = protoURI.getPort();
			
			if (scheme == null)
				return false;
			
			Socket sock;
			if (scheme.compareToIgnoreCase("XRTS") == 0) {
				SSLSocketFactory sslSockFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
				sock = sslSockFactory.createSocket(host, port);
			}
			else
				sock = new Socket(host, port);
			setSocket(sock);
			
			if (XRTCommand.request(this, protoURIStr) == false)
				return false;
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		
		start();
		
		return true;
	}
	
	public boolean close()
	{
		stop();
		
		Socket socket = getSocket();
		
		if (socket == null)
			return false;
		try {
			XRTCommand.exit(this);
			socket.close();
		}
		catch (Exception e) {
			Debug.warning(e);
			//return false;
		};
		return true;
	}

	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread recvThread = null;
		
	public void run()
	{
		Thread thisThread = Thread.currentThread();
		
		try {
			while (recvThread == thisThread) {
				XRTCommand xrtCmd = new XRTCommand();
				if (xrtCmd.recv(this) == false)
					break;
				if (cmdListener == null)
					continue;
				cmdListener.commandReceived(xrtCmd);
			}
		}
		catch (Exception e) {}
	}
	
	public void start()
	{
		recvThread = new Thread(this);
		recvThread.start();
	}
	
	public void stop()
	{
		recvThread = null;
	}
}
