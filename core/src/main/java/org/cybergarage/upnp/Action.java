/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2004
 *
 *	File: Action.java
 *
 *	Revision;
 *
 *	12/05/02
 *		- first revision.
 *	08/30/03
 *		- Gordano Sassaroli <sassarol@cefriel.it>
 *		- Problem    : When invoking an action that has at least one out parameter, an error message is returned
 *		- Error      : The action post method gets the entire list of arguments instead of only the in arguments
 *	01/04/04
 *		- Added UPnP status methods.
 *		- Changed about new ActionListener interface.
 *	01/05/04
 *		- Added clearOutputAgumentValues() to initialize the output values before calling performActionListener().
 *	07/09/04
 *		- Thanks for Dimas <cyberrate@users.sourceforge.net> and Stefano Lenzi <kismet-sl@users.sourceforge.net>
 *		- Changed postControlAction() to set the status code to the UPnPStatus.
 *	04/12/06
 *		- Added setUserData() and getUserData() to set a user original data object.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import java.util.Iterator;

import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlResponse;
import org.cybergarage.upnp.xml.ActionData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;

/**
 * Represents a UPnP action that can be invoked on a service.
 *
 * <p>Actions are methods exposed by UPnP services that can be called remotely by control points.
 * Each action has a name, a list of input arguments, and a list of output arguments. Actions are
 * defined in the service's SCPD (Service Control Protocol Definition) document.
 *
 * <p>This class provides methods to:
 *
 * <ul>
 *   <li>Set and retrieve action arguments
 *   <li>Invoke the action on a remote device via {@link #postControlAction()}
 *   <li>Handle incoming action requests from control points via {@link ActionListener}
 *   <li>Access action execution status and response data
 * </ul>
 *
 * <p>Thread-safety: This class uses internal synchronization via {@link #lock()} and {@link
 * #unlock()} methods. Multiple threads can safely interact with the same action instance when using
 * these synchronization methods.
 *
 * <p>Example usage for invoking an action on a device:
 *
 * <pre>{@code
 * Service service = device.getService("urn:schemas-upnp-org:service:AVTransport:1");
 * Action playAction = service.getAction("Play");
 * playAction.setArgumentValue("InstanceID", "0");
 * playAction.setArgumentValue("Speed", "1");
 * boolean success = playAction.postControlAction();
 * if (success) {
 *     System.out.println("Play action executed successfully");
 * }
 * }</pre>
 *
 * @see Service
 * @see Argument
 * @see ActionListener
 */
public class Action {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for action nodes in SCPD documents. */
  public static final String ELEM_NAME = "action";

  ////////////////////////////////////////////////
  //	Member
  ////////////////////////////////////////////////

  private Node serviceNode;
  private Node actionNode;

  private Node getServiceNode() {
    return serviceNode;
  }

  /**
   * Returns the service that contains this action.
   *
   * @return the parent service of this action
   */
  public Service getService() {
    return new Service(getServiceNode());
  }

  void setService(Service s) {
    serviceNode = s.getServiceNode();
    /*To ensure integrity of the XML structure*/
    Iterator i = getArgumentList().iterator();
    while (i.hasNext()) {
      Argument arg = (Argument) i.next();
      arg.setService(s);
    }
  }

  /**
   * Returns the underlying XML node representing this action.
   *
   * @return the action XML node
   */
  public Node getActionNode() {
    return actionNode;
  }

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs a new action with the specified service node.
   *
   * <p>Creates an empty action node that can be populated with action details.
   *
   * @param serviceNode the XML node representing the service that contains this action
   */
  public Action(Node serviceNode) {
    // TODO Test
    this.serviceNode = serviceNode;
    this.actionNode = new Node(Action.ELEM_NAME);
  }

  /**
   * Constructs an action from existing service and action XML nodes.
   *
   * <p>This constructor is typically used when parsing service descriptions to create action
   * objects from the SCPD document.
   *
   * @param serviceNode the XML node representing the parent service
   * @param actionNode the XML node containing action definition and arguments
   */
  public Action(Node serviceNode, Node actionNode) {
    this.serviceNode = serviceNode;
    this.actionNode = actionNode;
  }

  /**
   * Creates a copy of an existing action.
   *
   * <p>Note: This creates a shallow copy that references the same underlying XML nodes as the
   * original action.
   *
   * @param action the action to copy
   */
  public Action(Action action) {
    this.serviceNode = action.getServiceNode();
    this.actionNode = action.getActionNode();
  }

  ////////////////////////////////////////////////
  // Mutex
  ////////////////////////////////////////////////

  private Mutex mutex = new Mutex();

  /**
   * Acquires the lock for this action.
   *
   * <p>Use this method to synchronize access to the action from multiple threads. Always pair with
   * {@link #unlock()} to release the lock, preferably in a try-finally block.
   *
   * @see #unlock()
   */
  public void lock() {
    mutex.lock();
  }

  /**
   * Releases the lock for this action.
   *
   * <p>Should be called after {@link #lock()} once the synchronized operation is complete.
   *
   * @see #lock()
   */
  public void unlock() {
    mutex.unlock();
  }

  ////////////////////////////////////////////////
  //	isActionNode
  ////////////////////////////////////////////////

  /**
   * Checks if the given XML node represents an action element.
   *
   * @param node the XML node to check
   * @return {@code true} if the node's name matches the action element name, {@code false}
   *     otherwise
   */
  public static boolean isActionNode(Node node) {
    return Action.ELEM_NAME.equals(node.getName());
  }

  ////////////////////////////////////////////////
  //	name
  ////////////////////////////////////////////////

  private static final String NAME = "name";

  /**
   * Sets the name of this action.
   *
   * @param value the action name (e.g., "Play", "SetVolume")
   */
  public void setName(String value) {
    getActionNode().setNode(NAME, value);
  }

  /**
   * Returns the name of this action.
   *
   * @return the action name as defined in the SCPD document
   */
  public String getName() {
    return getActionNode().getNodeValue(NAME);
  }

  ////////////////////////////////////////////////
  //	argumentList
  ////////////////////////////////////////////////

  /**
   * Returns the complete list of arguments for this action.
   *
   * <p>This includes both input and output arguments as defined in the service's SCPD document.
   *
   * @return the list of all arguments for this action
   * @see #getInputArgumentList()
   * @see #getOutputArgumentList()
   */
  public ArgumentList getArgumentList() {
    ArgumentList argumentList = new ArgumentList();
    Node argumentListNode = getActionNode().getNode(ArgumentList.ELEM_NAME);
    if (argumentListNode == null) return argumentList;
    int nodeCnt = argumentListNode.getNNodes();
    for (int n = 0; n < nodeCnt; n++) {
      Node node = argumentListNode.getNode(n);
      if (Argument.isArgumentNode(node) == false) continue;
      Argument argument = new Argument(getServiceNode(), node);
      argumentList.add(argument);
    }
    return argumentList;
  }

  /**
   * Sets the argument list for this action.
   *
   * <p>Replaces the existing argument list with the provided list. This method ensures that all
   * arguments are properly associated with the action's service.
   *
   * @param al the argument list to set
   */
  public void setArgumentList(ArgumentList al) {
    Node argumentListNode = getActionNode().getNode(ArgumentList.ELEM_NAME);
    if (argumentListNode == null) {
      argumentListNode = new Node(ArgumentList.ELEM_NAME);
      getActionNode().addNode(argumentListNode);
    } else {
      argumentListNode.removeAllNodes();
    }
    Iterator i = al.iterator();
    while (i.hasNext()) {
      Argument a = (Argument) i.next();
      a.setService(getService());
      argumentListNode.addNode(a.getArgumentNode());
    }
  }

  /**
   * Returns only the input arguments for this action.
   *
   * <p>Input arguments are those with direction "in" and must be provided when invoking the action
   * on a device.
   *
   * @return the list of input arguments
   * @see #getOutputArgumentList()
   */
  public ArgumentList getInputArgumentList() {
    ArgumentList allArgList = getArgumentList();
    int allArgCnt = allArgList.size();
    ArgumentList argList = new ArgumentList();
    for (int n = 0; n < allArgCnt; n++) {
      Argument arg = allArgList.getArgument(n);
      if (arg.isInDirection() == false) continue;
      argList.add(arg);
    }
    return argList;
  }

  /**
   * Returns only the output arguments for this action.
   *
   * <p>Output arguments are those with direction "out" and are populated with values returned by
   * the device after action execution.
   *
   * @return the list of output arguments
   * @see #getInputArgumentList()
   */
  public ArgumentList getOutputArgumentList() {
    ArgumentList allArgList = getArgumentList();
    int allArgCnt = allArgList.size();
    ArgumentList argList = new ArgumentList();
    for (int n = 0; n < allArgCnt; n++) {
      Argument arg = allArgList.getArgument(n);
      if (arg.isOutDirection() == false) continue;
      argList.add(arg);
    }
    return argList;
  }

  /**
   * Returns the argument with the specified name.
   *
   * @param name the name of the argument to retrieve
   * @return the argument with the specified name, or {@code null} if not found
   */
  public Argument getArgument(String name) {
    ArgumentList argList = getArgumentList();
    int nArgs = argList.size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = argList.getArgument(n);
      String argName = arg.getName();
      if (argName == null) continue;
      if (name.equals(argName) == true) return arg;
    }
    return null;
  }

  /**
   * Sets argument values from the provided list.
   *
   * @param argList the argument list containing values to set
   * @deprecated You should use one of the following methods instead:<br>
   *     - {@link #setInArgumentValues(ArgumentList)} <br>
   *     - {@link #setOutArgumentValues(ArgumentList)}
   */
  public void setArgumentValues(ArgumentList argList) {
    getArgumentList().set(argList);
  }

  /**
   * Sets the values of input arguments from the provided argument list.
   *
   * <p>Only the values of arguments with direction "in" are updated.
   *
   * @param argList the argument list containing values to set
   * @since 1.8.0
   */
  public void setInArgumentValues(ArgumentList argList) {
    getArgumentList().setReqArgs(argList);
  }

  /**
   * Sets the values of output arguments from the provided argument list.
   *
   * <p>Only the values of arguments with direction "out" are updated.
   *
   * @param argList the argument list containing values to set
   * @since 1.8.0
   */
  public void setOutArgumentValues(ArgumentList argList) {
    getArgumentList().setResArgs(argList);
  }

  /**
   * Sets the value of a named argument.
   *
   * <p>If the argument with the specified name does not exist, this method does nothing.
   *
   * @param name the name of the argument to set
   * @param value the string value to assign to the argument
   */
  public void setArgumentValue(String name, String value) {
    Argument arg = getArgument(name);
    if (arg == null) return;
    arg.setValue(value);
  }

  /**
   * Sets the value of a named argument from an integer.
   *
   * <p>The integer value is converted to a string before being assigned. If the argument with the
   * specified name does not exist, this method does nothing.
   *
   * @param name the name of the argument to set
   * @param value the integer value to assign to the argument
   */
  public void setArgumentValue(String name, int value) {
    setArgumentValue(name, Integer.toString(value));
  }

  private void clearOutputAgumentValues() {
    ArgumentList allArgList = getArgumentList();
    int allArgCnt = allArgList.size();
    for (int n = 0; n < allArgCnt; n++) {
      Argument arg = allArgList.getArgument(n);
      if (arg.isOutDirection() == false) continue;
      arg.setValue("");
    }
  }

  /**
   * Returns the string value of the specified argument.
   *
   * @param name the name of the argument
   * @return the argument value as a string, or an empty string if the argument is not found
   */
  public String getArgumentValue(String name) {
    Argument arg = getArgument(name);
    if (arg == null) return "";
    return arg.getValue();
  }

  /**
   * Returns the integer value of the specified argument.
   *
   * @param name the name of the argument
   * @return the argument value as an integer, or 0 if the argument is not found
   */
  public int getArgumentIntegerValue(String name) {
    Argument arg = getArgument(name);
    if (arg == null) return 0;
    return arg.getIntegerValue();
  }

  ////////////////////////////////////////////////
  //	UserData
  ////////////////////////////////////////////////

  private ActionData getActionData() {
    Node node = getActionNode();
    ActionData userData = (ActionData) node.getUserData();
    if (userData == null) {
      userData = new ActionData();
      node.setUserData(userData);
      userData.setNode(node);
    }
    return userData;
  }

  ////////////////////////////////////////////////
  //	controlAction
  ////////////////////////////////////////////////

  /**
   * Returns the action listener registered for this action.
   *
   * @return the action listener, or {@code null} if none is registered
   */
  public ActionListener getActionListener() {
    return getActionData().getActionListener();
  }

  /**
   * Sets the listener that will be notified when this action is invoked.
   *
   * <p>The listener's {@link ActionListener#actionControlReceived(Action)} method will be called
   * when a control point invokes this action.
   *
   * @param listener the action listener to set, or {@code null} to remove the listener
   * @see ActionListener
   */
  public void setActionListener(ActionListener listener) {
    getActionData().setActionListener(listener);
  }

  /**
   * Executes the action listener for an incoming action request.
   *
   * <p>This method is called by the UPnP device when a control point invokes this action. It calls
   * the registered {@link ActionListener}, collects the response, and posts it back to the control
   * point.
   *
   * @param actionReq the incoming action request from the control point
   * @return {@code true} if a listener was registered and executed, {@code false} if no listener is
   *     available
   */
  public boolean performActionListener(ActionRequest actionReq) {
    ActionListener listener = (ActionListener) getActionListener();
    if (listener == null) return false;
    ActionResponse actionRes = new ActionResponse();
    setStatus(UPnPStatus.INVALID_ACTION);
    clearOutputAgumentValues();
    if (listener.actionControlReceived(this) == true) {
      actionRes.setResponse(this);
    } else {
      UPnPStatus upnpStatus = getStatus();
      actionRes.setFaultResponse(upnpStatus.getCode(), upnpStatus.getDescription());
    }
    if (Debug.isOn() == true) actionRes.print();
    actionReq.post(actionRes);
    return true;
  }

  ////////////////////////////////////////////////
  //	ActionControl
  ////////////////////////////////////////////////

  private ControlResponse getControlResponse() {
    return getActionData().getControlResponse();
  }

  private void setControlResponse(ControlResponse res) {
    getActionData().setControlResponse(res);
  }

  /**
   * Returns the UPnP error status from the most recent control response.
   *
   * @return the UPnP status from the control response
   */
  public UPnPStatus getControlStatus() {
    return getControlResponse().getUPnPError();
  }

  ////////////////////////////////////////////////
  //	postControlAction
  ////////////////////////////////////////////////

  /**
   * Posts this action to the device and waits for the response.
   *
   * <p>This method sends a SOAP action request to the device's control URL with the currently set
   * input argument values. It blocks until a response is received or a timeout occurs. Upon
   * successful execution, the output argument values are updated with the response data.
   *
   * <p>The execution status can be retrieved via {@link #getStatus()} after this method returns.
   *
   * @return {@code true} if the action was executed successfully and the response was valid, {@code
   *     false} if an error occurred
   * @see #setArgumentValue(String, String)
   * @see #getArgumentValue(String)
   * @see #getStatus()
   */
  public boolean postControlAction() {
    // Thanks for Giordano Sassaroli <sassarol@cefriel.it> (08/30/03)
    ArgumentList actionArgList = getArgumentList();
    ArgumentList actionInputArgList = getInputArgumentList();
    ActionRequest ctrlReq = new ActionRequest();
    ctrlReq.setRequest(this, actionInputArgList);
    if (Debug.isOn() == true) ctrlReq.print();
    ActionResponse ctrlRes = ctrlReq.post();
    if (Debug.isOn() == true) ctrlRes.print();
    setControlResponse(ctrlRes);
    // Thanks for Dimas <cyberrate@users.sourceforge.net> and Stefano Lenzi
    // <kismet-sl@users.sourceforge.net> (07/09/04)
    int statCode = ctrlRes.getStatusCode();
    setStatus(statCode);
    if (ctrlRes.isSuccessful() == false) return false;
    ArgumentList outArgList = ctrlRes.getResponse();
    try {
      actionArgList.setResArgs(outArgList);
    } catch (IllegalArgumentException ex) {
      setStatus(
          UPnPStatus.INVALID_ARGS, "Action succesfully delivered but invalid arguments returned.");
      return false;
    }
    return true;
  }

  ////////////////////////////////////////////////
  //	Debug
  ////////////////////////////////////////////////

  /**
   * Prints action information to standard output for debugging.
   *
   * <p>Outputs the action name and all argument details including direction, name, and current
   * value.
   */
  public void print() {
    System.out.println("Action : " + getName());
    ArgumentList argList = getArgumentList();
    int nArgs = argList.size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = argList.getArgument(n);
      String name = arg.getName();
      String value = arg.getValue();
      String dir = arg.getDirection();
      System.out.println(" [" + n + "] = " + dir + ", " + name + ", " + value);
    }
  }

  ////////////////////////////////////////////////
  //	UPnPStatus
  ////////////////////////////////////////////////

  private UPnPStatus upnpStatus = new UPnPStatus();

  /**
   * Sets the execution status of this action with a specific error code and description.
   *
   * @param code the UPnP error code
   * @param descr the human-readable description of the error
   * @see UPnPStatus
   */
  public void setStatus(int code, String descr) {
    upnpStatus.setCode(code);
    upnpStatus.setDescription(descr);
  }

  /**
   * Sets the execution status of this action with a specific error code.
   *
   * <p>The description is automatically derived from the error code.
   *
   * @param code the UPnP error code
   * @see UPnPStatus
   */
  public void setStatus(int code) {
    setStatus(code, UPnPStatus.code2String(code));
  }

  /**
   * Returns the current execution status of this action.
   *
   * <p>The status includes error codes and descriptions from the most recent action execution.
   *
   * @return the UPnP status object containing execution results
   * @see UPnPStatus
   */
  public UPnPStatus getStatus() {
    return upnpStatus;
  }

  ////////////////////////////////////////////////
  //	userData
  ////////////////////////////////////////////////

  private Object userData = null;

  /**
   * Sets custom user data associated with this action.
   *
   * <p>This allows applications to attach arbitrary data to an action instance for
   * application-specific purposes.
   *
   * @param data the user data object to associate with this action
   */
  public void setUserData(Object data) {
    userData = data;
  }

  /**
   * Returns the custom user data associated with this action.
   *
   * @return the user data object, or {@code null} if none has been set
   */
  public Object getUserData() {
    return userData;
  }
}
