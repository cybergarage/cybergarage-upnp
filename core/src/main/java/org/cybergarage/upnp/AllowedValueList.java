/******************************************************************
 *
 *	CyberLink for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2004
 *
 *	File: AllowedValueList.java
 *
 *	Revision:
 *
 *	03/27/04
 *		- first revision.
 *	02/28/05
 *		- Changed to use AllowedValue instead of String as the member.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import java.util.Iterator;
import java.util.Vector;

/**
 * A list of {@link AllowedValue} objects for a state variable.
 *
 * <p>This class represents the set of allowed values that constrain a UPnP state variable. It
 * extends {@link Vector} to provide type-safe access to allowed values and includes validation
 * methods.
 *
 * @see AllowedValue
 * @see StateVariable
 */
public class AllowedValueList extends Vector {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for allowed value list nodes in SCPD documents. */
  public static final String ELEM_NAME = "allowedValueList";

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /** Constructs an empty allowed value list. */
  public AllowedValueList() {}

  /**
   * Constructs an allowed value list from an array of strings.
   *
   * @param values the array of allowed value strings
   */
  public AllowedValueList(String[] values) {
    for (int i = 0; i < values.length; i++) {
      add(new AllowedValue(values[i]));
    }
    ;
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  /**
   * Returns the allowed value at the specified index.
   *
   * @param n the index of the allowed value to retrieve
   * @return the allowed value at the specified index
   * @throws ArrayIndexOutOfBoundsException if the index is out of range
   */
  public AllowedValue getAllowedValue(int n) {
    return (AllowedValue) get(n);
  }

  /**
   * Checks if the specified value is in the allowed value list.
   *
   * @param v the value string to check
   * @return {@code true} if the value is allowed, {@code false} otherwise
   */
  public boolean isAllowed(String v) {
    for (Iterator i = this.iterator(); i.hasNext(); ) {
      AllowedValue av = (AllowedValue) i.next();
      if (av.getValue().equals(v)) return true;
    }
    return false;
  }
}
