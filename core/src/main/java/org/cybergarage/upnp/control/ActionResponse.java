/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ActionResponse.java
 *
 *	Revision;
 *
 *	01/29/03
 *		- first revision.
 *	09/02/03
 *		- Giordano Sassaroli <sassarol@cefriel.it>
 *		- Problem : Action Responses do not contain the mandatory header field EXT
 *		- Error : ActionResponse class does not set the EXT header
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.upnp.*;
import org.cybergarage.http.*;
import org.cybergarage.soap.*;
import org.cybergarage.xml.*;

/**
 * Represents a UPnP action response returned from a device to a control point.
 * 
 * <p>This class encapsulates the SOAP-formatted response to an action invocation.
 * It contains the output argument values returned by the device after executing
 * the requested action.
 * 
 * <p>The response includes:
 * <ul>
 *   <li>HTTP status code (200 OK for success, 500 for errors)</li>
 *   <li>Output argument values</li>
 *   <li>Error information if the action failed</li>
 *   <li>Required EXT header for UPnP compliance</li>
 * </ul>
 * 
 * @see ActionRequest
 * @see Action
 * @see ControlResponse
 */
public class ActionResponse extends ControlResponse {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an empty action response.
   * 
   * <p>Sets the mandatory EXT header required by the UPnP specification.
   */
  public ActionResponse() {
    setHeader(HTTP.EXT, "");
  }

  /**
   * Constructs an action response from a SOAP response.
   * 
   * <p>Wraps an existing SOAP response and sets the mandatory EXT header.
   * 
   * @param soapRes the SOAP response to wrap
   */
  public ActionResponse(SOAPResponse soapRes) {
    super(soapRes);
    setHeader(HTTP.EXT, "");
  }

  ////////////////////////////////////////////////
  //	Response
  ////////////////////////////////////////////////

  /**
   * Constructs the response content from an executed action.
   * 
   * <p>Creates a SOAP response containing the action name and output
   * argument values. Sets the HTTP status to 200 OK.
   * 
   * @param action the action containing output argument values to return
   */
  public void setResponse(Action action) {
    setStatusCode(HTTPStatus.OK);

    Node bodyNode = getBodyNode();
    Node resNode = createResponseNode(action);
    bodyNode.addNode(resNode);

    Node envNode = getEnvelopeNode();
    setContent(envNode);
  }

  private Node createResponseNode(Action action) {
    String actionName = action.getName();
    Node actionNameResNode = new Node(SOAP.METHODNS + SOAP.DELIM + actionName + SOAP.RESPONSE);

    Service service = action.getService();
    if (service != null) {
      actionNameResNode.setAttribute("xmlns:" + SOAP.METHODNS, service.getServiceType());
    }

    ArgumentList argList = action.getArgumentList();
    int nArgs = argList.size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = argList.getArgument(n);
      if (arg.isOutDirection() == false) continue;
      Node argNode = new Node();
      argNode.setName(arg.getName());
      argNode.setValue(arg.getValue());
      actionNameResNode.addNode(argNode);
    }

    return actionNameResNode;
  }

  ////////////////////////////////////////////////
  //	getResponse
  ////////////////////////////////////////////////

  /**
   * Returns the XML node containing the action response data.
   * 
   * @return the action response node, or {@code null} if not present
   */
  private Node getActionResponseNode() {
    Node bodyNode = getBodyNode();
    if (bodyNode == null || bodyNode.hasNodes() == false) return null;
    return bodyNode.getNode(0);
  }

  /**
   * Returns the list of output arguments from the action response.
   * 
   * <p>Parses the SOAP response body to extract argument names and values
   * returned by the device.
   * 
   * @return the list of output arguments, or an empty list if none are present
   */
  public ArgumentList getResponse() {
    ArgumentList argList = new ArgumentList();

    Node resNode = getActionResponseNode();
    if (resNode == null) return argList;

    int nArgs = resNode.getNNodes();
    for (int n = 0; n < nArgs; n++) {
      Node node = resNode.getNode(n);
      String name = node.getName();
      String value = node.getValue();
      Argument arg = new Argument(name, value);
      argList.add(arg);
    }

    return argList;
  }
}
