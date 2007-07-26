/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIServerService.java
*
*	03/03/06
*		- first revision.
*
******************************************************************/


package org.cybergarage.upnp.rui.client;

import org.cybergarage.util.*;
import org.cybergarage.xml.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.rui.*;
import org.cybergarage.upnp.rui.xrt.*;


public abstract class RemoteUIClientService implements ActionListener, QueryListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:RemoteUIClient:1";		
	
	// Action	
	public final static String GET_CURRENT_CONNECTIONS= "GetCurrentConnections";
	public final static String GET_DEVICE_PROFILE = "GetDeviceProfile";
	public final static String ADD_UI_LISTING = "AddUIListing";
	
	// Argument
	public final static String STATIC_DEVICE_INFO = "StaticDeviceInfo";
	public final static String INPUT_UI_LIST = "InputUIList";
		
	public final static String SCPD = 
		"<?xml version=\"1.0\"encoding=\"utf-8\"?>\n"+
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n"+
		"   <specVersion>\n"+
		"      <major>1</major>\n"+
		"      <minor>0</minor>\n"+
		"   </specVersion>\n"+
		"   <actionList>\n"+
		"      <action>\n"+
		"         <Optional />\n"+
		"         <name>AddUIListing</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>InputUIList</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_CompatibleUIs</relatedStateVariable>\n"+
		"            </argument>\n"+
		"            <argument>\n"+
		"               <name>TimeToLive</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_Int</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <name>Connect</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>RequestedConnections</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>CurrentConnections</relatedStateVariable>\n"+
		"            </argument>\n"+
		"            <argument>\n"+
		"               <name>CurrentConnectionsList</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>CurrentConnections</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <name>Disconnect</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>RequestedDisconnects</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>CurrentConnections</relatedStateVariable>\n"+
		"            </argument>\n"+
		"            <argument>\n"+
		"               <name>CurrentConnectionsList</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>CurrentConnections</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <Optional />\n"+
		"         <name>DisplayMessage</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>MessageType</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_DisplayMessageType</relatedStateVariable>\n"+
		"            </argument>\n"+
		"            <argument>\n"+
		"               <name>Message</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_String</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <name>GetCurrentConnections</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>CurrentConnectionsList</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>CurrentConnections</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <name>GetDeviceProfile</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>StaticDeviceInfo</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>DeviceProfile</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <Optional />\n"+
		"         <name>GetUIListing</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>CompatibleUIList</name>\n"+
		"               <direction>out</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_CompatibleUIs</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <Optional />\n"+
		"         <name>ProcessInput</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>InputDataType</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_InputDataType</relatedStateVariable>\n"+
		"            </argument>\n"+
		"            <argument>\n"+
		"               <name>InputData</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_String</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"      <action>\n"+
		"         <Optional />\n"+
		"         <name>RemoveUIListing</name>\n"+
		"         <argumentList>\n"+
		"            <argument>\n"+
		"               <name>RemoveUIList</name>\n"+
		"               <direction>in</direction>\n"+
		"               <relatedStateVariable>A_ARG_TYPE_String</relatedStateVariable>\n"+
		"            </argument>\n"+
		"         </argumentList>\n"+
		"      </action>\n"+
		"   </actionList>\n"+
		"   <serviceStateTable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <Optional/><name>A_ARG_TYPE_CompatibleUIs</name>\n"+
		"         <dataType>string</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <Optional/><name>A_ARG_TYPE_DisplayMessageType</name>\n"+
		"         <dataType>string</dataType>\n"+
		"         <allowedValueList>\n"+
		"            <allowedValue>text/plain</allowedValue>\n"+
		"         </allowedValueList>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <Optional/><name>A_ARG_TYPE_Int</name>\n"+
		"         <dataType>i4</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <Optional/><name>A_ARG_TYPE_InputDataType</name>\n"+
		"         <dataType>string</dataType>\n"+
		"         <allowedValueList>\n"+
		"            <allowedValue>ASCII</allowedValue>\n"+
		"            <allowedValue>UNICODE</allowedValue>\n"+
		"            <allowedValue>ISO10646</allowedValue>\n"+
		"            <allowedValue>ISO8859</allowedValue>\n"+
		"         </allowedValueList>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"yes\">\n"+
		"         <Optional/><name>CurrentConnectionsEvent</name>\n"+
		"         <dataType>string</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <name>CurrentConnections</name>\n"+
		"         <dataType>string</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <name>DeviceProfile</name>\n"+
		"         <dataType>string</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"yes\">\n"+
		"         <Optional/><name>CompatibleUIsUpdateIDEvent</name>\n"+
		"         <dataType>i4</dataType>\n"+
		"      </stateVariable>\n"+
		"      <stateVariable sendEvents=\"no\">\n"+
		"         <Optional/><name>A_ARG_TYPE_String</name>\n"+
		"         <dataType>string</dataType>\n"+
		"      </stateVariable>\n"+
		"   </serviceStateTable>\n"+
		"</scpd>";

	////////////////////////////////////////////////
	// Constructor 
	////////////////////////////////////////////////
	
	public RemoteUIClientService()
	{
		setSession(null);
	}
	
	////////////////////////////////////////////////
	// Client
	////////////////////////////////////////////////
	
	private RemoteUIClient ruiClient;
	
	public void setClient(RemoteUIClient client)
	{
		ruiClient = client;
	}

	public RemoteUIClient getClient()
	{
		return ruiClient;
	}
	
	////////////////////////////////////////////////
	// Session
	////////////////////////////////////////////////
	
	private Session session;
	
	public void setSession(Session value)
	{
		session = value;
	}

	public Session getSession()
	{
		return session;
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		RemoteUIClient ruiClient = getClient();
		
		String actionName = action.getName();

		boolean ret = false;

		if (actionName.equals("GetDeviceProfile") == true) {
			Argument staticDevInfo = action.getArgument("StaticDeviceInfo");
			
			String protocolInfo =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<deviceprofile xmlns=\"urn:schemas-upnp-org:remoteui:devprofile-1-0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:schemas-upnp-org:remoteui:devprofile-1-0 DeviceProfile.xsd\">\n" +
				"<maxHoldUI>10</maxHoldUI>\n" +
				"<protocol shortName=\"" + ruiClient.getProtocolName() + "\">" +
				"<protocolInfo>" + 
				"version=" + ruiClient.getProtocolVersion() + ",displayWidth=" + ruiClient.getDisplayWidth() + ",displayHeight=" + ruiClient.getDisplayHeight() + ",imageEncoding=" + ruiClient.getImageEncoding() + ",maxCommandSize=" + ruiClient.getMaxCommandSize() + 
				"</protocolInfo>\n" +
				"</protocol>\n" +
				"</deviceprofile>";
			staticDevInfo.setValue(XML.escapeXMLChars(protocolInfo));
			
			ret = true;
		}
		else if (actionName.equals("GetCurrentConnections") == true) {
			Argument currConList = action.getArgument("CurrentConnectionsList");
			currConList.setValue("");
			ret = true;
		}
		else if (actionName.equals("AddUIListing") == true) {
			ret = addUIListing(action);
		}

		return ret;
	}

	////////////////////////////////////////////////
	// AddUIListing
	////////////////////////////////////////////////
	
	private boolean addUIListing(Action action)
	{
		Argument inputUIList = action.getArgument("InputUIList");

		Parser xmlParser = UPnP.getXMLParser();
		Node rootNode = null;
		try {
			rootNode = xmlParser.parse(inputUIList.getValue());
		}
		catch (ParserException pe) {
			Debug.warning(pe);
			return false;
		}

		if (rootNode == null)
			return false;
		Node uiNode = rootNode.getNode("ui");
		if (uiNode == null)
			return false;
		Node protocolNode = uiNode.getNode("protocol");
		if (protocolNode == null)
			return false;
		
		String uiID = uiNode.getNodeValue("uiID");
		String name = uiNode.getNodeValue("name");
		String desc = uiNode.getNodeValue("description");
		String protoName = protocolNode.getAttributeValue("shortName");
		String protoURI = protocolNode.getNodeValue("uri");
		
		if (protoURI == null || protoURI.length() <= 0)
			return false;
		
		Session session = new XRTSession();
		session.setUIID(uiID);
		session.setName(name);
		session.setDescription(desc);
		session.setProtocolName(protoName);
		session.setProtocolURI(protoURI);

		boolean conResult = addUIListing(session);
		if (conResult == false) {
			return false;
		}
	
		setSession(session);
		
		Argument timeToLive = action.getArgument("TimeToLive");
		timeToLive.setValue(ruiClient.getTimeToLive());
		
		return true;
	}
	
	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		return false;
	}

	////////////////////////////////////////////////
	// abstract methods
	////////////////////////////////////////////////
	
	public abstract boolean addUIListing(Session session);
	public abstract boolean close();
}

