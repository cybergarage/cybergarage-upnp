/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: NTS.java
 *
 *	Revision;
 *
 *	12/09/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.device;

public class NTS {
  public static final String ALIVE = "ssdp:alive";
  public static final String BYEBYE = "ssdp:byebye";
  public static final String PROPCHANGE = "upnp:propchange";

  public static final boolean isAlive(String ntsValue) {
    if (ntsValue == null) return false;
    return ntsValue.startsWith(NTS.ALIVE);
  }

  public static final boolean isByeBye(String ntsValue) {
    if (ntsValue == null) return false;
    return ntsValue.startsWith(NTS.BYEBYE);
  }
}
