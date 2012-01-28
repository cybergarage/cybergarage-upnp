/******************************************************************
*
*	CyberLink for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: UpnpProxy
*
*	Revision:
*
*	04/24/06
*		- first revision.
*
******************************************************************/

import org.cybergarage.xml.*;
import org.cybergarage.util.*;

import org.cybergarage.upnp.xml.*;
import org.cybergarage.upnp.control.*;

public class UpnpProxy
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public UpnpProxy()
	{
	}
	
	////////////////////////////////////////////////
	//	userData
	////////////////////////////////////////////////

	private Object userData = null; 
	
	public void setUserData(Object data) 
	{
		userData = data;
	}

	public Object getUserData() 
	{
		return userData;
	}
}
