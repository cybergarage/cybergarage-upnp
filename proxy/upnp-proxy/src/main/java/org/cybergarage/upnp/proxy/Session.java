/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: Session.java
*
*	Revision:
*
*	05/12/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.net.Socket;

public class Session
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public Session()
	{
		setUUID(new byte[0]);
		setHost("");
		setPort(0);
		setSocket(null);
	}

	////////////////////////////////////////////////
	//	UUID
	////////////////////////////////////////////////
	
	private byte[] uuid;
	
	public void setUUID(byte[] value)
	{
		uuid = value;
	}
	
	public byte[] getUUID()
	{
		return uuid;
	}
	
	////////////////////////////////////////////////
	//	Host
	////////////////////////////////////////////////
	
	private String host;
	
	public void setHost(String value)
	{
		host = value;
	}
	
	public String getHost()
	{
		return host;
	}

	////////////////////////////////////////////////
	//	Port
	////////////////////////////////////////////////
	
	private int port;
	
	public void setPort(int value)
	{
		port = value;
	}
	
	public int getPort()
	{
		return port;
	}
	
	////////////////////////////////////////////////
	// Socket
	////////////////////////////////////////////////
	
	private Socket socket;
	
	public void setSocket(Socket sock)
	{
		socket = sock;
	}
	
	public Socket getSocket()
	{
		return socket;
	}
	
	public boolean isConnected()
	{
		if (socket == null)
			return false;
		if (socket.isClosed() == true)
			return false;
		return socket.isConnected();
	}
	
	////////////////////////////////////////////////
	// open
	////////////////////////////////////////////////

	public boolean open()
	{
		try {
			String host = getHost();
			int port = getPort();
			
			if (host == null || host.length() <= 0)
				return false;
			if (port <= 0)
				return false;
			
			Socket sock;
			//SSLSocketFactory sslSockFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			//sock = sslSockFactory.createSocket(host, port);
			sock = new Socket(host, port);
			setSocket(sock);
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	////////////////////////////////////////////////
	// close
	////////////////////////////////////////////////
	
	public boolean close()
	{
		Socket socket = getSocket();
		
		if (socket == null)
			return false;
		try {
			socket.close();
			setSocket(null);
		}
		catch (Exception e) {};
		return true;
	}
}
