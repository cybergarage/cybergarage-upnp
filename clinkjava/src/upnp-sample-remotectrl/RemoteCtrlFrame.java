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

public class RemoteCtrlFrame extends JFrame implements Runnable, WindowListener
{
	private final static String TITLE = "CyberLink Sample Remote Control";
	
	private RemoteCtrl remoteCtrl;
	private RemoteCtrlPane remoteCtrlPane;
	
	public RemoteCtrlFrame()
	{
		super(TITLE);

		remoteCtrl = new RemoteCtrl();

		getContentPane().setLayout(new BorderLayout());

		remoteCtrlPane = new RemoteCtrlPane();		
		remoteCtrlPane.setDevice(remoteCtrl);
		getContentPane().add(remoteCtrlPane, BorderLayout.CENTER);

		addWindowListener(this);
		
		pack();
		setVisible(true);
	}

	public RemoteCtrlPane getTvPanel()
	{
		return remoteCtrlPane;
	}

	public RemoteCtrl getTvDevice()
	{
		return remoteCtrl;
	}
		
	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread timerThread = null;
		
	public void run()
	{
		Thread thisThread = Thread.currentThread();

		while (timerThread == thisThread) {
			getTvPanel().repaint();
			try {
				Thread.sleep(1000);
			}
			catch(InterruptedException e) {}
		}
	}
	
	public void start()
	{
		remoteCtrl.start();
		
		timerThread = new Thread(this);
		timerThread.start();
	}
	
	public void stop()
	{
		remoteCtrl.stop();
		timerThread = null;
	}

	////////////////////////////////////////////////
	//	window
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

	public static void main(String args[]) 
	{
		//Debug.on();

		RemoteCtrlFrame sampTv = new RemoteCtrlFrame();
		sampTv.start();
	}

}

