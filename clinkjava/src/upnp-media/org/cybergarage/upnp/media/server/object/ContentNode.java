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
*	04/27/04
*		- Added setID(String) and setParentID(String).
*		- Changed getID() and getParentID() to return the string value instead of interger.
*		- Changed findContentNodeByID() to serach using a string id.
*
******************************************************************/

package org.cybergarage.upnp.media.server.object;

import java.io.*;
import org.cybergarage.xml.*;
import org.cybergarage.upnp.media.server.*;
import org.cybergarage.upnp.media.server.object.container.*;
import org.cybergarage.upnp.media.server.object.item.*;

public abstract class ContentNode extends Node
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String ID = "id";
	public final static String PARENT_ID = "parentID";
	public final static String RESTRICTED = "restricted";

	public final static String UNKNOWN = "UNKNOWN";
	
	////////////////////////////////////////////////
	// Constroctor
	////////////////////////////////////////////////
	
	public ContentNode()
	{
		setID(0);
		setParentID(-1);
		setRestricted(1);
		setContentDirectory(null);
	}

	////////////////////////////////////////////////
	// ContentDirectory
	////////////////////////////////////////////////
	
	private ContentDirectory contentDir;
	
	public void setContentDirectory(ContentDirectory cdir)
	{
		contentDir = cdir;
	}
	
	public ContentDirectory getContentDirectory()
	{
		return contentDir;
	}

	public MediaServer getMediaServer()
	{
		return getContentDirectory().getMediaServer();
	}
	
	////////////////////////////////////////////////
	// is*Node
	////////////////////////////////////////////////
	
	public boolean isContainerNode()
	{
		if (this instanceof ContainerNode)
			return true;
		return false;
	}

	public boolean isItemNode()
	{
		if (this instanceof ItemNode)
			return true;
		return false;
	}

	////////////////////////////////////////////////
	//	Child node
	////////////////////////////////////////////////

	public boolean hasContentNodes()
	{
		return hasNodes();
	}
	
	public int getNContentNodes() 
	{
		return getNNodes();
	}

	public ContentNode getContentNode(int index) 
	{
		return (ContentNode)getNode(index);
	}

	public ContentNode getContentNode(String name) 
	{
		return (ContentNode)getNode(name);
	}
	
	public abstract void addContentNode(ContentNode node);
	public abstract boolean removeContentNode(ContentNode node);
	
	public void removeAllContentNodes()
	{
		removeAllNodes();	
	}
	
	////////////////////////////////////////////////
	//	Property (Basic)
	////////////////////////////////////////////////

	private ContentPropertyList propList = new ContentPropertyList();

	public int getNProperties() {
		return propList.size();
	}

	public ContentProperty getProperty(int index) {
		return propList.getContentProperty(index);
	}

	public ContentProperty getProperty(String name) 
	{
		return propList.getContentProperty(name);
	}

	public void addProperty(ContentProperty prop) {
		propList.add(prop);
	}

	public void insertPropertyAt(ContentProperty prop, int index) {
		propList.insertElementAt(prop, index);
	}

	public void addProperty(String name, String value) {
		ContentProperty prop = new ContentProperty(name, value);
		addProperty(prop);
	}

	public boolean removeProperty(ContentProperty prop) {
		return propList.remove(prop);
	}

	public boolean removeProperty(String name) {
		return removeProperty(getProperty(name));
	}

	public boolean hasProperties()
	{
		if (0 < getNProperties())
			return true;
		return false;
	}
	
	////////////////////////////////////////////////
	//	Property (Extention)
	////////////////////////////////////////////////

	public void setProperty(String name, String value) {
		ContentProperty prop = getProperty(name);
		if (prop != null) {
			prop.setValue(value);
			return;
		}
		prop = new ContentProperty(name, value);
		addProperty(prop);
	}

	public void setProperty(String name, int value) {
		setProperty(name, Integer.toString(value));
	}

	public void setProperty(String name, long value) {
		setProperty(name, Long.toString(value));
	}

	public String getPropertyValue(String name) {
		ContentProperty prop = getProperty(name);
		if (prop != null)
			return prop.getValue();
		return "";
	}

	public int getPropertyIntegerValue(String name) {
		String val = getPropertyValue(name);
		try {
			return Integer.parseInt(val);
		}
		catch (Exception e) {}
		return 0;
	}

	public long getPropertyLongValue(String name) {
		String val = getPropertyValue(name);
		try {
			return Long.parseLong(val);
		}
		catch (Exception e) {}
		return 0;
	}

	////////////////////////////////////////////////
	//	Property Attribute (Extention)
	////////////////////////////////////////////////

	public void setPropertyAttribure(String propName, String attrName, String value) 
	{
		ContentProperty prop = getProperty(propName);
		if (prop == null) {
			prop = new ContentProperty(propName, "");
			addProperty(prop);
		}
		prop.setAttribute(attrName, value);
	}

	public String getPropertyAttribureValue(String propName, String attrName) 
	{
		ContentProperty prop = getProperty(propName);
		if (prop != null)
			return prop.getAttributeValue(attrName);
		return "";
	}

	////////////////////////////////////////////////
	//	findContentNodeBy*
	////////////////////////////////////////////////
	
	public ContentNode findContentNodeByID(String id)
	{
		if (id == null)
			return null;
			
		String nodeID = getID();
		if (id.equals(nodeID) == true)
			return this;

		int nodeCnt = getNContentNodes();
		for (int n=0; n<nodeCnt; n++) {
			ContentNode cnode = getContentNode(n);
			ContentNode fnode = cnode.findContentNodeByID(id);
			if (fnode != null)
				return fnode;	
		}
		
		return null;
	}
	
	////////////////////////////////////////////////
	// set
	////////////////////////////////////////////////

	public void set(Node node)
	{
		int n;

		// Child Node -> Property;
		int nNode = node.getNNodes();
		for (n=0; n<nNode; n++) {
			Node cnode = node.getNode(n);
			if (ContainerNode.isContainerNode(cnode) == true)
				continue;
			if (ItemNode.isItemNode(cnode) == true)
				continue;
			setProperty(cnode.getName(), cnode.getValue());
		}

		// Attribute -> Attribute;
		int nAttr = node.getNAttributes();
		for (n=0; n<nAttr; n++) {
			Attribute attr = node.getAttribute(n);
			setAttribute(attr.getName(), attr.getValue());
		}
	}
	
	////////////////////////////////////////////////
	// ID
	////////////////////////////////////////////////
	
	public void setID(int id)
	{
		setAttribute(ID, id);
	}
	
	public void setID(String id)
	{
		setAttribute(ID, id);
	}
	
	public String getID()
	{
		return getAttributeValue(ID);
	}

	////////////////////////////////////////////////
	// parentID
	////////////////////////////////////////////////
	
	public void setParentID(int id)
	{
		setAttribute(PARENT_ID, id);
	}
	
	public void setParentID(String id)
	{
		setAttribute(PARENT_ID, id);
	}
	
	public String getParentID()
	{
		return getAttributeValue(PARENT_ID);
	}

	////////////////////////////////////////////////
	// parentID
	////////////////////////////////////////////////
	
	public void setRestricted(int id)
	{
		setAttribute(RESTRICTED, id);
	}
	
	public int getRestricted()
	{
		return getAttributeIntegerValue(RESTRICTED);
	}

	////////////////////////////////////////////////
	// dc:title
	////////////////////////////////////////////////
	
	public void setTitle(String title)
	{
		setProperty(DC.TITLE, title);
	}
	
	public String getTitle()
	{
		return getPropertyValue(DC.TITLE);
	}

	////////////////////////////////////////////////
	// upnp:class
	////////////////////////////////////////////////
	
	public void setUPnPClass(String title)
	{
		setProperty(UPnP.CLASS, title);
	}
	
	public String getUPnPClass()
	{
		return getPropertyValue(UPnP.CLASS);
	}

	////////////////////////////////////////////////
	// upnp:writeStatus
	////////////////////////////////////////////////
	
	public void setWriteStatus(String status)
	{
		setProperty(UPnP.WRITE_STATUS, status);
	}
	
	public String getWriteStatus()
	{
		return getPropertyValue(UPnP.WRITE_STATUS);
	}

	////////////////////////////////////////////////
	//	toString 
	////////////////////////////////////////////////

	private void outputPropertyAttributes(PrintWriter ps, ContentProperty prop)
	{
		int nAttributes = prop.getNAttributes();
		for (int n=0; n<nAttributes; n++) {
			Attribute attr = prop.getAttribute(n);
			ps.print(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
		}
	}
	
	public void output(PrintWriter ps, int indentLevel, boolean hasChildNode) 
	{
		String indentString = getIndentLevelString(indentLevel);

		String name = getName();
		String value = getValue();

		if (hasNodes() == false && hasProperties() == false) {		
			ps.print(indentString + "<" + name);
			outputAttributes(ps);
			ps.println(">" + value + "</" + name + ">");
			return;
		}
		
		ps.print(indentString + "<" + name);
		outputAttributes(ps);
		ps.println(">");
	
		int nProps = getNProperties();
		for (int n=0; n<nProps; n++) {
			String propIndentString = getIndentLevelString(indentLevel+1);
			ContentProperty prop = getProperty(n);
			String propName = prop.getName();
			String propValue = prop.getValue();
			if (prop.hasAttributes() == true) {
				ps.print(propIndentString + "<" + propName);
				outputPropertyAttributes(ps, prop);
				ps.println(">" + propValue + "</" + propName + ">");
			}
			else
				ps.println(propIndentString + "<" + propName + ">" + propValue + "</" + propName + ">");
		}
		
		if (hasChildNode == true) {
			int nChildNodes = getNNodes();
			for (int n=0; n<nChildNodes; n++) {
				Node cnode = getNode(n);
				cnode.output(ps, indentLevel+1, true);
			}
		}

		ps.println(indentString +"</" + name + ">");
	}
	
}

