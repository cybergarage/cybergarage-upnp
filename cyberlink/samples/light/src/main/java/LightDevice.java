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
import java.awt.*;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.control.*;

public class LightDevice extends Device implements ActionListener, QueryListener
{
	private final static String DESCRIPTION_FILE_NAME = "description/description.xml";

	private StateVariable powerVar;

	public LightDevice() throws InvalidDescriptionException
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
		
		ServiceList serviceList = getServiceList();
		Service service = serviceList.getService(0);
		service.setQueryListener(this);

		powerVar = getStateVariable("Power");

		Argument powerArg = getPowerAction.getArgument("Power");
		StateVariable powerState = powerArg.getRelatedStateVariable();
		AllowedValueList allowList = powerState.getAllowedValueList();
		for (int n=0; n<allowList.size(); n++) 
			System.out.println("[" + n + "] = " + allowList.getAllowedValue(n));
			
		AllowedValueRange allowRange = powerState.getAllowedValueRange();
		System.out.println("maximum = " + allowRange.getMaximum());
		System.out.println("minimum = " + allowRange.getMinimum());
		System.out.println("step = " + allowRange.getStep());
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

	private boolean onFlag = false;
	
	public void on()
	{
		onFlag = true;
		powerVar.setValue("on");
	}

	public void off()
	{
		onFlag = false;
		powerVar.setValue("off");
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
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		String actionName = action.getName();

		boolean ret = false;
		
		if (actionName.equals("GetPower") == true) {
			String state = getPowerState();
			Argument powerArg = action.getArgument("Power");
			powerArg.setValue(state);
			ret = true;
		}
		if (actionName.equals("SetPower") == true) {
			Argument powerArg = action.getArgument("Power");
			String state = powerArg.getValue();
			setPowerState(state);
			state = getPowerState();
			Argument resultArg = action.getArgument("Result");
			resultArg.setValue(state);
			ret = true;
		}

		comp.repaint();

		return ret;
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

