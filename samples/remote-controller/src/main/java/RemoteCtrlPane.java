/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : SampleClockPane.java
*
******************************************************************/

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;

import org.cybergarage.util.*;

public class RemoteCtrlPane extends JPanel implements MouseListener
{
	private final static int IMAGE_BORDER_SIZE = 20;
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public RemoteCtrlPane()
	{
		loadImage();
		initPanel();
		addMouseListener(this);
	}

	////////////////////////////////////////////////
	//	TvDevice
	////////////////////////////////////////////////

	private RemoteCtrl tvDev = null;
	
	public void setDevice(RemoteCtrl dev)
	{
		tvDev = dev;
	}

	public RemoteCtrl getDevice()
	{
		return tvDev;
	}
	
	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private final static String CLOCK_PANEL_IMAGE = "images/remotectrl.jpg";

	private BufferedImage panelmage;
	
	private void loadImage()
	{
		try {
			panelmage = ImageIO.read(RemoteCtrlPane.class.getResourceAsStream(CLOCK_PANEL_IMAGE));
		}
		catch (Exception e) {
			Debug.warning(e);
		}
	}

	private BufferedImage getPaneImage()
	{
		return panelmage;
	}

	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private void initPanel()
	{
		BufferedImage panelmage = getPaneImage();
		setPreferredSize(new Dimension(panelmage.getWidth(), panelmage.getHeight()));
	}

	////////////////////////////////////////////////
	//	Font
	////////////////////////////////////////////////

	private final static String DEFAULT_FONT_NAME = "Lucida Console";
	private final static int DEFAULT_TIME_FONT_SIZE = 14;

	private Font timeFont = null;

	private Font getFont(Graphics g, int size)
	{
		Font font = new Font(DEFAULT_FONT_NAME, Font.BOLD, size);
		if (font != null)
			return font;
		return g.getFont();
	}
		
	private Font getFont(Graphics g)
	{
		if (timeFont == null)
			timeFont = getFont(g, DEFAULT_TIME_FONT_SIZE);
		return timeFont;
	}

	////////////////////////////////////////////////
	//	panel
	////////////////////////////////////////////////
	
	private String panelMessage = "";

	private void display(String msg)
	{
		panelMessage = msg;
		update(getGraphics());
		try {
			Thread.sleep(500);
		}
		catch (Exception e) {};
		
		panelMessage = "";
		repaint();
	}
	
	////////////////////////////////////////////////
	//	mouse
	////////////////////////////////////////////////

	private Rectangle tvPowerRect = new Rectangle(20,80,50,50);

	private Rectangle lightPowerRect = new Rectangle(130,80,50,50);

	private Rectangle airconPowerRect = new Rectangle(20,170,50,50);
	private Rectangle airconUpRect = new Rectangle(100,170,40,50);
	private Rectangle airconDownRect = new Rectangle(140,170,40,50);
	
	private Rectangle washerStartRect = new Rectangle(20,250,50,50);
	private Rectangle washerStopRect = new Rectangle(100,250,50,50);
	
	public void mouseClicked(MouseEvent e) 
	{
	}

	public void mouseEntered(MouseEvent e) 
	{
	}

	public void mouseExited(MouseEvent e) 
	{
	}

	public void mousePressed(MouseEvent e) 
	{
		RemoteCtrl dev = getDevice();
		int x = e.getX();
		int y = e.getY();
		if (tvPowerRect.contains(x, y) == true) {
			display("TV POWER");
			dev.tvPowerOn();
		}
		if (lightPowerRect.contains(x, y) == true){
			display("LIGHT POWER");
			dev.lightPowerOn();
		}
		if (airconPowerRect.contains(x, y) == true){
			display("AIR CONDITIONER POWER");
			dev.airconPowerOn();
		}
		if (airconUpRect.contains(x, y) == true){
			display("AIR CONDITIONER UP");
			dev.airconTempUp();
		}
		if (airconDownRect.contains(x, y) == true){
			display("AIR CONDITIONER DOWN");
			dev.airconTempDown();
		}
		if (washerStartRect.contains(x, y) == true){
			display("WASHER START");
			dev.washerStart();
		}
		if (washerStopRect.contains(x, y) == true){
			display("WASHER STOP");
			dev.washerStop();
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
	}

	public void mouseDragged(MouseEvent e) 
	{
	}

	public void mouseMoved(MouseEvent e) 
	{
	}

	////////////////////////////////////////////////
	//	paint
	////////////////////////////////////////////////

	private void drawPanelMessage(Graphics g)
	{
		g.setColor(Color.WHITE);
		
		g.setFont(getFont(g));

		g.drawString(
		panelMessage,
			20,
			35);
	}

	private void clear(Graphics g)
	{
		g.setColor(Color.GRAY);
		g.clearRect(0, 0, getWidth(), getHeight());
	}
	

	private void drawPanelImage(Graphics g)
	{
		g.drawImage(getPaneImage(), 0, 0, null);
	}
		
	public void paint(Graphics g)
	{
		clear(g);
		drawPanelImage(g);
		drawPanelMessage(g);
	}
}

