/******************************************************************
*
*	MediaPlayer for CyberLink
*
*	Copyright (C) Satoshi Konno 2005
*
*	File : MediaPlayer.java
*
*	09/26/05
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.media.player;

import org.cybergarage.util.*;
import org.cybergarage.xml.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.media.player.action.BrowseAction;
import org.cybergarage.upnp.media.player.action.BrowseResult;
import org.cybergarage.upnp.media.server.*;
import org.cybergarage.upnp.media.server.object.*;
import org.cybergarage.upnp.media.server.object.item.*;
import org.cybergarage.upnp.media.server.object.container.*;
import org.cybergarage.upnp.media.player.action.*;

public class MediaPlayer extends ControlPoint
{
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public MediaPlayer()
	{
	}

	////////////////////////////////////////////////
	// DeviceList
	////////////////////////////////////////////////
	
	public DeviceList getServerDeviceList()
	{
		DeviceList mediaServerDevList = new DeviceList();
		
		DeviceList allDevList = getDeviceList();
		int allDevCnt = allDevList.size();
		for (int n=0; n<allDevCnt; n++) {
			Device dev = allDevList.getDevice(n);
			if (dev.isDeviceType(MediaServer.DEVICE_TYPE) == false)
				continue;
			mediaServerDevList.add(dev);
		}
		return mediaServerDevList;
	}

	////////////////////////////////////////////////
	// browse
	////////////////////////////////////////////////
	
	public Node browse(
			Device dev,
			String objectID,
			String browseFlag,
			String filter,
			int startIndex,
			int requestedCount,
			String sortCaiteria)
	{
		System.out.println("browse " + objectID + ", " + browseFlag + ", " + startIndex + ", " + requestedCount);
		
		if (dev == null)
			return null;
		
		Service conDir = dev.getService(ContentDirectory.SERVICE_TYPE);
		if (conDir == null)
			return null;
		Action action = conDir.getAction(ContentDirectory.BROWSE);
		if (action == null)
			return null;

		BrowseAction browseAction = new BrowseAction(action);
		browseAction.setObjectID(objectID);
		browseAction.setBrowseFlag(browseFlag);
		browseAction.setStartingIndex(startIndex);
		browseAction.setRequestedCount(requestedCount);
		browseAction.setFilter(filter);
		browseAction.setSortCriteria(sortCaiteria);
		if (browseAction.postControlAction() == false)
			return null;

		Argument resultArg = browseAction.getArgument(BrowseAction.RESULT);
		if (resultArg == null)
			return null;

		String resultStr = resultArg.getValue();
		if (resultStr == null)
			return null;

		Node node = null;

		Parser xmlParser = UPnP.getXMLParser();

		try {
			node = xmlParser.parse(resultStr);
		}
		catch (ParserException pe) {
			Debug.warning(pe);
			return null;
		};
		
		return node;
	}

	////////////////////////////////////////////////
	// browse*
	////////////////////////////////////////////////
	
	public Node browseMetaData(
			Device dev,
			String objectID,
			String filter,
			int startIndex,
			int requestedCount,
			String sortCaiteria)
	{
		return browse(dev, objectID, BrowseAction.BROWSE_METADATA, filter, startIndex, requestedCount, sortCaiteria);
	}

	
	public Node browseDirectChildren(
		Device dev,
		String objectID,
		String filter,
		int startIndex,
		int requestedCount,
		String sortCaiteria)
	{
		return browse(dev, objectID, BrowseAction.BROWSE_DIRECT_CHILDREN, filter, startIndex, requestedCount, sortCaiteria);
	}

	////////////////////////////////////////////////
	// Content
	////////////////////////////////////////////////

	public ContentNode getContentDirectory(Device dev)
	{
		Node rootNode = browseMetaData(dev, "0", "*", 0, 0, "");
		if (rootNode == null)
			return null;

		ContentNode contentRootNode = new RootNode();
		contentRootNode.set(rootNode);

		getContentDirectory(contentRootNode, dev, "0");
		if (Debug.isOn() == true)
			contentRootNode.print();

		return contentRootNode;
	}

	public int getContentDirectory(ContentNode parentNode, Device dev, String objectID)
	{
		if (objectID == null)
			return 0;

		Node resultNode = browseDirectChildren(dev, objectID, "*", 0, 0, "");
		if (resultNode == null)
			return 0;
		
		BrowseResult browseResult = new BrowseResult(resultNode);
		int nResultNode = 0;
		int nContents = browseResult.getNContentNodes();
		for (int n=0; n<nContents; n++) {
			Node xmlNode = browseResult.getContentNode(n);
			ContentNode contentNode = null;
			if (ContainerNode.isContainerNode(xmlNode) == true) {
				contentNode = new ContainerNode();
			}
			else if (ItemNode.isItemNode(xmlNode) == true)
				contentNode = new BrowseResultNode();
			if (contentNode == null)
				continue;
			contentNode.set(xmlNode);
			parentNode.addContentNode(contentNode);
			nResultNode++;
			// Add Child Nodes
			if (contentNode.isContainerNode() == true) {
				ContainerNode containerNode = (ContainerNode)contentNode;
				int childCnt = containerNode.getChildCount();
				if (0 < childCnt) {
					String objid = containerNode.getID();
					getContentDirectory(contentNode, dev, objid);
				}
			}
		}

		return nResultNode;
	}

	////////////////////////////////////////////////
	// main
	////////////////////////////////////////////////

	public void printContentNode(ContentNode node, int indentLevel)
	{
			int n;
			
			for (n=0; n<indentLevel; n++)
				System.out.print("  ");
			System.out.print(node.getTitle());
			if (node.isItemNode() == true) {
				ItemNode itemNode = (ItemNode)node;
				String res = itemNode.getResource();
				String protocolInfo = itemNode.getProtocolInfo();
				System.out.print(" (" + res + ")");
			}
			System.out.println("");
			
			int nContentNodes = node.getNContentNodes();
			for (n=0; n<nContentNodes; n++) {
				ContentNode cnode = node.getContentNode(n);
				printContentNode(cnode, indentLevel+1);
			}
	}

	public void printContentDirectory(Device dev)
	{
			ContentNode node = getContentDirectory(dev);
			if (node == null)
				return;
			//node->print();
			printContentNode(node, 1);
	}
	
	public void printMediaServers()
	{
			DeviceList devList = getDeviceList();
			int devCnt = devList.size();
			int mediaServerCnt = 0;
			for (int n=0; n<devCnt; n++) {
				Device dev = devList.getDevice(n);
				if (dev.isDeviceType(MediaServer.DEVICE_TYPE) == false)
					continue;
				System.out.println("[" + n +  "] " + dev.getFriendlyName() + ", " + dev.getLeaseTime() + ", " + dev.getElapsedTime());
				printContentDirectory(dev);
				mediaServerCnt++;
			}		
			if (mediaServerCnt == 0) {
				System.out.println("MediaServer is not found");
			}
	}
	
	////////////////////////////////////////////////
	// main
	////////////////////////////////////////////////

	public static void main(String args[]) 
	{
		MediaPlayer mplayer = new MediaPlayer();
		
		mplayer.start();
		
		while(true) {
			mplayer.printMediaServers();
			try {
				Thread.sleep(1000*20);
			}
			catch (Exception e) {};
		}
		
		//mplayer.stop();
	}
}

