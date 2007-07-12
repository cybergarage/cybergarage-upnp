/******************************************************************
*
*	CyberLink for Java
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClockServer.java
*
******************************************************************/

import org.cybergarage.util.*;
import org.cybergarage.upnp.rui.server.xrt.*;

public class RemoteUIClockServer extends RemoteUIServerXRT
{
	public RemoteUIClockServer()
	{
		super();
	}

	////////////////////////////////////////////////
	//	main
	////////////////////////////////////////////////

	public static void main(String args[]) 
	{
		Debug.on();

		RemoteUIClockServer remoteUIClockServer = new RemoteUIClockServer();
		remoteUIClockServer.start();
	}

}

