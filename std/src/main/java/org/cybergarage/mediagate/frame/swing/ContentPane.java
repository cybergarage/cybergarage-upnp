/******************************************************************
*
*	MediaGate for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: ContentPane.java
*
*	Revision;
*
*	01/28/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.mediagate.frame.swing;

import javax.swing.*;
import java.awt.event.*;

import org.cybergarage.upnp.std.av.server.*;

public class ContentPane extends JPanel
{
	////////////////////////////////////////////////
	// Member
	////////////////////////////////////////////////
	
	private JScrollPane scrPane;
	private JTable conTable;
	private ContentTable conTableModel;
	
	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	public ContentPane()
	{
		conTableModel = new ContentTable();
		conTable = new JTable(conTableModel);
		
		scrPane = new JScrollPane();
		scrPane.getViewport().setView(conTable);
		
		add(scrPane);
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e)
	{
	}
	
	////////////////////////////////////////////////
	// exit
	////////////////////////////////////////////////

	public void update(Directory dir)
	{
		conTableModel.update(dir);
		conTable.revalidate();
	}
}
