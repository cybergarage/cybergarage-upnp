/******************************************************************
*
* CyberLink for Java
*
* Copyright (C) Satoshi Konno 2002-2012
*
* This is licensed under BSD-style license, see file COPYING.
*
******************************************************************/

package org.cybergarage.xml;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cybergarage.xml.Node;

public class NodeTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NodeTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( NodeTest.class );
    }

    public void testSetChildNode()
    {
    	Node parentNode = new Node();
    	String childNodeName;
    	String childNodeValue;
    	Node childNode;
    	
    	assertEquals(0, parentNode.getNNodes());
    	
    	childNodeName = "cnode1";
    	childNodeValue = "cvalue1";
    	
    	parentNode.setNode(childNodeName, childNodeValue);
    	assertTrue(parentNode.hasNode(childNodeName));
    	assertEquals(1, parentNode.getNNodes());
    	childNode = parentNode.getNode(childNodeName);
    	assertNotNull(childNode);
    	assertEquals(childNodeName, childNode.getName());
    	assertEquals(childNodeValue, childNode.getValue());
    	
    	parentNode.setNode(childNodeName);
    	assertTrue(parentNode.hasNode(childNodeName));
    	assertEquals(1, parentNode.getNNodes());
    	childNode = parentNode.getNode(childNodeName);
    	assertNotNull(childNode);
    	assertEquals(childNodeName, childNode.getName());
    	assertEquals(childNodeValue, childNode.getValue());
    	
    	childNodeName = "cnode2";
    	childNodeValue = "cvalue2";
    	
    	parentNode.setNode(childNodeName);
    	assertTrue(parentNode.hasNode(childNodeName));
    	assertEquals(2, parentNode.getNNodes());
    	childNode = parentNode.getNode(childNodeName);
    	assertNotNull(childNode);
    	assertEquals(childNodeName, childNode.getName());
    	
    	parentNode.setNode(childNodeName, childNodeValue);
    	assertTrue(parentNode.hasNode(childNodeName));
    	assertEquals(2, parentNode.getNNodes());
    	childNode = parentNode.getNode(childNodeName);
    	assertNotNull(childNode);
    	assertEquals(childNodeName, childNode.getName());
    	assertEquals(childNodeValue, childNode.getValue());
    }

    public void testEquals() {
    	Node node1 = new Node();
    	Node node2 = new Node();

    	assertTrue(node1.equals(node2));
    	assertTrue(node2.equals(node1));
    	assertFalse(node1.equals(null));
    	assertFalse(node2.equals(null));

    	String childNodeName;
    	String childNodeValue;
    	
    	childNodeName = "cnode";
    	childNodeValue = "cvalue";
    	
    	node1.setName(childNodeName);
    	assertFalse(node1.equals(node2));
    	assertFalse(node2.equals(node1));

    	node1.setValue(childNodeValue);
    	assertFalse(node1.equals(node2));
    	assertFalse(node2.equals(node1));

    	node2.setName(childNodeName);
    	assertFalse(node1.equals(node2));
    	assertFalse(node2.equals(node1));

    	node2.setValue(childNodeValue);
    	assertTrue(node1.equals(node2));
    	assertTrue(node2.equals(node1));
    }
}
