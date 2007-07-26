/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : RemoteUIClockServer.java
*
******************************************************************/

import org.cybergarage.util.*;
import org.cybergarage.upnp.rui.xrt.*;
import org.cybergarage.upnp.rui.client.xrt.*;

import java.awt.*;
import java.awt.event.*;

public class RemoteUIPlayer extends Frame implements WindowListener, MouseListener, XRTCommandListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	private final static String TITLE = "CyberGarage RemoteUI Client (XRT2)";
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public RemoteUIPlayer()
	{
		ruiClientDev = new RemoteUIClientXRT();
		
		setTitle(TITLE);
		setSize(ruiClientDev.getDisplayWidth(), ruiClientDev.getDisplayHeight());
		addWindowListener(this);
		addMouseListener(this);
		
		getClientService().setListener(this);
	}

	////////////////////////////////////////////////
	// Device
	////////////////////////////////////////////////
	
	private RemoteUIClientXRT ruiClientDev;
	
	public RemoteUIClientXRT getClient()
	{
		return ruiClientDev;
	}
	
	public RemoteUIClientServiceXRT getClientService()
	{
		return (RemoteUIClientServiceXRT)ruiClientDev.getService();
	}
	
	////////////////////////////////////////////////
	// paint
	////////////////////////////////////////////////
	
	public void paint(Graphics g) 
	{
        //g.drawString("Hello World!!", 20, 50);
    }
	
	////////////////////////////////////////////////
	//	Mouse action listners
	////////////////////////////////////////////////
	
	public void mouseEntered(MouseEvent e)
	{
	}
	
	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}
	
	public void mousePressed(MouseEvent e)
	{
		RemoteUIClientServiceXRT service = getClientService();
		
		byte mouseData[] = new byte[12];
		
		XRTCommand.toByte(e.getX(), mouseData, 0);
		XRTCommand.toByte(e.getY(), mouseData, 4);
		XRTCommand.toByte(1, mouseData, 8);
		
		service.postCommand(XRTCommand.MOUSE_DOWN, mouseData);
	}
	
	public void mouseReleased(MouseEvent e)
	{
		RemoteUIClientServiceXRT service = getClientService();
		
		byte mouseData[] = new byte[12];
		
		XRTCommand.toByte(e.getX(), mouseData, 0);
		XRTCommand.toByte(e.getY(), mouseData, 4);
		XRTCommand.toByte(1, mouseData, 8);
		
		service.postCommand(XRTCommand.MOUSE_UP, mouseData);
	}
	
	////////////////////////////////////////////////
	//	Window action listners
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
	// start
	////////////////////////////////////////////////

	public boolean commandReceived(XRTCommand xrtCmd)
	{
		xrtCmd.dump();
		switch (xrtCmd.getCode()) {
		case XRTCommand.DRAWFILLBOX:
			{
				Debug.message("DRAWFILLBOX = " + 
						xrtCmd.getX() + "," +
						xrtCmd.getY() + "," +
						xrtCmd.getWidth() + "," +
						xrtCmd.getHeight() + "," +
						xrtCmd.getRed() + "," +
						xrtCmd.getGreen() + "," +
						xrtCmd.getBlue());
				
				Graphics g = getGraphics();
				g.setColor(new Color(xrtCmd.getRed(), xrtCmd.getGreen(), xrtCmd.getBlue()));
				g.fillRect(xrtCmd.getX(), xrtCmd.getY(), xrtCmd.getWidth(), xrtCmd.getHeight());
			}
			break;
		}
		return true;
	}
	
	////////////////////////////////////////////////
	// start
	////////////////////////////////////////////////
	
	public void start()
	{
		getClient().start();
	}
	
	public void stop()
	{
		getClient().stop();
	}
	
	////////////////////////////////////////////////
	// main
	////////////////////////////////////////////////
	
	public static void main(String args[]) 
	{
		Debug.on();

		RemoteUIPlayer ruiPlayer = new RemoteUIPlayer();
		ruiPlayer.show();
		ruiPlayer.start();
	}

}
