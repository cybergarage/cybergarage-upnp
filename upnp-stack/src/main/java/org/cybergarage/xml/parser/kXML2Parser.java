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

import java.io.InputStream;
import java.io.InputStreamReader;

import org.cybergarage.xml.Node;
import org.cybergarage.xml.ParserException;
import org.kxml2.io.KXmlParser;

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
			KXmlParser xpp = new KXmlParser();
			xpp.setInput(inReader);
			int eventType = xpp.getEventType();
			while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case org.xmlpull.v1.XmlPullParser.START_TAG:
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
				case org.xmlpull.v1.XmlPullParser.TEXT:
					{
						String value = xpp.getText();
						if (currNode != null)
							currNode.setValue(value);
					}
					break;
				case org.xmlpull.v1.XmlPullParser.END_TAG:
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

