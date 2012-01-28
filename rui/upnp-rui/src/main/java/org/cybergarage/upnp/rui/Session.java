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

package org.cybergarage.upnp.rui;

import java.net.Socket;

public abstract class Session
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public Session()
	{
		setSocket(null);
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
	//	UIID
	////////////////////////////////////////////////
	
	private String uiId;
	
	public void setUIID(String value)
	{
		uiId = value;
	}
	
	public String getUIID()
	{
		return uiId;
	}

	////////////////////////////////////////////////
	//	Name
	////////////////////////////////////////////////
	
	private String name;
	
	public void setName(String value)
	{
		name = value;
	}
	
	public String getName()
	{
		return name;
	}

	////////////////////////////////////////////////
	//	Description
	////////////////////////////////////////////////
	
	private String desc;
	
	public void setDescription(String value)
	{
		desc = value;
	}
	
	public String getDescription()
	{
		return desc;
	}

	////////////////////////////////////////////////
	//	ProtocolName
	////////////////////////////////////////////////
	
	private String protocolName;
	
	public void setProtocolName(String value)
	{
		protocolName = value;
	}
	
	public String getProtocolName()
	{
		return protocolName;
	}

	////////////////////////////////////////////////
	//	ProtocolURI
	////////////////////////////////////////////////
	
	private String protocolURI;
	
	public void setProtocolURI(String value)
	{
		protocolURI = value;
	}
	
	public String getProtocolURI()
	{
		return protocolURI;
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
	// Socket (abstract)
	////////////////////////////////////////////////
	
	public abstract boolean open();
	public abstract boolean close();
}
