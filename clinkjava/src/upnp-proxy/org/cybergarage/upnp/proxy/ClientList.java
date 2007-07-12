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
*	05/12/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.util.*;

public class ClientList extends SessionList
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public ClientList() 
	{
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public void addClient(Client obj)
	{
		addSession(obj);
	}
	
	public Client getClient(int n)
	{
		return (Client)getSession(n);
	}
	
	public Client getClinet(byte uuid[])
	{
		return (Client)getSession(uuid);
	}
	
	public boolean hasClinet(byte uuid[])
	{
		return hasSession(uuid);
	}
}
