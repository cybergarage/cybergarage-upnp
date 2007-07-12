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

package org.cybergarage.upnp.rui.server;

import java.io.*;

import org.cybergarage.xml.*;
import org.cybergarage.net.*;
import org.cybergarage.util.*;
import org.cybergarage.http.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.media.server.MediaServer;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.rui.*;
import org.cybergarage.upnp.rui.client.*;

public abstract class RemoteUIServer extends Device implements NotifyListener, EventListener, SearchResponseListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	public final static String DEVICE_TYPE = "urn:schemas-upnp-org:device:RemoteUIServerDevice:1";
	
	public final static int DEFAULT_HTTP_PORT = 39520;
	
	public final static String DESCRIPTION = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n"+
		"<specVersion>\n"+
		"<major>1</major>\n"+
		"<minor>0</minor>\n"+
		"</specVersion>\n"+
		"<URLBase>base URL for all relative URLs</URLBase>\n"+
		"<device>\n"+
		"<deviceType>urn:schemas-upnp-org:device:RemoteUIServerDevice:1 \n"+
		"</deviceType>\n"+
		"<friendlyName>short user-friendly title</friendlyName>\n"+
		"<manufacturer>manufacturer name</manufacturer>\n"+
		"<manufacturerURL>URL to manufacturer site</manufacturerURL>\n"+
		"<modelDescription>long user-friendly title</modelDescription>\n"+
		"<modelName>model name</modelName>\n"+
		"<modelNumber>model number</modelNumber>\n"+
		"<modelURL>URL to model site</modelURL>\n"+
		"<serialNumber>manufacturer's serial number</serialNumber>\n"+
		"<UDN>uuid:UUID</UDN>\n"+
		"<UPC>Universal Product Code</UPC>\n"+
		"<iconList>\n"+
		"<icon>\n"+
		"<mimetype>image/format</mimetype>\n"+
		"<width>horizontal pixels</width>\n"+
		"<height>vertical pixels</height>\n"+
		"<depth>color depth</depth>\n"+
		"<url>URL to icon</url>\n"+
		"</icon>\n"+
		"</iconList>\n"+
		"<serviceList>\n"+
		"<service>\n"+
		"<serviceType>urn:schemas-upnp-org:service:RemoteUIServer:1</serviceType>\n"+
		"<serviceId>urn:upnp-org:serviceId:RemoteIUServer1</serviceId>\n"+
		"<SCPDURL>/service/description.xml</SCPDURL>\n"+
		"<controlURL>/service/control</controlURL>\n"+
		"<eventSubURL>/service/eventSub</eventSubURL>\n"+
		"</service>\n"+
		"</serviceList>\n"+
		"<presentationURL>URL for presentation</presentationURL>\n"+
		"</device>\n"+
		"</root>\n";
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public RemoteUIServer()
	{
		super();

		try {
			loadDescription(DESCRIPTION);
		
			Service servService = getService(RemoteUIServerService.SERVICE_TYPE);
			servService.loadSCPD(RemoteUIServerService.SCPD);
		}
		catch (InvalidDescriptionException e) {};
		
		initialize();
	}
	
	private void initialize()
	{
		ControlPoint ctrlPonit = getControlPoint();
		
		ctrlPonit.addNotifyListener(this);
		ctrlPonit.addSearchResponseListener(this);
		ctrlPonit.addEventListener(this);
	}
	
	protected void finalize()
	{
		stop();		
	}
	
	////////////////////////////////////////////////
	// Service
	////////////////////////////////////////////////
	
	private RemoteUIServerService service;
	
	public RemoteUIServerService getService()
	{
		return service;
	}

	////////////////////////////////////////////////
	// Control Point
	////////////////////////////////////////////////
	
	private ControlPoint ctrlPoint = new ControlPoint();
	
	private ControlPoint getControlPoint()
	{
		return ctrlPoint;
	}
	
	////////////////////////////////////////////////
	// GetDeviceProfile
	////////////////////////////////////////////////
	
	private DeviceProfile getDeviceProfile(Device dev)
	{
		Action profileAction = dev.getAction(RemoteUIClientService.GET_DEVICE_PROFILE);
		if (profileAction == null)
			return null;
		if (profileAction.postControlAction() == false)
			return null;
		String resultMetaData = XML.unescapeXMLChars(profileAction.getArgumentValue(RemoteUIClientService.STATIC_DEVICE_INFO));
		DeviceProfile devProp = new DeviceProfile();
		devProp.setResultMetaData(resultMetaData);
		return devProp;
	}
	
	////////////////////////////////////////////////
	// Client
	////////////////////////////////////////////////
	
	private RemoteUIClientList clientList;
	
	public RemoteUIClientList getClientList()
	{
		return clientList;
	}

	private synchronized void updateNewClients()
	{
		DeviceList clientDevList = new DeviceList();
		
		// Find RemoteUI Client
		ControlPoint ctrlPoint = getControlPoint();
		DeviceList allDevList = ctrlPoint.getDeviceList();
		int allDevCnt = allDevList.size();
		for (int n=0; n<allDevCnt; n++) {
			Device dev = allDevList.getDevice(n);
			if (dev.isDeviceType(RemoteUIClient.DEVICE_TYPE) == false)
				continue;
			DeviceProfile devProf = (DeviceProfile)dev.getUserData();
			if (devProf != null) {
				if (devProf.isConnected() == true)
					continue;
			}
			dev.setUserData(null);
			devProf = getDeviceProfile(dev);
			if (devProf == null)
				continue;
			if (addUIListing(dev) == false)
				continue;
			devProf.setConnectedFlag(true);
			dev.setUserData(devProf);
		}
	}

	////////////////////////////////////////////////
	// start/stop (Overridded)
	////////////////////////////////////////////////

	public boolean start()
	{
		getControlPoint().start(); 
		return super.start();
	}

	public boolean stop()
	{
		getControlPoint().start(); 
		return super.stop();
	}
	
	////////////////////////////////////////////////
	// HttpRequestListner (Overridded)
	////////////////////////////////////////////////
	
	public void httpRequestRecieved(HTTPRequest httpReq)
	{
		super.httpRequestRecieved(httpReq);
	}

	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	public void update()
	{
		updateNewClients();
	}

	////////////////////////////////////////////////
	//	Control Point Listener
	////////////////////////////////////////////////
	
	public void deviceNotifyReceived(SSDPPacket packet)
	{
		updateNewClients();
	}
	
	public void deviceSearchResponseReceived(SSDPPacket packet)
	{
		updateNewClients();
	}
	
	public void eventNotifyReceived(String uuid, long seq, String name, String value)
	{
	}

	////////////////////////////////////////////////
	// abstract methods
	////////////////////////////////////////////////
	
	public abstract boolean addUIListing(Device dev);
}

