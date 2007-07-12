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

import javax.swing.*;
import javax.imageio.ImageIO;

import org.cybergarage.util.*;

public class AirconPane extends JPanel  // MouseListener
{
	public AirconPane()
	{
		loadImage();
		initPanel();
	}

	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private final static String AIRCON_PANEL_IMAGE = "images/aircon.jpg";

	private BufferedImage panelmage;
	
	private void loadImage()
	{
		File f = new File(AIRCON_PANEL_IMAGE);
		try {
			panelmage = ImageIO.read(f);
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
	//	LightDevice
	////////////////////////////////////////////////

	private AirconDevice airconDev = null;
	
	public void setDevice(AirconDevice dev)
	{
		airconDev = dev;
	}

	public AirconDevice getDevice()
	{
		return airconDev;
	}

	////////////////////////////////////////////////
	//	Font
	////////////////////////////////////////////////

	private final static String DEFAULT_FONT_NAME = "Lucida Console";
	private final static int DEFAULT_TIME_FONT_SIZE = 8;

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
	//	paint
	////////////////////////////////////////////////

	private void drawInfo(Graphics g)
	{
		AirconDevice dev = getDevice();
		
		g.setFont(getFont(g));

		//////////////////////////////////////
		//	power
		//////////////////////////////////////
		
		if (dev.isOn() == true)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.LIGHT_GRAY);
	
		g.fillRect(5, 50, 20, 5);

		//////////////////////////////////////
		//	tempture
		//////////////////////////////////////
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(250, 50, 40, 10);
		
		if (dev.isOn() == true){
			g.setColor(Color.BLACK);
			g.drawString(
				dev.getTempture(),
				280,
				59);
		}
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
		drawInfo(g);
	}
}

