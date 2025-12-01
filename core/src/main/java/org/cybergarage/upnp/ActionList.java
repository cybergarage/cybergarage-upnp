/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ActionList.java
 *
 *	Revision:
 *
 *	12/05/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import java.util.Vector;

/**
 * A list of {@link Action} objects.
 *
 * <p>This class extends {@link Vector} to provide type-safe access to UPnP actions within a
 * service.
 *
 * @see Action
 * @see Service
 */
public class ActionList extends Vector {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for action list nodes in service descriptions. */
  public static final String ELEM_NAME = "actionList";

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /** Constructs an empty action list. */
  public ActionList() {}

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  /**
   * Returns the action at the specified index.
   *
   * @param n the index of the action to retrieve
   * @return the action at the specified index
   * @throws ArrayIndexOutOfBoundsException if the index is out of range
   */
  public Action getAction(int n) {
    return (Action) get(n);
  }
}
