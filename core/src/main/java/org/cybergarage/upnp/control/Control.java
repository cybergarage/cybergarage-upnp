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

/**
 * Constants for UPnP control protocol operations.
 * 
 * <p>This class defines standard namespace prefixes, URNs, and element names
 * used in UPnP control messages (SOAP actions and state variable queries).
 */
public class Control {
  /**
   * Namespace prefix for UPnP control elements.
   */
  public static final String NS = "u";
  
  /**
   * SOAP action URN for querying state variables.
   */
  public static final String QUERY_SOAPACTION =
      "urn:schemas-upnp-org:control-1-0#QueryStateVariable";
  
  /**
   * XML namespace for UPnP control protocol version 1.0.
   */
  public static final String XMLNS = "urn:schemas-upnp-org:control-1-0";
  
  /**
   * Element name for state variable query requests.
   */
  public static final String QUERY_STATE_VARIABLE = "QueryStateVariable";
  
  /**
   * Element name for state variable query responses.
   */
  public static final String QUERY_STATE_VARIABLE_RESPONSE = "QueryStateVariableResponse";
  
  /**
   * Element name for the variable name parameter in queries.
   */
  public static final String VAR_NAME = "varName";
  
  /**
   * Element name for the return value in query responses.
   */
  public static final String RETURN = "return";
}
