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
*	02/09/05
*		- Stefano Lenzi <kismet-sl@users.sourceforge.net>
*		- Fixed a bug in XercesParser::parse(Node,Node,int) that is when you faound an xml like that <test></test> 
*		  you crate a node with name="test" and value=null that is non correct. It should had name="test" and value="". 
*
******************************************************************/

package org.cybergarage.xml.parser;

import java.io.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xerces.parsers.DOMParser;

import org.cybergarage.xml.*;

public class XercesParser extends org.cybergarage.xml.Parser
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public XercesParser()
	{
	}

	////////////////////////////////////////////////
	//	parse (Node)
	////////////////////////////////////////////////

	public org.cybergarage.xml.Node parse(org.cybergarage.xml.Node parentNode, org.w3c.dom.Node domNode, int rank)
	{
		int domNodeType = domNode.getNodeType();
//		if (domNodeType != Node.ELEMENT_NODE)
//			return;
			
		String domNodeName = domNode.getNodeName();
		String domNodeValue = domNode.getNodeValue();
		NamedNodeMap attrs = domNode.getAttributes(); 
		int arrrsLen = (attrs != null) ? attrs.getLength() : 0;

//		Debug.message("[" + rank + "] ELEM : " + domNodeName + ", " + domNodeValue + ", type = " + domNodeType + ", attrs = " + arrrsLen);

		if (domNodeType == org.w3c.dom.Node.TEXT_NODE) {
			parentNode.setValue(domNodeValue);
			return parentNode;
		}

		if (domNodeType != org.w3c.dom.Node.ELEMENT_NODE)
			return parentNode;

		org.cybergarage.xml.Node node = new org.cybergarage.xml.Node();
		node.setName(domNodeName);
		node.setValue(domNodeValue);

		if (parentNode != null)
			parentNode.addNode(node);

		NamedNodeMap attrMap = domNode.getAttributes(); 
		int attrLen = attrMap.getLength();
		//Debug.message("attrLen = " + attrLen);
		for (int n = 0; n<attrLen; n++) {
			org.w3c.dom.Node attr = attrMap.item(n);
			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();
			node.setAttribute(attrName, attrValue);
		}
		
		// Thanks for Stefano Lenzi (02/10/05)
		org.w3c.dom.Node child = domNode.getFirstChild(); 
		if(child==null){ 
			node.setValue(""); 
			return node; 
		} 
		do{ 
			parse(node, child, rank+1); 
			child = child.getNextSibling();  
		}while (child != null); 

		
		return node;
	}

	public org.cybergarage.xml.Node parse(org.cybergarage.xml.Node parentNode, org.w3c.dom.Node domNode)
	{
		return parse(parentNode, domNode, 0);
	}
	
	////////////////////////////////////////////////
	//	parse
	////////////////////////////////////////////////

	public org.cybergarage.xml.Node parse(InputStream inStream) throws ParserException
	{
		org.cybergarage.xml.Node root = null;
		
		try {
			DOMParser parser = new DOMParser();
			InputSource inSrc = new InputSource(inStream);
			parser.parse(inSrc);

			Document doc = parser.getDocument();
			org.w3c.dom.Element docElem = doc.getDocumentElement();

			if (docElem != null)
				root = parse(root, docElem);
/*
			NodeList rootList = doc.getElementsByTagName("root");
			Debug.message("rootList = " + rootList.getLength());
			
			if (0 < rootList.getLength())
				root = parse(root, rootList.item(0));
*/
		}
		catch (Exception e) {
			throw new ParserException(e);
		}
		
		return root;
	}
	
}

