/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : UpnpDump.java
*
******************************************************************/

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.ssdp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.*;

public class UpnpDump extends ControlPoint implements NotifyListener, EventListener, SearchResponseListener
{
	////////////////////////////////////////////////
	//	Constractor
	////////////////////////////////////////////////
    
	public UpnpDump()
	{
		addNotifyListener(this);
		addSearchResponseListener(this);
		addEventListener(this);
	}
    
	////////////////////////////////////////////////
	//	Graphics
	////////////////////////////////////////////////
	
	public void printConsole(String msg)
	{
        System.out.println(msg);
	}
	
	////////////////////////////////////////////////
	//	Listener
	////////////////////////////////////////////////
	
	public void deviceNotifyReceived(SSDPPacket packet)
	{
		System.out.println(packet.toString());
		
		if (packet.isDiscover() == true) {
			String st = packet.getST();
			printConsole("ssdp:discover : ST = " + st); 
		}
		else if (packet.isAlive() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
			String url = packet.getLocation();
			printConsole("ssdp:alive : uuid = " + usn + ", NT = " + nt + ", location = " + url); 
		}
		else if (packet.isByeBye() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
			printConsole("ssdp:byebye : uuid = " + usn + ", NT = " + nt); 
		}
	}
	
	public void deviceSearchResponseReceived(SSDPPacket packet)
	{
		String uuid = packet.getUSN();
		String st = packet.getST();
		String url = packet.getLocation();
		printConsole("device search res : uuid = " + uuid + ", ST = " + st + ", location = " + url); 
	}
	
	public void eventNotifyReceived(String uuid, long seq, String name, String value)
	{
		printConsole("event notify : uuid = " + uuid + ", seq = " + seq + ", name = " + name + ", value =" + value); 
	}

	////////////////////////////////////////////////
	//	main
	////////////////////////////////////////////////
			
	public static void main(String args[]) 
	{
		UpnpDump upnpDump = new UpnpDump();
		upnpDump.start();
		upnpDump.search();
	}
}
