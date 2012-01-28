/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: ServerConnectionDlg.java
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
import java.util.*;
import javax.swing.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.Action;

public class SettingDialog extends JDialog implements ActionListener
{
	private JButton okButton;
	private JButton cancelButton;
	private boolean result;
	
	private JTextField portLabel;
	private JTextField passwdLabel;

	public SettingDialog(Frame frame)
	{
		super(frame, true);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel argListPane = new JPanel();
		argListPane.setLayout(new GridLayout(0, 2));
		getContentPane().add(argListPane, BorderLayout.CENTER);

		JLabel staticLabel;
		
		staticLabel = new JLabel("Port");
		portLabel = new JTextField();
		argListPane.add(staticLabel);
		argListPane.add(portLabel);
		
		staticLabel = new JLabel("Password");
		passwdLabel = new JPasswordField();
		argListPane.add(staticLabel);
		argListPane.add(passwdLabel);
		
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		JPanel buttonPane = new JPanel();
		buttonPane.add(okButton);
		buttonPane.add(cancelButton);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		pack();
		
		Dimension size = getSize();
	    Point fpos = frame.getLocationOnScreen();
	    Dimension fsize = frame.getSize();
	    setLocation(fpos.x + (fsize.width - size.width)/2, fpos.y +(fsize.height - size.height)/2);
	}
	
	////////////////////////////////////////////////
	//	Arguments
	////////////////////////////////////////////////

	public int getPort()
	{
		try {
			String wanPortStr = portLabel.getText(); 
			return Integer.parseInt(wanPortStr);
		}
		catch (Exception e) {
		}
		return 0;
	}
	
	public String getPassword()
	{
		return passwdLabel.getText();
	}
	
	////////////////////////////////////////////////
	//	actionPerformed
	////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == okButton) {
			result = true;
		}
		if (e.getSource() == cancelButton) {
			result = false;
		}
		dispose();
	}

	////////////////////////////////////////////////
	//	actionPerformed
	////////////////////////////////////////////////
	
	public boolean doModal()
	{
		show();
		return result;
	}
}
