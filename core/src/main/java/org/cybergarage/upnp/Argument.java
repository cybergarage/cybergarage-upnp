/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Argument.java
 *
 *	Revision;
 *
 *	12/05/02
 *		- first revision.
 *	03/28/04
 *		- Added getRelatedStateVariable().
 *		- Changed setRelatedStateVariable() to setRelatedStateVariableName().
 *		- Changed getRelatedStateVariable() to getRelatedStateVariableName().
 *		- Added getActionNode() and getAction().
 *		- Added getServiceNode() and getService().
 *		- Added the parent service node to the constructor.
 *	04/12/06
 *		- Added setUserData() and getUserData() to set a user original data object.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.upnp.xml.ArgumentData;
import org.cybergarage.xml.Node;

/**
 * Represents an argument of a UPnP action.
 * 
 * <p>Arguments are the parameters passed to and returned from UPnP actions.
 * Each argument has:
 * <ul>
 *   <li>A name identifying the argument</li>
 *   <li>A direction (in or out) indicating if it's an input or output parameter</li>
 *   <li>A related state variable defining its data type and constraints</li>
 *   <li>A value holding the actual data</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Creating an input argument
 * Argument volumeArg = new Argument("DesiredVolume", "50");
 * 
 * // Checking direction
 * if (argument.isInDirection()) {
 *     String value = argument.getValue();
 * }
 * 
 * // Getting related state variable
 * StateVariable stateVar = argument.getRelatedStateVariable();
 * }</pre>
 * 
 * @see Action
 * @see StateVariable
 * @see ArgumentList
 */
public class Argument {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /**
   * The XML element name for argument nodes in SCPD documents.
   */
  public static final String ELEM_NAME = "argument";

  /**
   * Direction constant for input arguments.
   */
  public static final String IN = "in";
  
  /**
   * Direction constant for output arguments.
   */
  public static final String OUT = "out";

  ////////////////////////////////////////////////
  //	Member
  ////////////////////////////////////////////////

  private Node argumentNode;
  private Node serviceNode;

  /**
   * Returns the underlying XML node representing this argument.
   * 
   * @return the argument XML node
   */
  public Node getArgumentNode() {
    return argumentNode;
  }

  private Node getServiceNode() {
    return serviceNode;
  }

  /**
   * Returns the service that contains this argument.
   * 
   * @return the parent service of this argument
   */
  public Service getService() {
    return new Service(getServiceNode());
  }

  void setService(Service s) {
    s.getServiceNode();
  }

  /**
   * Returns the XML node representing the action that contains this argument.
   * 
   * @return the action XML node, or {@code null} if not found
   */
  public Node getActionNode() {
    Node argumentLinstNode = getArgumentNode().getParentNode();
    if (argumentLinstNode == null) return null;
    Node actionNode = argumentLinstNode.getParentNode();
    if (actionNode == null) return null;
    if (Action.isActionNode(actionNode) == false) return null;
    return actionNode;
  }

  /**
   * Returns the action that contains this argument.
   * 
   * @return the parent action of this argument
   */
  public Action getAction() {
    return new Action(getServiceNode(), getActionNode());
  }

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an empty argument.
   */
  public Argument() {
    argumentNode = new Node(ELEM_NAME);
    serviceNode = null;
  }

  /**
   * Constructs an argument associated with a service node.
   * 
   * @param servNode the XML node representing the parent service
   */
  public Argument(Node servNode) {
    argumentNode = new Node(ELEM_NAME);
    serviceNode = servNode;
  }

  /**
   * Constructs an argument from existing service and argument XML nodes.
   * 
   * @param servNode the XML node representing the parent service
   * @param argNode the XML node representing the argument definition
   */
  public Argument(Node servNode, Node argNode) {
    serviceNode = servNode;
    argumentNode = argNode;
  }

  /**
   * Constructs an argument with the specified name and value.
   * 
   * @param name the argument name
   * @param value the argument value
   */
  public Argument(String name, String value) {
    this();
    setName(name);
    setValue(value);
  }

  ////////////////////////////////////////////////
  //	isArgumentNode
  ////////////////////////////////////////////////

  /**
   * Checks if the given XML node represents an argument element.
   * 
   * @param node the XML node to check
   * @return {@code true} if the node's name matches the argument element name,
   *         {@code false} otherwise
   */
  public static boolean isArgumentNode(Node node) {
    return Argument.ELEM_NAME.equals(node.getName());
  }

  ////////////////////////////////////////////////
  //	name
  ////////////////////////////////////////////////

  private static final String NAME = "name";

  /**
   * Sets the name of this argument.
   * 
   * @param value the argument name
   */
  public void setName(String value) {
    getArgumentNode().setNode(NAME, value);
  }

  /**
   * Returns the name of this argument.
   * 
   * @return the argument name
   */
  public String getName() {
    return getArgumentNode().getNodeValue(NAME);
  }

  ////////////////////////////////////////////////
  //	direction
  ////////////////////////////////////////////////

  private static final String DIRECTION = "direction";

  /**
   * Sets the direction of this argument.
   * 
   * @param value the direction, either {@link #IN} or {@link #OUT}
   */
  public void setDirection(String value) {
    getArgumentNode().setNode(DIRECTION, value);
  }

  /**
   * Returns the direction of this argument.
   * 
   * @return the direction string, or {@code null} if not set
   */
  public String getDirection() {
    return getArgumentNode().getNodeValue(DIRECTION);
  }

  /**
   * Checks if this argument is an input parameter.
   * 
   * @return {@code true} if the direction is "in", {@code false} otherwise
   */
  public boolean isInDirection() {
    String dir = getDirection();
    if (dir == null) return false;
    return dir.equalsIgnoreCase(IN);
  }

  /**
   * Checks if this argument is an output parameter.
   * 
   * @return {@code true} if the direction is not "in", {@code false} otherwise
   */
  public boolean isOutDirection() {
    return !isInDirection();
  }

  ////////////////////////////////////////////////
  //	relatedStateVariable
  ////////////////////////////////////////////////

  private static final String RELATED_STATE_VARIABLE = "relatedStateVariable";

  /**
   * Sets the name of the related state variable.
   * 
   * <p>The related state variable defines the data type, range, and
   * constraints for this argument's value.
   * 
   * @param value the name of the related state variable
   */
  public void setRelatedStateVariableName(String value) {
    getArgumentNode().setNode(RELATED_STATE_VARIABLE, value);
  }

  /**
   * Returns the name of the related state variable.
   * 
   * @return the name of the related state variable
   */
  public String getRelatedStateVariableName() {
    return getArgumentNode().getNodeValue(RELATED_STATE_VARIABLE);
  }

  /**
   * Returns the related state variable object.
   * 
   * <p>Looks up the state variable by name in the parent service's
   * state table.
   * 
   * @return the related state variable, or {@code null} if not found
   *         or if the service is not available
   * @see StateVariable
   */
  public StateVariable getRelatedStateVariable() {
    Service service = getService();
    if (service == null) return null;
    String relatedStatVarName = getRelatedStateVariableName();
    return service.getStateVariable(relatedStatVarName);
  }

  ////////////////////////////////////////////////
  //	UserData
  ////////////////////////////////////////////////

  private ArgumentData getArgumentData() {
    Node node = getArgumentNode();
    ArgumentData userData = (ArgumentData) node.getUserData();
    if (userData == null) {
      userData = new ArgumentData();
      node.setUserData(userData);
      userData.setNode(node);
    }
    return userData;
  }

  ////////////////////////////////////////////////
  //	value
  ////////////////////////////////////////////////

  /**
   * Sets the value of this argument.
   * 
   * @param value the argument value as a string
   */
  public void setValue(String value) {
    getArgumentData().setValue(value);
  }

  /**
   * Sets the value of this argument from an integer.
   * 
   * <p>The integer is converted to a string representation.
   * 
   * @param value the argument value as an integer
   */
  public void setValue(int value) {
    setValue(Integer.toString(value));
  }

  /**
   * Returns the value of this argument.
   * 
   * @return the argument value as a string
   */
  public String getValue() {
    return getArgumentData().getValue();
  }

  /**
   * Returns the value of this argument as an integer.
   * 
   * <p>If the value cannot be parsed as an integer, returns 0.
   * 
   * @return the argument value as an integer, or 0 if parsing fails
   */
  public int getIntegerValue() {
    String value = getValue();
    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
    }
    return 0;
  }

  ////////////////////////////////////////////////
  //	userData
  ////////////////////////////////////////////////

  private Object userData = null;

  /**
   * Sets custom user data associated with this argument.
   * 
   * <p>Allows applications to attach arbitrary data to an argument
   * instance for application-specific purposes.
   * 
   * @param data the user data object to associate with this argument
   */
  public void setUserData(Object data) {
    userData = data;
  }

  /**
   * Returns the custom user data associated with this argument.
   * 
   * @return the user data object, or {@code null} if none has been set
   */
  public Object getUserData() {
    return userData;
  }
}
