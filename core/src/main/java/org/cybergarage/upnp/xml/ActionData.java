/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: ActionData.java
 *
 *	Revision;
 *
 *	03/28/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.xml;

import org.cybergarage.upnp.control.*;

/**
 * Stores user data associated with an action XML node.
 * 
 * <p>This class holds runtime information attached to action nodes,
 * including the action listener and control response objects.
 * 
 * @see org.cybergarage.upnp.Action
 */
public class ActionData extends NodeData {
  /**
   * Constructs an empty action data object.
   */
  public ActionData() {}

  ////////////////////////////////////////////////
  // ActionListener
  ////////////////////////////////////////////////

  private ActionListener actionListener = null;

  /**
   * Returns the action listener associated with this action.
   * 
   * @return the action listener, or {@code null} if none is set
   */
  public ActionListener getActionListener() {
    return actionListener;
  }

  /**
   * Sets the action listener for this action.
   * 
   * @param actionListener the action listener to set
   */
  public void setActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  ////////////////////////////////////////////////
  // ControlResponse
  ////////////////////////////////////////////////

  private ControlResponse ctrlRes = null;

  /**
   * Returns the control response from the most recent action execution.
   * 
   * @return the control response, or {@code null} if the action has not been executed
   */
  public ControlResponse getControlResponse() {
    return ctrlRes;
  }

  /**
   * Sets the control response for this action.
   * 
   * @param res the control response to store
   */
  public void setControlResponse(ControlResponse res) {
    ctrlRes = res;
  }
}
