/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClientXRT.java
*
*	03/23/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.client.xrt;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.rui.client.*;


public class RemoteUIClientXRT extends RemoteUIClient
{
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public RemoteUIClientXRT()
	{
		super(new RemoteUIClientServiceXRT());
	}
	
	public String getProtocolName()
	{
		return "XRT2";
	}
	
	public String getProtocolVersion()
	{
		return "2.1";
	}
	
	public String getImageEncoding()
	{
		return "JPEG+PNG";
	}
	
}

