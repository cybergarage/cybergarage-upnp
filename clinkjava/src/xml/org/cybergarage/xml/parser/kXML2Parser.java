/******************************************************************
*
*	CyberXML for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: XercesParser.java
*
*	Revision;
*
*	11/26/02
*		- first revision.
*	12/26/03
*		- Changed to the file name from Parser.java to XercesParser.java.
*		- Changed to implement org.cybergarage.xml.Parser interface.
*
******************************************************************/

package org.cybergarage.xml.parser;

import java.io.*;

import org.kxml2.io.*;
import org.xmlpull.v1.*;

import org.cybergarage.xml.*;

public class kXML2Parser extends org.cybergarage.xml.Parser
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public kXML2Parser()
	{
	}

	////////////////////////////////////////////////
	//	parse
	////////////////////////////////////////////////

	public Node parse(InputStream inStream) throws ParserException
	{
		Node rootNode = null;
		Node currNode = null;
		
		try {
			InputStreamReader inReader = new InputStreamReader(inStream);
			XmlPullParser xpp = new KXmlParser();
			xpp.setInput(inReader);
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					{
						Node node = new Node();
						String nodeName = xpp.getName();
						node.setName(nodeName);
						int attrsLen = xpp.getAttributeCount();
						for (int n=0; n<attrsLen; n++) {
							String attrName = xpp.getAttributeName(n);
							String attrValue = xpp.getAttributeValue(n);
							node.setAttribute(attrName, attrValue);
						}
					
						if (currNode != null)
							currNode.addNode(node);
						currNode = node;
						if (rootNode == null)
							rootNode = node;
					}
					break;
				case XmlPullParser.TEXT:
					{
						String value = xpp.getText();
						if (currNode != null)
							currNode.setValue(value);
					}
					break;
				case XmlPullParser.END_TAG:
					{
						currNode = currNode.getParentNode();
					}
					break;
				}
				eventType = xpp.next();
			}
		}
		catch (Exception e) {
			throw new ParserException(e);
		}
		
		return rootNode;
	}
	
}

