/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ProxyPane.java
*
*	Revision:
*
*	08/18/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Icon;

public class ProxyPane extends JPanel  implements MouseListener
{
	public ProxyPane()
	{
		setLayout(new BorderLayout());
		
		// Console
		consoleArea = new JTextArea();
		consoleScrPane = new JScrollPane(consoleArea);
		add(consoleArea, BorderLayout.CENTER);
	}

	////////////////////////////////////////////////
	//	Console
	////////////////////////////////////////////////

	private JTextArea consoleArea;
	private JScrollPane consoleScrPane;
	
	public JTextArea getConsoleArea() {
		return consoleArea;
	}
	
	public void printConsole(String str)
	{
		consoleArea.append(str + "\n");
		JScrollBar scrBar = consoleScrPane.getVerticalScrollBar();
		int maxPos = scrBar.getMaximum();
		scrBar.setValue(maxPos);
	}

	public void clearConsole()
	{
		consoleArea.setText("");
	}
	
	////////////////////////////////////////////////
	//	mouse
	////////////////////////////////////////////////

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
 	}
 	
	public void mouseReleased(MouseEvent e)
	{
	}
}

