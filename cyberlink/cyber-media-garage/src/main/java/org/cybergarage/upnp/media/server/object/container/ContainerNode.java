/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2003
*
*	File : ContentNode
*
*	Revision:
*
*	10/22/03
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.media.server.object.container;

import org.cybergarage.xml.*;
import org.cybergarage.upnp.media.server.object.*;

public class ContainerNode extends ContentNode
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String NAME = "container";
	
	public final static String CHILD_COUNT = "childCount";
	public final static String SEARCHABLE = "searchable";

	public final static String OBJECT_CONTAINER = "object.container";

	////////////////////////////////////////////////
	// Constroctor
	////////////////////////////////////////////////
	
	public ContainerNode()
	{
		setID(-1);
		setName(NAME);
		setSearchable(0);
		setChildCount(0);
		setUPnPClass(OBJECT_CONTAINER);
		setWriteStatus(UNKNOWN);
	}

	////////////////////////////////////////////////
	//	isContainerNode
	////////////////////////////////////////////////
	
	public final static boolean isContainerNode(Node node)
	{
		String name = node.getName();
		if (name == null)
			return false;
		return name.equals(NAME);
	}
	
	////////////////////////////////////////////////
	//	Child node
	////////////////////////////////////////////////

	public void addContentNode(ContentNode node) 
	{
		addNode(node);
		node.setParentID(getID());
		setChildCount(getNContentNodes());
		node.setContentDirectory(getContentDirectory());
	}

	public boolean removeContentNode(ContentNode node) 
	{
		boolean ret = removeNode(node);
		setChildCount(getNContentNodes());
		return ret;
	}

	////////////////////////////////////////////////
	// chileCount
	////////////////////////////////////////////////
	
	public void setChildCount(int id)
	{
		setAttribute(CHILD_COUNT, id);
	}
	
	public int getChildCount()
	{
		return getAttributeIntegerValue(CHILD_COUNT);
	}

	////////////////////////////////////////////////
	// searchable
	////////////////////////////////////////////////
	
	public void setSearchable(int value)
	{
		setAttribute(SEARCHABLE, value);
	}
	
	public int getSearchable()
	{
		return getAttributeIntegerValue(SEARCHABLE);
	}
	
}

