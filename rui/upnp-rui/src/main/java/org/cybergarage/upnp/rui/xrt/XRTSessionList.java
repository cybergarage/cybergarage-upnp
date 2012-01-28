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

package org.cybergarage.upnp.rui.xrt;

import java.util.*;
import org.cybergarage.upnp.rui.*;

public class XRTSessionList extends SessionList
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public XRTSessionList() 
	{
		super();
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public XRTSession getXRTSession(int n)
	{
		return (XRTSession)get(n);
	}
}

