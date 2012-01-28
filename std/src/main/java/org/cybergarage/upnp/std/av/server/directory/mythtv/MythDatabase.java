/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File : MythTVDatabase.java
*
*	Revision:
*
*	02/11/04
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.std.av.server.directory.mythtv;

import java.util.*;
import org.cybergarage.sql.mysql.*;

public class MythDatabase extends MySQL
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	private final static String DB_NAME = "mythconverg";
	private final static String DB_USER = "mythtv";
	private final static String DB_PASSWD = "mythtv";
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public MythDatabase()
	{
	}
	
	////////////////////////////////////////////////
	// open
	////////////////////////////////////////////////
	
	public boolean open(String host)
	{
		return super.open(host, DB_NAME, DB_USER, DB_PASSWD);
	}
	
	public boolean open()
	{
		// grant all on mythconverg.* to mythtv@"localhost.localdomain" identified by "mythtv";
		return open("localhost");
	}

	////////////////////////////////////////////////
	// open
	////////////////////////////////////////////////
	
	public String getRecordFilePrefix()
	{
		String sql = "select * from settings where value = 'RecordFilePrefix'";
		
		if (query(sql) == false)
			return "";
		
		if (fetch() == false)
			return "";

		return getString("data");			
	}
	
	////////////////////////////////////////////////
	// RecordedInfo
	////////////////////////////////////////////////
	
	public MythRecordedInfo[] getRecordedInfos()
	{
		Vector recVec = new Vector();
		
		String recFilePrefix = getRecordFilePrefix();
		
		String sql = "select * from recorded";
		
		if (query(sql) == false)
			return new MythRecordedInfo[0];
		
		while (fetch() == true) {
			MythRecordedInfo recInfo = new MythRecordedInfo();
			recInfo.setRecordFilePrefix(recFilePrefix);
			recInfo.setChanID(getInteger("chanid"));
			recInfo.setRecordID(getInteger("recordid"));
			recInfo.setStartTime(getTimestamp("starttime"));
			recInfo.setEndTime(getTimestamp("endtime"));
			recInfo.setTitle(getString("title"));
			recInfo.setSubTitle(getString("subtitle"));
			recInfo.setDescription(getString("description"));
			recInfo.setCategory(getString("category"));
            recInfo.setFileName((getString("basename")));
            recInfo.setFileSize(recInfo.getFile().length());
            recVec.add(recInfo);
		}
		
		int recCnt = recVec.size();
		MythRecordedInfo recArray[] = new MythRecordedInfo[recCnt];
		for (int n=0; n<recCnt; n++)
			recArray[n] = (MythRecordedInfo)recVec.get(n);
		return recArray;
	}
}

