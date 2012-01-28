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

public class AirconFrame extends JFrame implements WindowListener 
{
	private final static String TITLE = "CyberLink Sample Airconditoner";
	
	private AirconDevice airconDev;
	private AirconPane airconPane;
	
	public AirconFrame()
	{
		super(TITLE);

		try {
			airconDev = new AirconDevice();
		}
		catch (InvalidDescriptionException e) {
			Debug.warning(e);
		}
				
		getContentPane().setLayout(new BorderLayout());

		airconPane = new AirconPane();		
		airconPane.setDevice(airconDev);
		airconDev.setComponent(airconPane);
		getContentPane().add(airconPane, BorderLayout.CENTER);

		addWindowListener(this);
		
		pack();
		setVisible(true);
		
		airconDev.start();
	}

	public AirconPane getClockPanel()
	{
		return airconPane;
	}

	public AirconDevice getClockDevice()
	{
		return airconDev;
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
		airconDev.stop();
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

		AirconFrame sampClock = new AirconFrame();
	}

}

