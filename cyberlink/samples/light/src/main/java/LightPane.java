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

public class LightPane extends JPanel
{
	////////////////////////////////////////////////
	//	Images
	////////////////////////////////////////////////

	private final static String LIGHT_ON_PANEL_IMAGE = "images/lighton.jpg";
	private final static String LIGHT_OFF_PANEL_IMAGE = "images/lightoff.jpg";
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public LightPane()
	{
		loadImage(LIGHT_OFF_PANEL_IMAGE);
		initPanel();
	}

	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private BufferedImage panelmage;
	
	private void loadImage(String finename)
	{
		File f = new File(finename);
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

	private LightDevice lightDev = null;
	
	public void setDevice(LightDevice dev)
	{
		lightDev = dev;
	}

	public LightDevice getDevice()
	{
		return lightDev;
	}

	////////////////////////////////////////////////
	//	paint
	////////////////////////////////////////////////

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
		LightDevice dev = getDevice();
		if (dev.isOn() == true)
			loadImage(LIGHT_ON_PANEL_IMAGE);
		else
			loadImage(LIGHT_OFF_PANEL_IMAGE);
		
		clear(g);
		drawPanelImage(g);
	}
}

