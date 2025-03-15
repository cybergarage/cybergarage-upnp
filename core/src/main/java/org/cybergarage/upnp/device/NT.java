/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: NT.java
 *
 *	Revision;
 *
 *	12/09/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.device;

public class NT {
  public static final String ROOTDEVICE = "upnp:rootdevice";
  public static final String EVENT = "upnp:event";

  public static final boolean isRootDevice(String ntValue) {
    if (ntValue == null) return false;
    return ntValue.startsWith(ROOTDEVICE);
  }
}
