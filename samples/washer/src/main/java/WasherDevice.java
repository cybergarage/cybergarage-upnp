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

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.xml.ParserException;

public class WasherDevice extends Device implements ActionListener, QueryListener, Runnable
{
	private final static String DESCRIPTION_FILE_NAME = "description/description.xml";
	private final static String STATE_SERVICE_DESCRIPTION_FILE_NAME = "description/service/state/description.xml";

	private StateVariable stateVar;
	
	public WasherDevice() throws InvalidDescriptionException
	{
		super();
		loadDescription(WasherDevice.class.getResourceAsStream(DESCRIPTION_FILE_NAME));
		setSSDPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		setHTTPBindAddress(
				HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null)
		);
		
		Service stateService = getService("urn:upnp-org:serviceId:state:1");
		try {
			stateService.loadSCPD(WasherDevice.class.getResourceAsStream(STATE_SERVICE_DESCRIPTION_FILE_NAME));
		} catch (ParserException e) {
			throw new InvalidDescriptionException(e);
		}

		Action getStateAction = getAction("GetState");
		getStateAction.setActionListener(this);
		
		Action setStateAction = getAction("SetState");
		setStateAction.setActionListener(this);
		
		ServiceList serviceList = getServiceList();
		Service service = serviceList.getService(0);
		service.setQueryListener(this);

		stateVar = getStateVariable("State");
	}

	////////////////////////////////////////////////
	//	Component
	////////////////////////////////////////////////

	private WasherPane panel;
	
	public void setPanel(WasherPane comp)
	{
		panel = comp;	
	}
	
	public WasherPane getPanel()
	{
		return panel;
	}

	////////////////////////////////////////////////
	//	on/off
	////////////////////////////////////////////////

	private boolean washFlag = false;
	
	public void startWash()
	{
		timerThread = new Thread(this);
		timerThread.start();
		washFlag = true;
		stateVar.setValue("Start");
		panel.flipAnimationImage();
	}

	public void stopWash()
	{
		timerThread = null;
		washFlag = false;
		stateVar.setValue("Stop");
	}

	public void finishWash()
	{
		timerThread = null;
		washFlag = false;
		stateVar.setValue("Finish");
	}

	public boolean isWashing()
	{
		return washFlag;
	}
	
	public void setPowerState(String state)
	{
		if (state == null) {
			stopWash();
			return;
		}
		if (state.compareTo("1") == 0) {
			startWash();
			return;
		}
		if (state.compareTo("0") == 0) {
			stopWash();
			return;
		}
	}
	
	public String getWashState()
	{
		if (washFlag == true)
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
		
		if (actionName.equals("GetState") == true) {
			String state = getWashState();
			Argument stateArg = action.getArgument("State");
			stateArg.setValue(state);
			ret = true;
		}
		if (actionName.equals("SetState") == true) {
			Argument powerArg = action.getArgument("State");
			String state = powerArg.getValue();
			setPowerState(state);
			state = getWashState();
			Argument resultArg = action.getArgument("State");
			resultArg.setValue(state);
			ret = true;
		}

		panel.repaint();

		return ret;
	}

	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		return false;
	}

	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread timerThread = null;
		
	public void run()
	{
		Thread thisThread = Thread.currentThread();

		int cnt = 0;
		while (timerThread == thisThread && cnt < 20) {
			panel.flipAnimationImage();
			panel.repaint();
			try {
				Thread.sleep(500);
			}
			catch(InterruptedException e) {}
			cnt++;
		}

		finishWash();
		panel.repaint();
	}
	
	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	public void update()
	{
	}			
}

