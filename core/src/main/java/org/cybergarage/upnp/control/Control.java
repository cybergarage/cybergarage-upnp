/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Control.java
 *
 *	Revision;
 *
 *	01/20/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

public class Control {
  public static final String NS = "u";
  public static final String QUERY_SOAPACTION =
      "urn:schemas-upnp-org:control-1-0#QueryStateVariable";
  public static final String XMLNS = "urn:schemas-upnp-org:control-1-0";
  public static final String QUERY_STATE_VARIABLE = "QueryStateVariable";
  public static final String QUERY_STATE_VARIABLE_RESPONSE = "QueryStateVariableResponse";
  public static final String VAR_NAME = "varName";
  public static final String RETURN = "return";
}
