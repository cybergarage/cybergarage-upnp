/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClient.java
*
*	03/03/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.client;

import java.io.*;

import org.cybergarage.xml.*;
import org.cybergarage.net.*;
import org.cybergarage.util.*;
import org.cybergarage.http.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.rui.*;

public abstract class RemoteUIClient
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	public final static String DEVICE_TYPE = "urn:schemas-upnp-org:device:RemoteUIClientDevice:1";
	
	public final static int DEFAULT_HTTP_PORT = 38520;
	
	public final static int DEFAULT_DISPLAY_WIDTH = 640;
	public final static int DEFAULT_DISPLAY_HEIGHT = 480;
	public final static int DEFAULT_MAX_COMMAND_SIZE = 300000;
	public final static int DEFAULT_MAX_HOLD_UI = 10;
	public final static int DEFAULT_TIME_TO_LIVE = 1600;
	
	public final static String DEFAULT_FRIENDLY_NAME = "CyberGarage RemoteUI Client";
	public final static String DEFAULT_MANUFACTURE = "CyberGarage";
	public final static String DEFAULT_MANUFACTURE_URL = "http://www.cybergarage.org";

	public final static String DESCRIPTION = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n"+
		"<specVersion>\n"+
		"<major>1</major>\n"+
		"<minor>0</minor>\n"+
		"</specVersion>\n"+
		"<URLBase>base URL for all relative URLs</URLBase>\n"+
		"<device>\n"+
		"<deviceType>urn:schemas-upnp-org:device:RemoteUIClientDevice:1</deviceType>\n"+
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
		"<serviceType>urn:schemas-upnp-org:service:RemoteUIClient:1</serviceType>\n"+
		"<serviceId>urn:upnp-org:serviceId:RemoteUIClient1</serviceId>\n"+
		"<SCPDURL>/service/description.xml</SCPDURL>\n" +
		"<controlURL>/service/control</controlURL>\n" +
		"<eventSubURL>/service/eventSub</eventSubURL>\n" +
		"</service>\n"+
		"</serviceList>\n"+
		"<presentationURL>URL for presentation</presentationURL>\n"+
		"</device>\n"+
		"</root>";

	////////////////////////////////////////////////
	// Device
	////////////////////////////////////////////////
	
	private Device clientDev;
	
	private void setDevice(Device dev)
	{
		clientDev = dev;
	}

	public Device getDevice()
	{
		return clientDev;
	}
	
	private boolean isDeviceAvairable()
	{
		return (clientDev != null) ? true : false;
	}
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public RemoteUIClient(RemoteUIClientService ruiService)
	{
		setDevice(null);
		
		Device clientDev = new Device();
	
		try {
			clientDev.loadDescription(DESCRIPTION);	

     		Service service = clientDev.getService(RemoteUIClientService.SERVICE_TYPE);
			service.loadSCPD(RemoteUIClientService.SCPD);
		}
		catch (InvalidDescriptionException e) {}
		
		setDevice(clientDev);
		setService(ruiService);
		
		initialize();
	}
	
	////////////////////////////////////////////////
	// initialize
	////////////////////////////////////////////////
	
	private void initialize()
	{
		Device dev = getDevice();
		dev.setFriendlyName(DEFAULT_FRIENDLY_NAME);
		dev.setManufacture(DEFAULT_MANUFACTURE);
		dev.setManufactureURL(DEFAULT_MANUFACTURE_URL);
		
		RemoteUIClientService ruiService = getService();
		dev.setActionListener(ruiService);
		dev.setQueryListener(ruiService);
	
		setDisplayWidth(DEFAULT_DISPLAY_WIDTH);
		setDisplayHeight(DEFAULT_DISPLAY_HEIGHT);
		setMaxCommandSize(DEFAULT_MAX_COMMAND_SIZE);
		setMaxHoldUI(DEFAULT_MAX_HOLD_UI);
		setTimeToLive(DEFAULT_TIME_TO_LIVE);
	}
	
	protected void finalize()
	{
		if (isDeviceAvairable())
			getDevice().stop();		
	}
	
	////////////////////////////////////////////////
	// Service
	////////////////////////////////////////////////
	
	private RemoteUIClientService ruiService;
	
	private void setService(RemoteUIClientService service)
	{
		ruiService = service;
		service.setClient(this);
	}
	
	public RemoteUIClientService getService()
	{
		return ruiService;
	}

	////////////////////////////////////////////////
	// SessionList
	////////////////////////////////////////////////
	
	private SessionList sessionList = new SessionList();
	
	public SessionList getSessionList()
	{
		return sessionList;
	}

	////////////////////////////////////////////////
	// Display
	////////////////////////////////////////////////

	private int displayWidth;
	private int displayHeight;
	
	public void setDisplayWidth(int value)
	{
		displayWidth = value;
	}
	
	public void setDisplayHeight(int value)
	{
		displayHeight = value;
	}
	
	public int getDisplayWidth()
	{
		return displayWidth;
	}
	
	public int getDisplayHeight()
	{
		return displayHeight;
	}
	
	////////////////////////////////////////////////
	// maxHoldUI
	////////////////////////////////////////////////	

	private int maxHoldUI;
	
	public void setMaxHoldUI(int value)
	{
		maxHoldUI = value;
	}

	public int getMaxHoldUI()
	{
		return maxHoldUI;
	}
	
	////////////////////////////////////////////////
	// maxCommandSize
	////////////////////////////////////////////////
	
	private int maxCommandSize;
	
	public void setMaxCommandSize(int value)
	{
		maxCommandSize = value;
	}
	
	public int getMaxCommandSize()
	{
		return maxCommandSize;
	}
	
	////////////////////////////////////////////////
	// maxCommandSize
	////////////////////////////////////////////////
	
	private int timeToLive;
	
	public void setTimeToLive(int value)
	{
		timeToLive = value;
	}
	
	public int getTimeToLive()
	{
		return timeToLive;
	}
	
	////////////////////////////////////////////////
	// abstract methods
	////////////////////////////////////////////////
	
	public abstract String getProtocolName();
	
	public abstract String getProtocolVersion();
	
	public abstract String getImageEncoding();

	////////////////////////////////////////////////
	// start/stop
	////////////////////////////////////////////////
	
	public boolean start()
	{
		Device dev = getDevice();
		if (dev == null)
			return false;
		return dev.start();
	}

	public boolean stop()
	{
		Device dev = getDevice();
		if (dev == null)
			return false;
		return dev.stop();
	}
}

