/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ControlRequest.java
 *
 *	Revision;
 *
 *	01/29/03
 *		- first revision.
 *	05/09/05
 *		- Changed getActionName() to return when the delimiter is not found.
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.http.*;
import org.cybergarage.xml.*;
import org.cybergarage.soap.*;

import org.cybergarage.upnp.*;

/**
 * Represents a UPnP action request sent from a control point to a device.
 *
 * <p>This class encapsulates a SOAP-formatted action invocation request. It is used by control
 * points to invoke actions on remote UPnP devices and by devices to parse incoming action requests.
 *
 * <p>The request contains:
 *
 * <ul>
 *   <li>The action name and service type
 *   <li>Input argument values
 *   <li>HTTP and SOAP headers
 * </ul>
 *
 * @see ActionResponse
 * @see Action
 * @see ControlRequest
 */
public class ActionRequest extends ControlRequest {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /** Constructs an empty action request. */
  public ActionRequest() {}

  /**
   * Constructs an action request from an HTTP request.
   *
   * @param httpReq the HTTP request containing the SOAP action message
   */
  public ActionRequest(HTTPRequest httpReq) {
    set(httpReq);
  }

  ////////////////////////////////////////////////
  //	Action
  ////////////////////////////////////////////////

  /**
   * Returns the XML node representing the action element.
   *
   * <p>The action node is the first child of the SOAP body node and contains the action name and
   * argument values.
   *
   * @return the action XML node, or {@code null} if not present
   */
  public Node getActionNode() {
    Node bodyNode = getBodyNode();
    if (bodyNode == null) return null;
    if (bodyNode.hasNodes() == false) return null;
    return bodyNode.getNode(0);
  }

  /**
   * Returns the name of the action being invoked.
   *
   * <p>Extracts the action name from the SOAP action node, removing any namespace prefix.
   *
   * @return the action name, or an empty string if not found
   */
  public String getActionName() {
    Node node = getActionNode();
    if (node == null) return "";
    String name = node.getName();
    if (name == null) return "";
    int idx = name.indexOf(SOAP.DELIM) + 1;
    if (idx < 0) return "";
    return name.substring(idx, name.length());
  }

  /**
   * Returns the list of arguments from the action request.
   *
   * <p>Parses the action node to extract argument names and values.
   *
   * @return the list of arguments contained in the request
   */
  public ArgumentList getArgumentList() {
    Node actNode = getActionNode();
    int nArgNodes = actNode.getNNodes();
    ArgumentList argList = new ArgumentList();
    for (int n = 0; n < nArgNodes; n++) {
      Argument arg = new Argument();
      Node argNode = actNode.getNode(n);
      arg.setName(argNode.getName());
      arg.setValue(argNode.getValue());
      argList.add(arg);
    }
    return argList;
  }

  ////////////////////////////////////////////////
  //	setRequest
  ////////////////////////////////////////////////

  /**
   * Constructs the action request from an action and its arguments.
   *
   * <p>Creates a SOAP envelope with the action name, service type, and argument values properly
   * formatted for transmission to the device.
   *
   * @param action the action to be invoked
   * @param argList the list of arguments to include in the request
   */
  public void setRequest(Action action, ArgumentList argList) {
    Service service = action.getService();

    setRequestHost(service);

    setEnvelopeNode(SOAP.createEnvelopeBodyNode());
    Node envNode = getEnvelopeNode();
    Node bodyNode = getBodyNode();
    Node argNode = createContentNode(service, action, argList);
    bodyNode.addNode(argNode);
    setContent(envNode);

    String serviceType = service.getServiceType();
    String actionName = action.getName();
    String soapAction = "\"" + serviceType + "#" + actionName + "\"";
    setSOAPAction(soapAction);
  }

  ////////////////////////////////////////////////
  //	Contents
  ////////////////////////////////////////////////

  private Node createContentNode(Service service, Action action, ArgumentList argList) {
    String actionName = action.getName();
    String serviceType = service.getServiceType();

    Node actionNode = new Node();
    actionNode.setName(Control.NS, actionName);
    actionNode.setNameSpace(Control.NS, serviceType);

    int argListCnt = argList.size();
    for (int n = 0; n < argListCnt; n++) {
      Argument arg = argList.getArgument(n);
      Node argNode = new Node();
      argNode.setName(arg.getName());
      argNode.setValue(arg.getValue());
      actionNode.addNode(argNode);
    }

    return actionNode;
  }

  ////////////////////////////////////////////////
  //	post
  ////////////////////////////////////////////////

  /**
   * Sends this action request to the device and waits for the response.
   *
   * <p>Posts the SOAP action message to the device's control URL and returns the parsed response.
   *
   * @return the action response from the device
   */
  public ActionResponse post() {
    SOAPResponse soapRes = postMessage(getRequestHost(), getRequestPort());
    return new ActionResponse(soapRes);
  }
}
