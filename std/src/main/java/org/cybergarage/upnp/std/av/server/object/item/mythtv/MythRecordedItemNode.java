/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2003
*
*	File : MythRecordedItemNode.java
*
*	Revision:
*
*	02/12/04
*		- first revision.
*	08/10/04
*		- Changed the mime type to video/mpeg.
*		- Added the size attribure to the protocolInfo.
*
******************************************************************/

package org.cybergarage.upnp.std.av.server.object.item.mythtv;

import java.io.*;

import org.cybergarage.xml.*;
import org.cybergarage.util.*;
import org.cybergarage.upnp.std.av.server.*;
import org.cybergarage.upnp.std.av.server.directory.mythtv.*;
import org.cybergarage.upnp.std.av.server.object.item.*;

public class MythRecordedItemNode extends ItemNode
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////
	
	private final static String MIME_TYPE = "video/mpeg";
	//private final static String MIME_TYPE = "*/*";
	
	////////////////////////////////////////////////
	// Constroctor
	////////////////////////////////////////////////
	
	public MythRecordedItemNode()
	{
		setRecordedInfo(null);
	}

	////////////////////////////////////////////////
	// RecordedInfo
	////////////////////////////////////////////////

	private MythRecordedInfo recInfo;
	
	public MythRecordedInfo getRecordedInfo()
	{
		return recInfo;
	}

	public void setRecordedInfo(MythRecordedInfo info)
	{	
		recInfo = info;
		
		if (info == null)
			return;
			
		// Title
		setTitle(info.getTitle());
			
		// Creator
		setCreator("");

		// Media Class
		setUPnPClass(UPnP.OBJECT_ITEM_VIDEOITEM_MOVIE);

		// Date
		setDate(info.getStartTime());
		
        setStorageUsed(info.getFileSize());

		// ProtocolInfo
		String protocol = ConnectionManager.HTTP_GET + ":*:" + MIME_TYPE + ":*";
		String id = getID();
		String url = getContentDirectory().getContentExportURL(id);
		AttributeList attrList = new AttributeList();
		Attribute attr = new Attribute(ItemNode.SIZE, Long.toString(info.getFileSize()));
		attrList.add(attr);
		setResource(url, protocol, attrList);
	}
	
	////////////////////////////////////////////////
	// equals
	////////////////////////////////////////////////
	
	public boolean equals(MythRecordedInfo info)
	{
		MythRecordedInfo recInfo = getRecordedInfo();
		if (info == null || recInfo == null)
			return false;
		if (info.getChanID() == recInfo.getChanID())
			return true;
		return false;
	}

	////////////////////////////////////////////////
	// Abstract methods
	////////////////////////////////////////////////
	
	public byte[] getContent()
	{
		File recFile = getRecordedInfo().getFile();
		if (!recFile.exists())
			return new byte[0];
		byte fileByte[] = new byte[0];
		try {
			fileByte = FileUtil.load(recFile); 
		}
		catch (Exception e) {}
		return fileByte;
	}

	public long getContentLength()
	{
		File recFile = getRecordedInfo().getFile();
		return recFile.length();
	}
	
	public InputStream getContentInputStream()
	{
		try {	
			File recFile = getRecordedInfo().getFile();
			return new FileInputStream(recFile);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return null;
	}

	public String getMimeType()
	{
		return MIME_TYPE;
	}
}

