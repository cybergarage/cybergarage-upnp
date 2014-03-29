/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File : MythDirectory.java
*
*	Revision:
*
*	02/10/04
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.std.av.server.directory.mythtv;

import org.cybergarage.util.*;
import org.cybergarage.upnp.std.av.server.*;
import org.cybergarage.upnp.std.av.server.object.item.mythtv.*;

public class MythDirectory extends Directory
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	private final static String NAME = "MythTV";
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public MythDirectory(String name)
	{
		super(name);
	}

	public MythDirectory()
	{
		this(NAME);
	}
	
	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	private MythRecordedItemNode[] getAddedRecordedItemNodes()
	{
		int nContents = getNContentNodes();
		MythRecordedItemNode recNode[] = new MythRecordedItemNode[nContents];
		for (int n=0; n<nContents; n++)
			recNode[n] = (MythRecordedItemNode)getContentNode(n);
		return recNode;		
	}		

	private MythRecordedInfo[] getCurrentRecordedInfos()
	{
		MythDatabase mythdb = new MythDatabase();
		mythdb.open();
		MythRecordedInfo recInfo[] = mythdb.getRecordedInfos();
		mythdb.close();
		return recInfo;
	}

	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////
	
	public boolean update()
	{
		boolean updateFlag = false;

		MythRecordedItemNode addedItemNode[] = getAddedRecordedItemNodes();
		MythRecordedInfo currRecInfo[] = getCurrentRecordedInfos();
		int nAddedItems = addedItemNode.length;
		int nCurrRecInfos = currRecInfo.length;
		
		// Checking Deleted Items
		for (int i=0; i<nAddedItems; i++) {
			MythRecordedItemNode recItem = addedItemNode[i];
			boolean hasRecItem = false;
			for (int j=0; j<nCurrRecInfos; j++) {
				MythRecordedInfo recInfo = currRecInfo[j];
				if (recItem.equals(recInfo) == true) {
					hasRecItem = true;
					break;
				}
			}
			if (hasRecItem == true)
				continue;
			removeContentNode(recItem);
			updateFlag = true;
		}
		
		// Checking Added Items
		for (int j=0; j<nCurrRecInfos; j++) {
			MythRecordedInfo recInfo = currRecInfo[j];
			boolean hasRecItem = false;
			for (int i=0; i<nAddedItems; i++) {
				MythRecordedItemNode recItem = addedItemNode[i];
				if (recItem.equals(recInfo) == true) {
					hasRecItem = true;
					break;
				}
			}
			if (hasRecItem == true)
				continue;
		
			// Add new item.
			MythRecordedItemNode recItem = new MythRecordedItemNode();
			int newItemID = getContentDirectory().getNextItemID();
			recItem.setID(newItemID);
			recItem.setContentDirectory(getContentDirectory());
			recItem.setRecordedInfo(recInfo);
			addContentNode(recItem);
			updateFlag = true;
		}
		
		return updateFlag;
	}
	
	////////////////////////////////////////////////
	// main
	////////////////////////////////////////////////
	
	public static void main(String args[]) 
	{
		Debug.on();
		MythDirectory mythdir = new MythDirectory();
		mythdir.update();
	}

}

