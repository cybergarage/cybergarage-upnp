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

public class TvFrame extends JFrame implements Runnable, WindowListener 
{
	private final static String TITLE = "CyberLink Sample TV";
	
	private TvDevice tvDev;
	private TvPane tvPane;
	
	public TvFrame()
	{
		super(TITLE);

		tvDev = new TvDevice();

		getContentPane().setLayout(new BorderLayout());

		tvPane = new TvPane();
		tvDev.setComponent(tvPane);
		tvPane.setDevice(tvDev);
		getContentPane().add(tvPane, BorderLayout.CENTER);

		addWindowListener(this);
		
		pack();
		setVisible(true);
	}

	public TvPane getTvPanel()
	{
		return tvPane;
	}

	public TvDevice getTvDevice()
	{
		return tvDev;
	}
		
	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread timerThread = null;
		
	public void run()
	{
		Thread thisThread = Thread.currentThread();

		while (timerThread == thisThread) {
			tvDev.setMessage("");
			tvPane.repaint();
			try {
				Thread.sleep(1000*5);
			}
			catch(InterruptedException e) {}
		}
	}
	
	public void start()
	{
		tvDev.start();
		
		timerThread = new Thread(this);
		timerThread.start();
	}
	
	public void stop()
	{
		tvDev.stop();
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
		tvDev.off();
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

	public static void main(String args[]) 
	{
		//Debug.on();

		TvFrame sampTv = new TvFrame();
		sampTv.start();
	}

}

