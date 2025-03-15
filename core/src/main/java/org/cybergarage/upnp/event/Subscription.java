/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: ST.java
 *
 *	Revision;
 *
 *	01/31/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.event;

import org.cybergarage.upnp.*;

public class Subscription {
  public static final String XMLNS = "urn:schemas-upnp-org:event-1-0";
  public static final String TIMEOUT_HEADER = "Second-";
  public static final String INFINITE_STRING = "infinite";
  public static final int INFINITE_VALUE = -1;
  public static final String UUID = "uuid:";
  public static final String SUBSCRIBE_METHOD = "SUBSCRIBE";
  public static final String UNSUBSCRIBE_METHOD = "UNSUBSCRIBE";

  ////////////////////////////////////////////////
  //	Timeout
  ////////////////////////////////////////////////

  public static final String toTimeoutHeaderString(long time) {
    if (time == Subscription.INFINITE_VALUE) return Subscription.INFINITE_STRING;
    return Subscription.TIMEOUT_HEADER + Long.toString(time);
  }

  public static final long getTimeout(String headerValue) {
    int minusIdx = headerValue.indexOf('-');
    long timeout = Subscription.INFINITE_VALUE;
    try {
      String timeoutStr = headerValue.substring(minusIdx + 1, headerValue.length());
      timeout = Long.parseLong(timeoutStr);
    } catch (Exception e) {
    }
    return timeout;
  }

  ////////////////////////////////////////////////
  //	SID
  ////////////////////////////////////////////////

  public static final String createSID() {
    return UPnP.createUUID();
  }

  public static final String toSIDHeaderString(String id) {
    return Subscription.UUID + id;
  }

  public static final String getSID(String headerValue) {
    if (headerValue == null) return "";
    if (headerValue.startsWith(Subscription.UUID) == false) return headerValue;
    return headerValue.substring(Subscription.UUID.length(), headerValue.length());
  }
}
