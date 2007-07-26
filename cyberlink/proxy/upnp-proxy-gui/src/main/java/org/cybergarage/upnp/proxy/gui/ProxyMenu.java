/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ProxyMenu.java
*
*	Revision:
*
*	08/18/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy.gui;

import java.awt.event.*;
import javax.swing.*;
import org.cybergarage.upnp.proxy.*;

public class ProxyMenu extends JMenuBar implements ActionListener
{
	JFrame parentFrame;
	
	JMenu fileMenu;

	JMenuItem connectItem;
	JMenuItem quitItem;
	
	Proxy upnpProxy;
	
	public ProxyMenu(JFrame frame, Proxy proxy)
	{
		parentFrame = frame;
		upnpProxy = proxy;
		
		fileMenu = new JMenu("File");
		add(fileMenu);
		
		connectItem = new JMenuItem("Connect");
		connectItem.addActionListener(this);
		fileMenu.add(connectItem);
		
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		fileMenu.add(quitItem);
	}

	////////////////////////////////////////////////
	//	actionPerformed
	////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == connectItem) {
			ServerConnectionDlg dlg = new ServerConnectionDlg(parentFrame);
			if (dlg.doModal()) {
				upnpProxy.addServer(dlg.getHost(), dlg.getPort(), dlg.getPassword());
			}
		}
		if (e.getSource() == quitItem) {
			System.exit(0);
		}
	}
}
