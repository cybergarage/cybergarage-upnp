/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : ClockDevice.java
*
******************************************************************/

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.*;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.control.*;

public class AirconDevice extends Device implements ActionListener, QueryListener
{
	private final static String DESCRIPTION_FILE_NAME = "description/description.xml";

	private StateVariable tempVar;
	
	public AirconDevice() throws InvalidDescriptionException
	{		
		super(new File(DESCRIPTION_FILE_NAME));
		setSSDPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		setHTTPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		
		Action getPowerAction = getAction("GetPower");
		getPowerAction.setActionListener(this);
		
		Action setPowerAction = getAction("SetPower");
		setPowerAction.setActionListener(this);
		
		Action getTempAction = getAction("GetTemp");
		getTempAction.setActionListener(this);
		
		Action setTempAction = getAction("SetTemp");
		setTempAction.setActionListener(this);
		
		ServiceList serviceList = getServiceList();
		Service service = serviceList.getService(0);
		service.setQueryListener(this);

		tempVar = getStateVariable("Temp");
		
		setTempture("18");
	}

	////////////////////////////////////////////////
	//	Component
	////////////////////////////////////////////////

	private Component comp;
	
	public void setComponent(Component comp)
	{
		this.comp = comp;	
	}
	
	public Component getComponent()
	{
		return comp;
	}
	
	////////////////////////////////////////////////
	//	on/off
	////////////////////////////////////////////////

	private boolean onFlag = true;
	
	public void on()
	{
		onFlag = true;
	}

	public void off()
	{
		onFlag = false;
	}

	public boolean isOn()
	{
		return onFlag;
	}
	
	public void setPowerState(String state)
	{
		if (state == null) {
			off();
			return;
		}
		if (state.compareTo("1") == 0) {
			on();
			return;
		}
		if (state.compareTo("0") == 0) {
			off();
			return;
		}
	}
	
	public String getPowerState()
	{
		if (onFlag == true)
			return "1";
		return "0";
	}

	////////////////////////////////////////////////
	//	on/off
	////////////////////////////////////////////////

	private int tempture = 18;
	
	public void setTempture(String state)
	{
		if (isOn() == false)
			return;
			
		if (state.compareTo("+1") == 0)
			tempture++;
		else if (state.compareTo("-1") == 0)
			tempture--;
		else {
			try {
				tempture = Integer.parseInt(state);
			}
			catch (Exception e) {};
		}
		
		tempVar.setValue(Integer.toString(tempture));
	}
	
	public String getTempture()
	{
		return Integer.toString(tempture);
	}

	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		String actionName = action.getName();
		if (actionName.equals("GetPower") == true) {
			String state = getPowerState();
			Argument powerArg = action.getArgument("Power");
			powerArg.setValue(state);
			comp.repaint();
			return true;
		}
		if (actionName.equals("SetPower") == true) {
			Argument powerArg = action.getArgument("Power");
			String state = powerArg.getValue();
			setPowerState(state);
			state = getPowerState();
			Argument resultArg = action.getArgument("Result");
			resultArg.setValue(state);
			comp.repaint();
			return true;
		}
		if (actionName.equals("GetTemp") == true) {
			String temp = getTempture();
			Argument tempArg = action.getArgument("Temp");
			tempArg.setValue(temp);
			comp.repaint();
			return true;
		}
		if (actionName.equals("SetTemp") == true) {
			Argument powerArg = action.getArgument("Temp");
			String temp = powerArg.getValue();
			setTempture(temp);
			temp = getTempture();
			Argument resultArg = action.getArgument("Result");
			resultArg.setValue(temp);
			comp.repaint();
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		stateVar.setValue(getPowerState());
		return true;
	}

	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	public void update()
	{
	}			
}

