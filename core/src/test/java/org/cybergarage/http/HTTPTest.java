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

public class HTTPTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public HTTPTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(HTTPTest.class);
  }

  public void testToRelativeURL() {
    String urlString;

    urlString = HTTP.toRelativeURL("/testURL");
    assertTrue(urlString, urlString.equals("/testURL"));

    urlString = HTTP.toRelativeURL("testURL");
    assertTrue(urlString, urlString.equals("/testURL"));
  }

  public void testGetAbsoluteURL() {
    String urlString;

    urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80/", "/testURL");
    assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));

    urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80/", "testURL");
    assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));

    urlString = HTTP.getAbsoluteURL("http://192.168.0.1:80", "/testURL");
    assertTrue(urlString, urlString.equals("http://192.168.0.1:80/testURL"));
  }

  public void testGetValueFromEmptyPacket() {
    // Zero-length SSDP packets must not throw IllegalArgumentException (#13).
    assertEquals("", HTTPHeader.getValue("", "HOST"));
    assertEquals("", HTTPHeader.getValue(new byte[0], "HOST"));
    assertEquals("", HTTPHeader.getValue((byte[]) null, "HOST"));
    assertEquals(0, HTTPHeader.getIntegerValue(new byte[0], "MX"));
  }

  public void testGetValue() {
    String packet = "NOTIFY * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nNTS: ssdp:alive\r\n\r\n";
    assertEquals("239.255.255.250:1900", HTTPHeader.getValue(packet, "HOST"));
    assertEquals("ssdp:alive", HTTPHeader.getValue(packet.getBytes(), "nts"));
  }
}
