/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : CtrlPoint.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.ssdp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.*;

public class UpnpIGDTool extends ControlPoint implements NotifyListener, EventListener, SearchResponseListener
{
	private final static String TITLE = "CyberLink UPnP IGD Tool";
	public static int DEFAULT_WIDTH = 640;
	public final static int DEFAULT_HEIGHT = 480;
	
	private JFrame frame;
	private UpnpIGDToolPane mainPane;
	private JMenuBar menuBar;
	
	public UpnpIGDTool()
	{
		addNotifyListener(this);
		addSearchResponseListener(this);
		addEventListener(this);

		Debug.on();
		
		initFrame();
	}

	////////////////////////////////////////////////
	//	Frame
	////////////////////////////////////////////////

	private void initFrame()
	{
		frame = new JFrame(TITLE);

		frame.getContentPane().setLayout(new BorderLayout());
	
		menuBar = new UpnpIGDToolMenu(this);
		frame.setJMenuBar(menuBar);
		
		mainPane = new UpnpIGDToolPane(this);		
		getContentPane().add(mainPane, BorderLayout.CENTER);
		
		frame.setSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public JFrame getFrame()
	{
		return frame;
	}
	
	public Container getContentPane()
	{
		return getFrame().getContentPane();
	}
	
	////////////////////////////////////////////////
	//	Graphics
	////////////////////////////////////////////////
	
	public void printConsole(String msg)
	{
		mainPane.printConsole(msg); 
	}

	public void clearConsole()
	{
		mainPane.clearConsole();
	}

	public void updateDeviceList()
	{
		mainPane.updateDeviceComboList();
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
		updateDeviceList();
	}
	
	public void deviceSearchResponseReceived(SSDPPacket packet)
	{
		String uuid = packet.getUSN();
		String st = packet.getST();
		String url = packet.getLocation();
		printConsole("device search res : uuid = " + uuid + ", ST = " + st + ", location = " + url); 
		updateDeviceList();
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
		//Debug.on();
		for (int n=0; n<args.length; n++) {
			String opt = args[n];
			if (opt.equals("-v") || opt.equals("--verbose")) {
				Debug.on();
				Debug.message("Debug.on");			}
		}
		
		UpnpIGDTool igdtool = new UpnpIGDTool();
		igdtool.start();
	}

}
