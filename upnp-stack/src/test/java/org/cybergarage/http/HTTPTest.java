/******************************************************************
*
* CyberLink for Java
*
* Copyright (C) Satoshi Konno 2002-2012
*
* This is licensed under BSD-style license, see file COPYING.
*
******************************************************************/

package org.cybergarage.http;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HTTPTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HTTPTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( HTTPTest.class );
    }

    public void testToRelativeURL()
    {
        String urlString;
        
        urlString = HTTP.toRelativeURL("/testURL");
        assertTrue(urlString, urlString.equals("/testURL"));
        
        urlString = HTTP.toRelativeURL("testURL");
        assertTrue(urlString, urlString.equals("/testURL"));
    }

    public void testGetAbsoluteURL()
    {
        String urlString;
        
        urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80/", "/testURL");
        assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));

        urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80/", "testURL");
        assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));
        
        urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80", "/testURL");
        assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));
    }
}
