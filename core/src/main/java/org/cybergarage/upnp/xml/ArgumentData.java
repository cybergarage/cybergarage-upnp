/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: ArgumentData.java
 *
 *	Revision;
 *
 *	02/24/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.xml;

/**
 * Stores user data associated with an argument XML node.
 *
 * <p>This class holds the runtime value of an argument, separate from the argument's definition in
 * the SCPD document.
 *
 * @see org.cybergarage.upnp.Argument
 */
public class ArgumentData extends NodeData {
  /** Constructs an empty argument data object. */
  public ArgumentData() {}

  ////////////////////////////////////////////////
  // value
  ////////////////////////////////////////////////

  private String value = "";

  /**
   * Returns the value of this argument.
   *
   * @return the argument value as a string
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of this argument.
   *
   * @param value the argument value to set
   */
  public void setValue(String value) {
    this.value = value;
  }
}
