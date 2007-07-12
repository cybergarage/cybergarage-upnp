/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : CtrlPointPane.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Icon;

public class UpnpIGDToolPane extends JPanel implements ActionListener
{
	private ControlPoint ctrlPoint;
	private JComboBox deviceCombo;
	private JButton infoButton;
	private JButton addButton;
	
	private JLabel	externalIPLabel;
	
	public UpnpIGDToolPane(ControlPoint ctrlPoint)
	{
		setLayout(new BorderLayout());
		
		this.ctrlPoint = ctrlPoint;
		
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
		add(mainSplitPane, BorderLayout.CENTER);

		JPanel topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));
		mainSplitPane.setTopComponent(topPane);
		
		deviceCombo = new JComboBox();
		deviceCombo.addActionListener(this);
		topPane.add(deviceCombo);
		
		// Info
		
		JPanel infoPane = new JPanel();
		infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.X_AXIS));
		topPane.add(infoPane);
		
		JLabel staticLabel;
		staticLabel = new JLabel("ExtenalIP : ");
		infoPane.add(staticLabel);
		externalIPLabel = new JLabel();
		infoPane.add(externalIPLabel);
		// Button
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		topPane.add(buttonPane);
		
		/*
		infoButton = new JButton("Info");
		buttonPane.add(infoButton);
		infoButton.addActionListener(this);
		*/
		
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		buttonPane.add(addButton);
		
		// Console
		consoleArea = new JTextArea();
		consoleScrPane = new JScrollPane(consoleArea);
		mainSplitPane.setBottomComponent(consoleScrPane);
	}

	////////////////////////////////////////////////
	//	Device
	////////////////////////////////////////////////
	
	public Frame getFrame()
	{
		return (Frame)getRootPane().getParent();
	}
	
	////////////////////////////////////////////////
	//	Device
	////////////////////////////////////////////////

	public String getDeviceComboName(Device dev)
	{
		return dev.getFriendlyName() + " / " + dev.getUDN();
	}
	
	public Device getDevice(String devComboName)
	{
		if (devComboName == null)
			return null;
		
		DeviceList devList = ctrlPoint.getDeviceList();
		int devCnt = devList.size();
		for (int n=0; n<devCnt; n++) {
			Device dev = devList.getDevice(n);
			if (devComboName.compareTo(getDeviceComboName(dev)) == 0)
				return dev;
		}
		return null;
	}
	
	////////////////////////////////////////////////
	//	Components
	////////////////////////////////////////////////
	
	public synchronized void updateDeviceComboList()
	{
		DeviceList devList = ctrlPoint.getDeviceList();
		int devCnt = devList.size();
		for (int n=0; n<devCnt; n++) {
			Device dev = devList.getDevice(n);
			if (dev.isDeviceType("urn:schemas-upnp-org:device:InternetGatewayDevice:1") == false)
				continue;
			String devComboName = getDeviceComboName(dev);
 			int itemCnt = deviceCombo.getItemCount();
 			boolean hasSameDeviceName = false;
			for (int i=0; i<itemCnt; i++) {
				String itemName = (String)deviceCombo.getItemAt(i);
				if (itemName == null)
					continue;
				if (itemName.compareTo(devComboName) == 0) {
					hasSameDeviceName = true;
					break;
				}
			}
			if (hasSameDeviceName == false) {
				deviceCombo.addItem(devComboName);
			}
		}
	}
	
	public synchronized Device getSelectedDevice()
	{
		return getDevice((String)deviceCombo.getSelectedItem());
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
	//	warrning
	////////////////////////////////////////////////

	public void showMessage(String message)
	{
		JOptionPane.showMessageDialog(getFrame(), message, "", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showWarnning(String message)
	{
		JOptionPane.showMessageDialog(getFrame(), message, "", JOptionPane.WARNING_MESSAGE);
	}
	
	////////////////////////////////////////////////
	//	actionPerformed
	////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e)
	{
		Device dev = getSelectedDevice();
		if (dev == null) {
			showWarnning("Selected Device is not found");
			return;
		}
		if (e.getSource() == addButton) {
			UpnpIGDToolAddPortDlg addPortDlg = new UpnpIGDToolAddPortDlg(getFrame(), dev);
			if (addPortDlg.doModal() == true)
				addPortMapping(addPortDlg);
		}
		else if (e.getSource() == deviceCombo) {
			externalIPLabel.setText(getExternalIPAddress());
		}
	}

	////////////////////////////////////////////////
	//	IGD actions
	////////////////////////////////////////////////
	
	private String getExternalIPAddress()
	{
		Device dev = getSelectedDevice();
		Action addPortAct = dev.getAction("GetExternalIPAddress");
		if (addPortAct == null) {
			showWarnning("GetExternalIPAddress is not found");
			return "";
		}
		if (addPortAct.postControlAction() == true) 
			return addPortAct.getArgumentValue("NewExternalIPAddress");
		return "";
	}
	
	private void addPortMapping(UpnpIGDToolAddPortDlg addPortDlg)
	{
		Device dev = getSelectedDevice();
		Action addPortAct = dev.getAction("AddPortMapping");
		if (addPortAct == null) {
			showWarnning("AddPortMapping is not found");
			return;
		}
		addPortAct.setArgumentValue("NewRemoteHost", "");
		addPortAct.setArgumentValue("NewProtocol", addPortDlg.getProtocol());
		addPortAct.setArgumentValue("NewPortMappingDescription", addPortDlg.getName());
		addPortAct.setArgumentValue("NewExternalPort", addPortDlg.getExternalPort());
		addPortAct.setArgumentValue("NewInternalClient", addPortDlg.getInternalIP());
		addPortAct.setArgumentValue("NewInternalPort", addPortDlg.getInternalPort());
		addPortAct.setArgumentValue("NewEnabled", 1);
		addPortAct.setArgumentValue("NewLeaseDuration", 0);
		
		String actionMsg = 
			addPortDlg.getName() + " " +
			"(" + addPortDlg.getProtocol() + ") : " +
			addPortDlg.getExternalPort() + " -> " +
			addPortDlg.getInternalIP() + ":" +
			addPortDlg.getInternalPort();
		
		if (addPortAct.postControlAction() == true) {
			UPnPStatus upnpStat = addPortAct.getStatus();
			String msg = "New port mapping is created\n" + actionMsg;
			showMessage(msg);
		}
		else {
			UPnPStatus upnpStat = addPortAct.getStatus();
			String msg = "New port mapping is failed\n" +
					upnpStat.getDescription() + " (" + upnpStat.getCode() + ")\n" +
					actionMsg;
			showWarnning(msg);
		}
	}
}

