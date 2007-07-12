/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : ActionDialog.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.Action;

public class UpnpIGDToolAddPortDlg extends JDialog implements ActionListener
{
	private Device igdDevice;
	private JButton okButton;
	private JButton cancelButton;
	private boolean result;
	private ArgumentList inArgList;
	private Vector inArgFieldList;
	
	private JTextField nameLabel;
	private JComboBox protoLabel;
	private JTextField wanPortLabel;
	private JTextField lanIpLabel;
	private JTextField lanPortLabel;

	public UpnpIGDToolAddPortDlg(Frame frame, Device dev)
	{
		super(frame, true);
		getContentPane().setLayout(new BorderLayout());
		
		inArgList = new ArgumentList();
		inArgFieldList = new Vector();
		
		igdDevice = dev;
		
		JPanel argListPane = new JPanel();
		argListPane.setLayout(new GridLayout(0, 2));
		getContentPane().add(argListPane, BorderLayout.CENTER);

		JLabel staticLabel;
		
		staticLabel = new JLabel("Name");
		nameLabel = new JTextField();
		argListPane.add(staticLabel);
		argListPane.add(nameLabel);
		
		staticLabel = new JLabel("Protocol");
		protoLabel = new JComboBox();
		protoLabel.addItem("TCP");
		protoLabel.addItem("UDP");
		argListPane.add(staticLabel);
		argListPane.add(protoLabel);
		
		staticLabel = new JLabel("WAN port");
		wanPortLabel = new JTextField();
		argListPane.add(staticLabel);
		argListPane.add(wanPortLabel);
		
		staticLabel = new JLabel("LAN IP");
		lanIpLabel = new JTextField();
		argListPane.add(staticLabel);
		argListPane.add(lanIpLabel);
		
		staticLabel = new JLabel("LAN port");
		lanPortLabel = new JTextField();
		argListPane.add(staticLabel);
		argListPane.add(lanPortLabel);
		
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

	public String getName()
	{
		return nameLabel.getText();
	}
	
	public String getProtocol()
	{
		return (String)protoLabel.getSelectedItem();
	}
	
	public int getExternalPort()
	{
		try {
			String wanPortStr = wanPortLabel.getText(); 
			return Integer.parseInt(wanPortStr);
		}
		catch (Exception e) {
		}
		return 0;
	}
	
	public String getInternalIP()
	{
		return lanIpLabel.getText();
	}
	
	public int getInternalPort()
	{
		try {
			String lanPortStr = wanPortLabel.getText(); 
			return Integer.parseInt(lanPortStr);
		}
		catch (Exception e) {
		}
		return 0;
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
