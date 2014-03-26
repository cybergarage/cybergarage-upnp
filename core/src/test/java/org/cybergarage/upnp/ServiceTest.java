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

public class ServiceTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ServiceTest.class );
    }

    public void testServiceAbsoluteURL()
    {
    }
}
