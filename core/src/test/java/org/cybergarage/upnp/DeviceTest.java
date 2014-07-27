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

import org.cybergarage.xml.Node;

public class DeviceTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DeviceTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DeviceTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testDeviceAbsoluteURL()
    {
        Device dev = new Device();
        String devAbsUrl;
        
        /********************************************************************************
        * O:serviceURLStr ?:baseURLStr ?:locationURLStr
        ********************************************************************************/
        
        // O:serviceURLStr -:baseURLStr -:locationURLStr
        devAbsUrl = dev.getAbsoluteURL("http://192.168.0.1:80/serviceURL", null, null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.1:80/serviceURL"));

        /* O:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("http://192.168.0.1:80/serviceURL", "http://192.168.0.2:80/", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.1:80/serviceURL"));

        /* O:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("http://192.168.0.1:80/serviceURL", null, "http://192.168.0.3:80/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.1:80/serviceURL"));

        /* O:serviceURLStr O:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("http://192.168.0.1:80/serviceURL", "http://192.168.0.2:80/", "http://192.168.0.3:80/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.1:80/serviceURL"));

        /********************************************************************************
        * X:serviceURLStr X:baseURLStr X:locationURLStr
        ********************************************************************************/
        
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", null, null);
        assertTrue(devAbsUrl, devAbsUrl.equals("/serviceURL"));

        /********************************************************************************
        * X:serviceURLStr O:baseURLStr -:locationURLStr (CASE01)
        ********************************************************************************/
        
        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", "http://192.168.0.2:80/", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/serviceURL"));

        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", "http://192.168.0.2:80", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/serviceURL"));

        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", "http://192.168.0.2:80/", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/serviceURL"));

        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", "http://192.168.0.2:80", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/serviceURL"));
        
        /********************************************************************************
        * X:serviceURLStr O:baseURLStr -:locationURLStr (CASE02)
        ********************************************************************************/
    
        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", "http://192.168.0.2:80/device/", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/device/serviceURL"));
    
        /* X:serviceURLStr O:baseURLStr -:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", "http://192.168.0.2:80/device/", null);
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.2:80/device/serviceURL"));

        /********************************************************************************
        * X:serviceURLStr -:baseURLStr O:locationURLStr (CASE01)
        ********************************************************************************/
    
        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", null, "http://192.168.0.3:80/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/serviceURL"));

        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", null, "http://192.168.0.3:80");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/serviceURL"));

        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", null, "http://192.168.0.3:80/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/serviceURL"));

        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", null, "http://192.168.0.3:80");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/serviceURL"));

        /********************************************************************************
        * X:serviceURLStr -:baseURLStr O:locationURLStr (CASE02)
        ********************************************************************************/
    
        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("/serviceURL", null, "http://192.168.0.3:80/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/serviceURL"));

        /* X:serviceURLStr -:baseURLStr O:locationURLStr */
        devAbsUrl = dev.getAbsoluteURL("serviceURL", null, "http://192.168.0.3:80/device/");
        assertTrue(devAbsUrl, devAbsUrl.equals("http://192.168.0.3:80/device/serviceURL"));    
    }

    /**
     * Internal Id Generation Test
     */
    public void testInternalStaticIds()
    {
        Device dev = new Device();
        
        String devUUID = dev.getUUID();
        assertTrue(0 < devUUID.length());

        int bootId = dev.getBootId();
        assertTrue(0 <= bootId);
    }

    /**
     * Device Icon
     */
    public void testAddIconNode()
    {
        Icon icon = new Icon();
        
        int width = 64;
        icon.setWidth(width);
        assertEquals(icon.getWidth(), width);
        
        int height = 48;
        icon.setHeight(height);
        assertEquals(icon.getHeight(), height);

        int depth = 24;
        icon.setDepth(depth);
        assertEquals(icon.getDepth(), depth);
        
        String url = "/icon?num=1";
        icon.setURL(url);
        assertEquals(icon.getURL(), url);
        
        String image = "PNG";
        assertNull(icon.getBytes());
        icon.setBytes(image.getBytes());
        assertNotNull(icon.getBytes());
        
        Device dev = new Device(new Node());
        IconList iconList = dev.getIconList();
        assertEquals(iconList.size(), 0);
        
        assertTrue(dev.addIcon(icon));
        iconList = dev.getIconList();
        assertEquals(iconList.size(), 1);
        
        Icon devIcon = iconList.getIcon(0);
        assertEquals(devIcon.getWidth(), width);
        assertEquals(devIcon.getHeight(), height);
        assertEquals(devIcon.getDepth(), depth);
        assertEquals(devIcon.getURL(), url);
        assertNotNull(devIcon.getBytes());
    }
}
