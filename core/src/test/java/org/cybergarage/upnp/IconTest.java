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

    /**
     * Non node icon
     */
    public void testConstructorWithoutNode()
    {
    	Icon icon = new Icon();
    	
    	String mimeType = "image/png";
    	assertEquals("", icon.getMimeType());
    	icon.setMimeType(mimeType);
    	assertEquals(mimeType, icon.getMimeType());

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
    	assertEquals("", icon.getURL());
    	icon.setURL(url);
    	assertEquals(url, icon.getURL());

    	byte data[] = new byte[1];
    	assertEquals(null, icon.getBytes());
    	icon.setBytes(data);
    	assertTrue(icon.getBytes() != null);
    }
}
