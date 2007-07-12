/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClientServiceXRT.java
*
*	03/30/06
*		- first revision.
*
******************************************************************/


package org.cybergarage.upnp.rui.client.xrt;

import java.net.*;
import java.io.*;
import javax.net.*;
import javax.net.ssl.*;

import org.cybergarage.util.*;
import org.cybergarage.xml.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.rui.*;
import org.cybergarage.upnp.rui.client.*;
import org.cybergarage.upnp.rui.xrt.*;


public class RemoteUIClientServiceXRT extends RemoteUIClientService
{
	////////////////////////////////////////////////
	// Constructor 
	////////////////////////////////////////////////
	
	public RemoteUIClientServiceXRT()
	{
		setListener(null);
	}
	
	////////////////////////////////////////////////
	//	Command Lintener
	////////////////////////////////////////////////
	
	private XRTCommandListener cmdListener;
	
	public void setListener(XRTCommandListener value)
	{
		cmdListener = value;
	}
	
	public XRTCommandListener getListener()
	{
		return cmdListener;
	}

	////////////////////////////////////////////////
	// connect
	////////////////////////////////////////////////
	
	public boolean addUIListing(Session session)
	{
		XRTSession xrtSession = (XRTSession)session;
		if (xrtSession.open() == false)
			return false;
		xrtSession.setListener(getListener());
		return true;
	}

	////////////////////////////////////////////////
	// close
	////////////////////////////////////////////////

	public boolean close()
	{
		Session session = getSession();
		if (session == null)
			return false;
		
		session.close();
		
		return true;
	}

	////////////////////////////////////////////////
	// postCommand
	////////////////////////////////////////////////
	
	public synchronized boolean postCommand(short commandCode, byte[] commandData)
	{
		Session session = getSession();
		if (session == null)
			return false;
		if (session.isConnected() == false) {
			session.close();
			if (session.open() == false)
				return false;
		}
		XRTCommand xrtCmd = new XRTCommand();
		xrtCmd.setCode(commandCode);
		xrtCmd.setData(commandData);
		return xrtCmd.send(session);
	}
}

