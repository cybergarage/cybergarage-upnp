/******************************************************************
*
* CyberLink for Java
*
* Copyright (C) Satoshi Konno 2002-2012
*
* This is licensed under BSD-style license, see file COPYING.
*
******************************************************************/

package org.cybergarage.upnp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cybergarage.upnp.Icon;

public class IconTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public IconTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( IconTest.class );
    }

    public void testConstructorWithoutNode()
    {
    	Icon icon = new Icon();
    	
    	String mimeType = "image/png";
    	assertFalse(icon.hasMimeType());
    	assertEquals("", icon.getMimeType());
    	icon.setMimeType(mimeType);
    	assertEquals(mimeType, icon.getMimeType());
    	assertTrue(icon.hasMimeType());

    	int width = 640;
    	assertEquals(0, icon.getWidth());
    	icon.setWidth(width);
    	assertEquals(width, icon.getWidth());

    	int height = 480;
    	assertEquals(0, icon.getHeight());
    	icon.setHeight(height);
    	assertEquals(height, icon.getHeight());

    	int depth = 24;
    	assertEquals(0, icon.getDepth());
    	icon.setDepth(depth);
    	assertEquals(depth, icon.getDepth());

    	String url = "/icon";
    	assertFalse(icon.hasURL());
    	assertEquals("", icon.getURL());
    	icon.setURL(url);
    	assertTrue(icon.hasURL());
    	assertEquals(url, icon.getURL());
    	assertTrue(icon.isURL(url));

    	byte data[] = new byte[1];
    	assertEquals(null, icon.getBytes());
    	icon.setBytes(data);
    	assertTrue(icon.hasBytes());
    	assertTrue(icon.getBytes() != null);
    }

    public void testIconHasURL()
    {
    	Icon icon = new Icon();
    	
    	assertFalse(icon.hasURL());
    	
    	icon.setURL(null);
    	assertFalse(icon.hasURL());
    	
    	icon.setURL("");
    	assertFalse(icon.hasURL());
    	
    	icon.setURL("/icon");
    	assertTrue(icon.hasURL());
    }
}
