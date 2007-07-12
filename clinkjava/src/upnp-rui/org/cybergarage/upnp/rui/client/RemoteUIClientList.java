/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClientList.java
*
*	03/14/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.client;

import java.util.*;

public class RemoteUIClientList extends Vector 
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public RemoteUIClientList() 
	{
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public RemoteUIClient getClient(int n)
	{
		return (RemoteUIClient)get(n);
	}
}

