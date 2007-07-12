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
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;
import javax.imageio.ImageIO;

import org.cybergarage.util.*;

public class ClockPane extends JPanel  // MouseListener
{
	public ClockPane()
	{
		loadImage();
		initPanel();
	}

	////////////////////////////////////////////////
	//	Background
	////////////////////////////////////////////////

	private final static String CLOCK_PANEL_IMAGE = "images/clock.jpg";

	private BufferedImage panelmage;
	
	private void loadImage()
	{
		File f = new File(CLOCK_PANEL_IMAGE);
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
	//	Font
	////////////////////////////////////////////////

	private final static String DEFAULT_FONT_NAME = "Lucida Console";
	private final static int DEFAULT_TIME_FONT_SIZE = 60;
	private final static int DEFAULT_DATE_FONT_SIZE = 18;
	private final static int DEFAULT_SECOND_BLOCK_HEIGHT = 8;
	private final static int DEFAULT_SECOND_BLOCK_FONT_SIZE = 10;

	private Font timeFont = null;
	private Font dateFont = null;
	private Font secondFont = null;

	private Font getFont(Graphics g, int size)
	{
		Font font = new Font(DEFAULT_FONT_NAME, Font.PLAIN, size);
		if (font != null)
			return font;
		return g.getFont();
	}
		
	private Font getTimeFont(Graphics g)
	{
		if (timeFont == null)
			timeFont = getFont(g, DEFAULT_TIME_FONT_SIZE);
		return timeFont;
	}

	private Font getDateFont(Graphics g)
	{
		if (dateFont == null)
			dateFont = getFont(g, DEFAULT_DATE_FONT_SIZE);
		return dateFont;
	}

	private Font getSecondFont(Graphics g)
	{
		if (secondFont == null)
			secondFont = getFont(g, DEFAULT_SECOND_BLOCK_FONT_SIZE);
		return secondFont;
	}

	////////////////////////////////////////////////
	//	paint
	////////////////////////////////////////////////

	private void drawClockInfo(Graphics g)
	{
		Clock clock = Clock.getInstance();
		
		int winWidth = getWidth();
		int winHeight = getHeight();
		
		g.setColor(Color.BLACK);
		
		//// Time String ////
		
		String timeStr = clock.getTimeString();

		Font timeFont = getTimeFont(g);
		g.setFont(timeFont);

		FontMetrics timeFontMetric = g.getFontMetrics();
		Rectangle2D timeStrBounds = timeFontMetric.getStringBounds(timeStr, g);

		int timeStrWidth = (int)timeStrBounds.getWidth();		
		int timeStrHeight = (int)timeStrBounds.getHeight();
		int timeStrX = (winWidth-timeStrWidth)/2;
		int timeStrY = (winHeight+timeStrHeight)/2;
		int timeStrOffset = timeStrHeight/8/2;
		g.drawString(
			timeStr,
			timeStrX,
			timeStrY);

		//// Date String ////

		String dateStr = clock.getDateString();

		Font dateFont = getDateFont(g);
		g.setFont(dateFont);

		FontMetrics dateFontMetric = g.getFontMetrics();
		Rectangle2D dateStrBounds = dateFontMetric.getStringBounds(dateStr, g);

		g.drawString(
			dateStr,
			(winWidth-(int)dateStrBounds.getWidth())/2,
			timeStrY-timeStrHeight-timeStrOffset);

		//// Second Bar ////
		
		Font secFont = getSecondFont(g);
		g.setFont(secFont);
		int sec = clock.getSecond();
		int secBarBlockSize = timeStrWidth / 60;
		int secBarBlockY = timeStrY + timeStrOffset;
		for (int n=0; n<sec; n++) {
			int x = timeStrX + (secBarBlockSize*n);
			g.fillRect(
				x,
				secBarBlockY,
				secBarBlockSize-1,
				DEFAULT_SECOND_BLOCK_HEIGHT);
		}
		if (sec != 0 && (sec % 10) == 0) {
			int x = timeStrX + (secBarBlockSize*sec);
			g.drawString(
				Integer.toString(sec),
				x + secBarBlockSize,
				secBarBlockY + DEFAULT_SECOND_BLOCK_HEIGHT);
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
		drawClockInfo(g);
	}
}

