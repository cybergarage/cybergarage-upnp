/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : XRTCommandListener
*
*	04/18/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.xrt;

public interface XRTCommandListener
{
	public boolean commandReceived(XRTCommand xrtCmd);
}
