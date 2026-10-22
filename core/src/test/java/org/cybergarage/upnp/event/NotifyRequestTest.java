/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2026
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp.event;

import junit.framework.TestCase;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.upnp.UPnP;

public class NotifyRequestTest extends TestCase {

  protected void setUp() {
    // Loads the default XML parser and registers it for SOAP messages too.
    UPnP.getXMLParser();
  }

  private static NotifyRequest newNotifyRequest(String content) {
    HTTPRequest httpReq = new HTTPRequest();
    httpReq.setMethod("NOTIFY");
    httpReq.setContent(content);
    return new NotifyRequest(httpReq);
  }

  public void testGetPropertyListWithEmptyBody() {
    // An empty or unparsable NOTIFY body must not throw NullPointerException (#26).
    PropertyList props = newNotifyRequest("").getPropertyList();
    assertNotNull(props);
    assertEquals(0, props.size());
  }

  public void testGetPropertyListWithInvalidBody() {
    PropertyList props = newNotifyRequest("<e:propertyset").getPropertyList();
    assertNotNull(props);
    assertEquals(0, props.size());
  }

  public void testGetPropertyList() {
    String content =
        "<?xml version=\"1.0\"?>"
            + "<e:propertyset xmlns:e=\"urn:schemas-upnp-org:event-1-0\">"
            + "<e:property><Volume>10</Volume></e:property>"
            + "<e:property><Mute>0</Mute></e:property>"
            + "</e:propertyset>";
    PropertyList props = newNotifyRequest(content).getPropertyList();
    assertEquals(2, props.size());
    assertEquals("Volume", props.getProperty(0).getName());
    assertEquals("10", props.getProperty(0).getValue());
    assertEquals("Mute", props.getProperty(1).getName());
    assertEquals("0", props.getProperty(1).getValue());
  }
}
