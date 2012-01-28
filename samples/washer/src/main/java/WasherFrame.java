/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002-2003
*
*	File : SampleClock.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.cybergarage.util.*;

import org.cybergarage.upnp.device.*;

public class WasherFrame extends JFrame implements WindowListener 
{
	private final static String TITLE = "CyberLink Sample Clock";
	private WasherDevice washerDev;
	private WasherPane washerPane;
	
	public WasherFrame()
	{
		super(TITLE);

		try {
			washerDev = new WasherDevice();
		}
		catch (InvalidDescriptionException e) {
			Debug.warning(e);
		}
				
		getContentPane().setLayout(new BorderLayout());

		washerPane = new WasherPane();
		washerPane.setDevice(washerDev);
		washerDev.setPanel(washerPane);
		getContentPane().add(washerPane, BorderLayout.CENTER);

		addWindowListener(this);
		
		pack();
		setVisible(true);
		
		washerDev.start();
	}

	public WasherPane getPanel()
	{
		return washerPane;
	}

	public WasherDevice getDevice()
	{
		return washerDev;
	}
		
	////////////////////////////////////////////////
	//	main
	////////////////////////////////////////////////
	
	public void windowActivated(WindowEvent e) 
	{
	}
	
	public void windowClosed(WindowEvent e) 
	{
	}
	
	public void windowClosing(WindowEvent e) 
	{
		washerDev.stop();
		System.exit(0);
	}
	
	public void windowDeactivated(WindowEvent e) 
	{
	}
	
	public void windowDeiconified(WindowEvent e) 
	{
	}
	
	public void windowIconified(WindowEvent e) 
	{
	}
	
	public void windowOpened(WindowEvent e)
	{
	}
	

	////////////////////////////////////////////////
	//	main
	////////////////////////////////////////////////

	public static void main(String args[]) 
	{
		//Debug.on();
		WasherFrame wahser = new WasherFrame();
	}

}

