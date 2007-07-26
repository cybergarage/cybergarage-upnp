/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClientService.java
*
*	03/03/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.server;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

public class RemoteUIServerService implements ActionListener, QueryListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:RemoteUIServer:1";		
	
	public final static String SCPD = 
		"<?xml version=\"1.0\"encoding=\"utf-8\"?>\n"+
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n"+
		"  <serviceStateTable>\n"+
		"    <stateVariable><Optional/>\n"+
		"      <name>UIListingUpdate</name>\n"+
		"      <sendEventsAttribute>yes</sendEventsAttribute>\n"+
		"      <dataType>string</dataType>\n"+
		"    </stateVariable>\n"+
		"    <stateVariable>\n"+
		"      <name>A_ARG_TYPE_DeviceProfile</name>\n"+
		"      <sendEventsAttribute>no</sendEventsAttribute>\n"+
		"      <dataType>string</dataType>\n"+
		"    </stateVariable>\n"+
		"    <stateVariable><Optional/>\n"+
		"      <name>A_ARG_TYPE_URI</name>\n"+
		"      <sendEventsAttribute>no</sendEventsAttribute>\n"+
		"      <dataType>uri</dataType>\n"+
		"    </stateVariable>\n"+
		"    <stateVariable>\n"+
		"      <name>A_ARG_TYPE_CompatibleUIs</name>\n"+
		"      <sendEventsAttribute>no</sendEventsAttribute>\n"+
		"      <dataType>string</dataType>\n"+
		"    </stateVariable>\n"+
		"    <stateVariable>\n"+
		"      <name>A_ARG_TYPE_String</name>\n"+
		"      <sendEventsAttribute>no</sendEventsAttribute>\n"+
		"      <dataType>string</dataType>\n"+
		"    </stateVariable>\n"+
		"    <stateVariable><Optional/>\n"+
		"      <name>A_ARG_TYPE_Int</name>\n"+
		"      <sendEventsAttribute>no</sendEventsAttribute>\n"+
		"      <dataType>int</dataType>\n"+
		"    </stateVariable>\n"+
		"  </serviceStateTable>\n"+
		"  <actionList>\n"+
		"    <action>\n"+
		"      <name>GetCompatibleUIs</name>\n"+
		"      <argumentList>\n"+
		"        <argument>\n"+
		"          <name>InputDeviceProfile</name>\n"+
		"          <direction>in</direction>\n"+
		"          <relatedStateVariable>A_ARG_TYPE_DeviceProfile</relatedStateVariable>\n"+
		"        </argument>\n"+
		"        <argument>\n"+
		"          <name>UIFilter</name>\n"+
		"          <direction>in</direction>\n"+
		"          <relatedStateVariable>A_ARG_TYPE_String</relatedStateVariable>\n"+
		"        </argument>\n"+
		"        <argument>\n"+
		"          <name>UIListing</name>\n"+
		"          <direction>out</direction>\n"+
		"          <relatedStateVariable>A_ARG_TYPE_CompatibleUIs</relatedStateVariable>\n"+
		"        </argument>\n"+
		"      </argumentList>\n"+
		"    </action>\n"+
		"    <action><Optional/>\n"+
		"      <name>SetUILifetime</name>\n"+
		"      <argumentList>\n"+
		"        <argument>\n"+
		"          <name>UI</name>\n"+
		"          <direction>in</direction>\n"+
		"          <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n"+
		"        </argument>\n"+
		"        <argument>\n"+
		"          <name>Lifetime</name>\n"+
		"          <direction>in</direction>\n"+
		"          <relatedStateVariable>A_ARG_TYPE_Int</relatedStateVariable>\n"+
		"        </argument>\n"+
		"      </argumentList>\n"+
		"    </action>\n"+
		"  </actionList>\n"+
		"</scpd>";
	
	////////////////////////////////////////////////
	// Constructor 
	////////////////////////////////////////////////
	
	public RemoteUIServerService()
	{
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		return false;
	}

	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		return false;
	}

}

