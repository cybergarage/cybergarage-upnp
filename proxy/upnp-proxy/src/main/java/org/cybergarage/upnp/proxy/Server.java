/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: Server.java
*
*	Revision:
*
*	07/28/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

public class Server extends Session
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public Server()
	{
	}

	public Server(String host, int port)
	{
		setHost(host);
		setPort(port);
	}

	////////////////////////////////////////////////
	//	Login
	////////////////////////////////////////////////
	
	public boolean login(byte uuid[], String passwd)
	{
		Command cmd = new Command();
		return cmd.login(this, uuid, passwd);
	}
}

