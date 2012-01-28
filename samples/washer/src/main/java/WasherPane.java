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

public class WasherPane extends JPanel  // MouseListener
{
	public WasherPane()
	{
		loadImage();
		initPanel();
	}

	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private final static String WASHER_PANEL_IMAGE = "images/washer.jpg";
	private final static String WASHER_ANIM_IMAGE1 = "images/washeron1.jpg";
	private final static String WASHER_ANIM_IMAGE2 = "images/washeron2.jpg";

	private BufferedImage panelmage;
	private BufferedImage animlmage;
	private BufferedImage anim1lmage;
	private BufferedImage anim2lmage;
	
	private void loadImage()
	{
		try {
			panelmage = ImageIO.read(WasherPane.class.getResourceAsStream(WASHER_PANEL_IMAGE));
			anim1lmage = ImageIO.read(WasherPane.class.getResourceAsStream(WASHER_ANIM_IMAGE1));
			anim2lmage = ImageIO.read(WasherPane.class.getResourceAsStream(WASHER_ANIM_IMAGE2));
		}
		catch (Exception e) {
			Debug.warning(e);
		}
	}

	private BufferedImage getPaneImage()
	{
		return panelmage;
	}

	public void flipAnimationImage()
	{
		if (animlmage == anim1lmage)
			animlmage = anim2lmage;
		else
			animlmage = anim1lmage;
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

	private WasherDevice washerDev = null;
	
	public void setDevice(WasherDevice dev)
	{
		washerDev = dev;
	}

	public WasherDevice getDevice()
	{
		return washerDev;
	}

	////////////////////////////////////////////////
	//	paint
	////////////////////////////////////////////////

	private void drawInfo(Graphics g)
	{
		WasherDevice dev = getDevice();
		if (dev.isWashing() == true) {
			g.drawImage(animlmage, 0, 0, null);
			g.setColor(Color.YELLOW);
		}
		else {
			g.drawImage(panelmage, 0, 0, null);
			g.setColor(Color.LIGHT_GRAY);
		}
		g.fillRect(5, 20, 20, 5);
	}

	private void clear(Graphics g)
	{
		g.setColor(Color.GRAY);
		g.clearRect(0, 0, getWidth(), getHeight());
	}
	

	public void paint(Graphics g)
	{
		clear(g);
		drawInfo(g);
	}
}

