/******************************************************************
 *
 *	CyberLink for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2004
 *
 *	File: AllowedValue.java
 *
 *	Revision:
 *
 *	03/27/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

/**
 * Represents a single allowed value for a UPnP state variable.
 *
 * <p>State variables can define a list of allowed values that constrain the possible values the
 * variable can take. This class represents one such allowed value as defined in the service's SCPD
 * document.
 *
 * @see AllowedValueList
 * @see StateVariable
 */
public class AllowedValue {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for allowed value nodes in SCPD documents. */
  public static final String ELEM_NAME = "allowedValue";

  ////////////////////////////////////////////////
  //	Member
  ////////////////////////////////////////////////

  private Node allowedValueNode;

  /**
   * Returns the underlying XML node representing this allowed value.
   *
   * @return the allowed value XML node
   */
  public Node getAllowedValueNode() {
    return allowedValueNode;
  }

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an allowed value from an existing XML node.
   *
   * @param node the XML node representing the allowed value
   */
  public AllowedValue(Node node) {
    allowedValueNode = node;
  }

  /**
   * Constructs an allowed value with the specified value string.
   *
   * <p>Creates a new XML node structure for the allowed value.
   *
   * @param value the value string to associate with this object
   * @author Stefano "Kismet" Lenzi - kismet-sl@users.sourceforge.net - 2005
   */
  public AllowedValue(String value) {

    // TODO Some test are done not stable
    allowedValueNode = new Node(ELEM_NAME); // better (twa)
    setValue(value); // better (twa)
  }

  ////////////////////////////////////////////////
  //	isAllowedValueNode
  ////////////////////////////////////////////////

  /**
   * Checks if the given XML node represents an allowed value element.
   *
   * @param node the XML node to check
   * @return {@code true} if the node's name matches the allowed value element name, {@code false}
   *     otherwise
   */
  public static boolean isAllowedValueNode(Node node) {
    return ELEM_NAME.equals(node.getName());
  }

  ////////////////////////////////////////////////
  //	Value
  ////////////////////////////////////////////////

  /**
   * Sets the value string for this allowed value.
   *
   * @param value the value to set
   */
  public void setValue(String value) {
    getAllowedValueNode().setValue(value);
  }

  /**
   * Returns the value string of this allowed value.
   *
   * @return the value string
   */
  public String getValue() {
    return getAllowedValueNode().getValue();
  }
}
