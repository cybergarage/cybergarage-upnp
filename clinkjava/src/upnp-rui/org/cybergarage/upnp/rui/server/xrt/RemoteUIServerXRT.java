/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIServer.java
*
*	03/03/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.server.xrt;

import java.io.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.rui.client.*;
import org.cybergarage.upnp.rui.server.*;

public abstract class RemoteUIServerXRT extends RemoteUIServer
{
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public RemoteUIServerXRT()
	{
		super();
	}
	
	////////////////////////////////////////////////
	// addUIListing
	////////////////////////////////////////////////
	
	public boolean addUIListing(Device dev) 
	{
		Action addUiAction = dev.getAction(RemoteUIClientService.ADD_UI_LISTING);
		addUiAction.setArgumentValue(RemoteUIClientService.INPUT_UI_LIST, "");
		if (addUiAction == null)
			return false;
		if (addUiAction.postControlAction() == false)
			return false;
		return true;
	}
}

