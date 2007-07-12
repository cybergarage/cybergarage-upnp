/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : SessionList.java
*
*	03/14/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui;

import java.util.*;

public class SessionList extends Vector 
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public SessionList() 
	{
		currMaxID = 1;
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public Session getSession(int n)
	{
		return (Session)get(n);
	}

	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	private long currMaxID;
	
	public synchronized long nextID() 
	{
		currMaxID++;
		return currMaxID;
	}
	
	public synchronized long getCurrentMaxID()
	{
		return currMaxID;
	}
	
}

