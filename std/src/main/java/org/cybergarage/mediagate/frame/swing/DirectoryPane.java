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

package org.cybergarage.mediagate.frame.swing;

import javax.swing.*;

import org.cybergarage.upnp.std.av.server.*;
import org.cybergarage.mediagate.*;

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
	
	public DirectoryPane(MediaGate mgate)
	{
		dirListMode = new DefaultListModel();
		dirList = new JList(dirListMode);
		
		scrPane = new JScrollPane();
		scrPane.getViewport().setView(dirList);
		
		add(scrPane);
		
		update(mgate);
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

	public void update(MediaGate mgate)
	{
		MediaServer mserver = mgate.getMediaServer();
		int nDirectories = mserver.getNContentDirectories();
		dirListMode.clear();
		for (int n=0; n<nDirectories; n++) {		
			Directory dir = mserver.getContentDirectory(n);
			dirListMode.addElement(dir.getFriendlyName());
		}
		dirList.revalidate();
	}
}
