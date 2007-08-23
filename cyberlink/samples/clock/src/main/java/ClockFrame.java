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
import java.io.IOException;

import javax.swing.*;

import org.cybergarage.util.*;

import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.*;

public class ClockFrame extends JFrame implements Runnable, WindowListener 
{
	private final static String TITLE = "CyberLink Sample Clock";
	private ClockDevice clockDev;
	private ClockPane clockPane;
	
	public ClockFrame() throws IOException
	{
		super(TITLE);
		try {
			clockDev = new ClockDevice();
		}
		catch (InvalidDescriptionException e) {
			Debug.warning(e);
		}
				
		getContentPane().setLayout(new BorderLayout());

		clockPane = new ClockPane();		
		getContentPane().add(clockPane, BorderLayout.CENTER);

		addWindowListener(this);
		
		pack();
		setVisible(true);
	}

	public ClockPane getClockPanel()
	{
		return clockPane;
	}

	public ClockDevice getClockDevice()
	{
		return clockDev;
	}
		
	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread timerThread = null;
		
	public void run()
	{
		Thread thisThread = Thread.currentThread();

		while (timerThread == thisThread) {
			getClockDevice().update();
			getClockPanel().repaint();
			try {
				Thread.sleep(1000);
			}
			catch(InterruptedException e) {}
		}
	}
	
	public void start()
	{
		clockDev.start();
		
		timerThread = new Thread(this);
		timerThread.start();
	}
	
	public void stop()
	{
		clockDev.stop();
		timerThread = null;
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
		stop();
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

	public static void main(String args[]) throws Throwable 
	{
		//Debug.on();

		ClockFrame sampClock = new ClockFrame();
		sampClock.start();
		
		ClockDevice clockDev = sampClock.getClockDevice();
	}

}

