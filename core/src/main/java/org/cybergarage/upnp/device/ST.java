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
 *	01/07/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.device;

public class ST {
  public static final String ALL_DEVICE = "ssdp:all";
  public static final String ROOT_DEVICE = "upnp:rootdevice";
  public static final String UUID_DEVICE = "uuid";
  public static final String URN_DEVICE = "urn:schemas-upnp-org:device:";
  public static final String URN_SERVICE = "urn:schemas-upnp-org:service:";

  public static final boolean isAllDevice(String value) {
    if (value == null) return false;
    if (value.equals(ALL_DEVICE) == true) return true;
    return value.equals("\"" + ALL_DEVICE + "\"");
  }

  public static final boolean isRootDevice(String value) {
    if (value == null) return false;
    if (value.equals(ROOT_DEVICE) == true) return true;
    return value.equals("\"" + ROOT_DEVICE + "\"");
  }

  public static final boolean isUUIDDevice(String value) {
    if (value == null) return false;
    if (value.startsWith(UUID_DEVICE) == true) return true;
    return value.startsWith("\"" + UUID_DEVICE);
  }

  public static final boolean isURNDevice(String value) {
    if (value == null) return false;
    if (value.startsWith(URN_DEVICE) == true) return true;
    return value.startsWith("\"" + URN_DEVICE);
  }

  public static final boolean isURNService(String value) {
    if (value == null) return false;
    if (value.startsWith(URN_SERVICE) == true) return true;
    return value.startsWith("\"" + URN_SERVICE);
  }
}
