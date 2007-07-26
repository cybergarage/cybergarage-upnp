/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ClientList.java
*
*	Revision:
*
*	07/28/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.util.*;

public class ServerList extends SessionList
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public ServerList() 
	{
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public void addServer(Server obj)
	{
		addSession(obj);
	}
	
	public Server getServer(int n)
	{
		return (Server)getSession(n);
	}
	
	public Server getClinet(byte uuid[])
	{
		return (Server)getSession(uuid);
	}
	
	public boolean hasClinet(byte uuid[])
	{
		return hasSession(uuid);
	}
	
	////////////////////////////////////////////////
	// Command
	////////////////////////////////////////////////
	
	public synchronized void search(String target)
	{
		if (target == null)
			return;
		
		int nServer = size();
		for (int n=0; n<nServer; n++) {
			Server server = getServer(n);
			if (server == null)
				continue;
			Command cmd = new Command();
			cmd.search(server, server.getUUID(), target);
		}
	}
}
