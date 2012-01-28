/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : DeviceProfile.java
*
*	04/12/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui;

public class DeviceProfile
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public DeviceProfile()
	{
		setConnectedFlag(false);
		setResultMetaData("");
	}

	////////////////////////////////////////////////
	//	connectedFlag
	////////////////////////////////////////////////
	
	private boolean connectedFlag;
	
	public void setConnectedFlag(boolean flag)
	{
		connectedFlag = flag;
	}
	
	public boolean isConnected()
	{
		return connectedFlag;
	}
	
	////////////////////////////////////////////////
	//	ResultMetaData
	////////////////////////////////////////////////
	
	private String resultMetaData;
	
	public void setResultMetaData(String value)
	{
		resultMetaData = value;
	}
	
	public String getResultMetaData()
	{
		return resultMetaData;
	}
}
