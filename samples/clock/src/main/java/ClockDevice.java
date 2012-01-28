/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : ClockDevice.java
*
******************************************************************/

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.http.*;
import org.cybergarage.net.HostInterface;

public class ClockDevice extends Device implements ActionListener, QueryListener
{
	private final static String DESCRIPTION_FILE_NAME = "description/description.xml";
	private final static String PRESENTATION_URI = "/presentation";
	
	private final static String DEVICE_DESCRIPTION = 
		"<?xml version=\"1.0\" ?>  \n" +
		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\"> \n" +
		" 	<specVersion> \n" +
		" 		<major>1</major>  \n" +
		" 		<minor>0</minor>  \n" +
		" 	</specVersion> \n" +
		" 	<device> \n" +
		" 		<deviceType>urn:schemas-upnp-org:device:clock:1</deviceType>  \n" +
		" 		<friendlyName>CyberGarage Clock Device</friendlyName>  \n" +
		" 		<manufacturer>CyberGarage</manufacturer>  \n" +
		" 		<manufacturerURL>http://www.cybergarage.org</manufacturerURL>  \n" +
		" 		<modelDescription>CyberUPnP Clock Device</modelDescription>  \n" +
		" 		<modelName>Clock</modelName>  \n" +
		" 		<modelNumber>1.0</modelNumber>  \n" +
		" 		<modelURL>http://www.cybergarage.org</modelURL>  \n" +
		" 		<serialNumber>1234567890</serialNumber>  \n" +
		" 		<UDN>uuid:cybergarageClockDevice</UDN>  \n" +
		" 		<UPC>123456789012</UPC>  \n" +
		" 		<iconList> \n" +
		" 			<icon> \n" +
		" 				<mimetype>image/gif</mimetype>  \n" +
		" 				<width>48</width>  \n" +
		" 				<height>32</height>  \n" +
		" 				<depth>8</depth>  \n" +
		" 				<url>icon.gif</url>  \n" +
		" 			</icon> \n" +
		" 		</iconList> \n" +
		" 		<serviceList> \n" +
		" 			<service> \n" +
		" 				<serviceType>urn:schemas-upnp-org:service:timer:1</serviceType>  \n" +
		" 				<serviceId>urn:upnp-org:serviceId:timer:1</serviceId>  \n" +
		" 				<SCPDURL>/service/timer/description.xml</SCPDURL>  \n" +
		" 				<controlURL>/service/timer/control</controlURL>  \n" +
		" 				<eventSubURL>/service/timer/eventSub</eventSubURL>  \n" +
		" 			</service> \n" +
		" 		</serviceList> \n" +
		" 		<presentationURL>/presentation</presentationURL>  \n" +
		" 	</device> \n" +
		"</root>";

	private final static String SERVICE_DESCRIPTION =
		"<?xml version=\"1.0\"?> \n" +
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\" > \n" +
		" 	<specVersion> \n" +
		" 		<major>1</major> \n" +
		" 		<minor>0</minor> \n" +
		" 	</specVersion> \n" +
		" 	<actionList> \n" +
		" 		<action> \n" +
		" 			<name>SetTime</name> \n" +
		" 			<argumentList> \n" +
		" 				<argument> \n" +
		" 					<name>NewTime</name> \n" +
		" 					<relatedStateVariable>Time</relatedStateVariable> \n" +
		" 					<direction>in</direction> \n" +
		" 				</argument> \n" +
		" 				<argument> \n" +
		" 					<name>Result</name> \n" +
		" 					<relatedStateVariable>Result</relatedStateVariable> \n" +
		" 					<direction>out</direction> \n" +
		" 				</argument> \n" +
		" 			</argumentList> \n" +
		" 		</action> \n" +
		" 		<action> \n" +
		" 			<name>GetTime</name> \n" +
		" 			<argumentList> \n" +
		" 				<argument> \n" +
		" 					<name>CurrentTime</name> \n" +
		" 					<relatedStateVariable>Time</relatedStateVariable> \n" +
		" 					<direction>out</direction> \n" +
		" 				</argument> \n" +
		" 			</argumentList> \n" +
		" 		</action> \n" +
		" 	</actionList> \n" +
		" 	<serviceStateTable> \n" +
		" 		<stateVariable sendEvents=\"yes\"> \n" +
		" 			<name>Time</name> \n" +
		" 			<dataType>string</dataType> \n" +
		" 		</stateVariable> \n" +
		" 		<stateVariable sendEvents=\"no\"> \n" +
		" 			<name>Result</name> \n" +
		" 			<dataType>string</dataType> \n" +
		" 		</stateVariable> \n" +
		" 	</serviceStateTable> \n" +
		"</scpd>";


	private StateVariable timeVar;
	
	public ClockDevice() throws InvalidDescriptionException, IOException
	{
		super();
		loadDescription(DEVICE_DESCRIPTION);
		setSSDPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		setHTTPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		Service timeService = getService("urn:schemas-upnp-org:service:timer:1");
		timeService.loadSCPD(SERVICE_DESCRIPTION);

		Action getTimeAction = getAction("GetTime");
		getTimeAction.setActionListener(this);
		
		Action setTimeAction = getAction("SetTime");
		setTimeAction.setActionListener(this);
		
		ServiceList serviceList = getServiceList();
		Service service = serviceList.getService(0);
		service.setQueryListener(this);

		timeVar = getStateVariable("Time");
		
		setLeaseTime(60);
	}

	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		String actionName = action.getName();
		if (actionName.equals("GetTime") == true) {
			Clock clock = Clock.getInstance();
			String dateStr = clock.getDateString();
			Argument timeArg = action.getArgument("CurrentTime");
			timeArg.setValue(dateStr);
			return true;
		}
		if (actionName.equals("SetTime") == true) {
			Argument timeArg = action.getArgument("NewTime");
			String newTime = timeArg.getValue();
			Argument resultArg = action.getArgument("Result");
			resultArg.setValue("Not implemented (" + newTime + ")");
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		Clock clock = Clock.getInstance();
		stateVar.setValue(clock.getDateString());
		return true;
	}

	////////////////////////////////////////////////
	// HttpRequestListner
	////////////////////////////////////////////////
	
	public void httpRequestRecieved(HTTPRequest httpReq)
	{
		String uri = httpReq.getURI();
		if (uri.startsWith(PRESENTATION_URI) == false) {
			super.httpRequestRecieved(httpReq);
			return;
		}
			 
		Clock clock = Clock.getInstance();
		String contents = "<HTML><BODY><H1>" + clock.toString() + "</H1></BODY></HTML>";
		
		HTTPResponse httpRes = new HTTPResponse();
		httpRes.setStatusCode(HTTPStatus.OK);
		httpRes.setContent(contents);
		httpReq.post(httpRes);
	}

	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	public void update()
	{
			Clock clock = Clock.getInstance();
			String timeStr = clock.toString();
			timeVar.setValue(timeStr);
	}			
}

