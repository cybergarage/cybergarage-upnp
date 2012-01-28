/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: SessionList.java
*
*	Revision:
*
*	05/12/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.util.*;

public class SessionList extends Vector 
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public SessionList() 
	{
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public synchronized void addSession(Session obj)
	{
		add(obj);
	}
	
	public synchronized Session getSession(int n)
	{
		return (Session)get(n);
	}
	
	public synchronized Session getSession(byte uuid[])
	{
		if (uuid == null)
			return null;
		
		String uuidStr = new String(uuid);
		
		int nClient = size();
		for (int n=0; n<nClient; n++) {
			Session session = getSession(n);
			if (session == null)
				continue;
			if (uuidStr.compareTo(new String(session.getUUID())) == 0)
				return session;
		}
		return null;
	}
	
	public synchronized boolean hasSession(byte uuid[])
	{
		if (uuid == null)
			return false;
		
		String uuidStr = new String(uuid);
		
		int nSession = size();
		for (int n=0; n<nSession; n++) {
			Session session = getSession(n);
			if (session == null)
				continue;
			if (uuidStr.compareTo(new String(session.getUUID())) == 0)
				return true;
		}
		return false;
	}
	
	////////////////////////////////////////////////
	// Mutex
	////////////////////////////////////////////////
	
	public void lock()
	{
	}
	
	public void unlock()
	{
	}
}
