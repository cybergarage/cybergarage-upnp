/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ActionListener.java
 *
 *	Revision;
 *
 *	01/16/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.upnp.*;

/**
 * Listener interface for handling incoming UPnP action requests.
 * 
 * <p>Implementations of this interface are registered with {@link Action}
 * instances to handle action invocations from control points. When a control
 * point invokes an action, the device calls this listener to execute the
 * application-specific logic.
 * 
 * <p>Example implementation:
 * <pre>{@code
 * action.setActionListener(new ActionListener() {
 *     public boolean actionControlReceived(Action action) {
 *         String volume = action.getArgumentValue("Volume");
 *         // Process the action and set output arguments
 *         action.setArgumentValue("Result", "OK");
 *         return true; // Success
 *     }
 * });
 * }</pre>
 * 
 * @see Action
 * @see Action#setActionListener(ActionListener)
 */
public interface ActionListener {
  /**
   * Called when an action control request is received from a control point.
   * 
   * <p>Implementations should:
   * <ul>
   *   <li>Read input argument values from the action</li>
   *   <li>Perform the requested operation</li>
   *   <li>Set output argument values in the action</li>
   *   <li>Return {@code true} if successful, {@code false} on error</li>
   * </ul>
   * 
   * <p>If this method returns {@code false}, the action's status should be
   * set using {@link Action#setStatus(int, String)} to provide error details
   * to the control point.
   * 
   * @param action the action being invoked, containing input arguments
   * @return {@code true} if the action was executed successfully,
   *         {@code false} if an error occurred
   */
  public boolean actionControlReceived(Action action);
}
