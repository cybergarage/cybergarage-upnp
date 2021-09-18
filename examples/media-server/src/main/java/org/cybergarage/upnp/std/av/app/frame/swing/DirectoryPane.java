/******************************************************************
*
*	MediaGate for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: DirectoryPane.java
*
*	Revision;
*
*	01/28/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.upnp.std.av.app.frame.swing;

import javax.swing.*;

import org.cybergarage.upnp.std.av.server.Directory;
import org.cybergarage.upnp.std.av.app.*;

public class DirectoryPane extends JPanel
{
	////////////////////////////////////////////////
	// Member
	////////////////////////////////////////////////
	
	private JScrollPane scrPane;
	private JList dirList;
	private DefaultListModel dirListMode;
	
	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	public DirectoryPane(MediaServer server)
	{
		dirListMode = new DefaultListModel();
		dirList = new JList(dirListMode);
		
		scrPane = new JScrollPane();
		scrPane.getViewport().setView(dirList);
		
		add(scrPane);
		
		update(server);
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public JList getList()
	{
		return dirList;
	}

	public JScrollPane getScrollPane()
	{
		return scrPane;
	}
	
	////////////////////////////////////////////////
	// exit
	////////////////////////////////////////////////

	public void update(MediaServer server)
	{
		int nDirectories = server.getNContentDirectories();
		dirListMode.clear();
		for (int n=0; n<nDirectories; n++) {		
			Directory dir = server.getContentDirectory(n);
			dirListMode.addElement(dir.getFriendlyName());
		}
		dirList.revalidate();
	}
}
