/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ProxyFrame.java
*
*	Revision:
*
*	08/18/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy.gui;

import java.awt.*;

import javax.swing.*;

import org.cybergarage.util.*;
import org.cybergarage.upnp.proxy.*;
import org.cybergarage.upnp.ssdp.*;

public class ProxyFrame
{
	private final static String TITLE = "CyberLink Sample Control Point";
	public static int DEFAULT_WIDTH = 640;
	public final static int DEFAULT_HEIGHT = 480;
	
	private Proxy upnpProxy;
	
	private JFrame frame;
	private ProxyPane mainPane;
	private ProxyMenu menuBar;
	
	public ProxyFrame()
	{
		initProxy();
		initFrame();
	}

	////////////////////////////////////////////////
	//	Frame
	////////////////////////////////////////////////

	private void initFrame()
	{
		frame = new JFrame(TITLE);

		frame.getContentPane().setLayout(new BorderLayout());

		menuBar = new ProxyMenu(frame, upnpProxy);
		frame.setJMenuBar(menuBar);

		mainPane = new ProxyPane();		
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

	/*
	public void printConsole(String msg)
	{
		ctrlPointPane.printConsole(msg); 
	}

	public void clearConsole()
	{
		ctrlPointPane.clearConsole();
	}

	public void updateTreeComp()
	{
		TreeComp upnpTree = ctrlPointPane.getTreeComp();
		upnpTree.update(this);
	}
	*/
	
	////////////////////////////////////////////////
	//	Proxy
	////////////////////////////////////////////////
	
	private void initProxy()
	{
		upnpProxy = new Proxy();
		upnpProxy.start();
	}
	
	////////////////////////////////////////////////
	//	Listener
	////////////////////////////////////////////////
	
	public void deviceNotifyReceived(SSDPPacket packet)
	{
		System.out.println(packet.toString());
		
		if (packet.isDiscover() == true) {
			String st = packet.getST();
			//printConsole("ssdp:discover : ST = " + st); 
		}
		else if (packet.isAlive() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
			String url = packet.getLocation();
			//printConsole("ssdp:alive : uuid = " + usn + ", NT = " + nt + ", location = " + url); 
		}
		else if (packet.isByeBye() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
			//printConsole("ssdp:byebye : uuid = " + usn + ", NT = " + nt); 
		}
		//updateTreeComp();
	}
	
	public void deviceSearchResponseReceived(SSDPPacket packet)
	{
		String uuid = packet.getUSN();
		String st = packet.getST();
		String url = packet.getLocation();
		//printConsole("device search res : uuid = " + uuid + ", ST = " + st + ", location = " + url); 
		//updateTreeComp();
	}
	
	public void eventNotifyReceived(String uuid, long seq, String name, String value)
	{
		//printConsole("event notify : uuid = " + uuid + ", seq = " + seq + ", name = " + name + ", value =" + value); 
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
		
		ProxyFrame proxyFrame = new ProxyFrame();
		//proxyFrame.start();
	}

}
