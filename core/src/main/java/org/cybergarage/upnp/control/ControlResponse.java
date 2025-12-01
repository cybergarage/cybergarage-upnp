/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ControlResponse.java
 *
 *	Revision;
 *
 *	01/29/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.http.*;
import org.cybergarage.xml.*;
import org.cybergarage.soap.*;

import org.cybergarage.upnp.*;

/**
 * Base class for UPnP control responses.
 * 
 * <p>This class extends {@link SOAPResponse} to provide common functionality
 * for both action responses and state variable query responses. It handles
 * SOAP fault generation for UPnP errors and error code extraction.
 * 
 * @see ActionResponse
 * @see QueryResponse
 * @see SOAPResponse
 */
public class ControlResponse extends SOAPResponse {
  /**
   * SOAP fault code for client errors.
   */
  public static final String FAULT_CODE = "Client";
  
  /**
   * SOAP fault string for UPnP errors.
   */
  public static final String FAULT_STRING = "UPnPError";

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an empty control response.
   * 
   * <p>Sets the server header to the UPnP stack identifier.
   */
  public ControlResponse() {
    setServer(UPnP.getServerName());
  }

  /**
   * Constructs a control response from a SOAP response.
   * 
   * @param soapRes the SOAP response to wrap
   */
  public ControlResponse(SOAPResponse soapRes) {
    super(soapRes);
  }

  ////////////////////////////////////////////////
  //	FaultResponse
  ////////////////////////////////////////////////

  /**
   * Sets this response as a SOAP fault with the specified UPnP error code and description.
   * 
   * <p>Creates a properly formatted SOAP fault response conforming to the UPnP
   * specification, including the error code and description in the detail element.
   * Sets the HTTP status to 500 Internal Server Error.
   * 
   * @param errCode the UPnP error code
   * @param errDescr the human-readable error description
   * @see UPnPStatus
   */
  public void setFaultResponse(int errCode, String errDescr) {
    setStatusCode(HTTPStatus.INTERNAL_SERVER_ERROR);

    Node bodyNode = getBodyNode();
    Node faultNode = createFaultResponseNode(errCode, errDescr);
    bodyNode.addNode(faultNode);

    Node envNode = getEnvelopeNode();
    setContent(envNode);
  }

  /**
   * Sets this response as a SOAP fault with the specified UPnP error code.
   * 
   * <p>The error description is automatically derived from the error code.
   * 
   * @param errCode the UPnP error code
   * @see #setFaultResponse(int, String)
   */
  public void setFaultResponse(int errCode) {
    setFaultResponse(errCode, UPnPStatus.code2String(errCode));
  }

  ////////////////////////////////////////////////
  //	createFaultResponseNode
  ////////////////////////////////////////////////

  private Node createFaultResponseNode(int errCode, String errDescr) {
    // <s:Fault>
    Node faultNode = new Node(SOAP.XMLNS + SOAP.DELIM + SOAP.FAULT);

    // <faultcode>s:Client</faultcode>
    Node faultCodeNode = new Node(SOAP.FAULT_CODE);
    faultCodeNode.setValue(SOAP.XMLNS + SOAP.DELIM + FAULT_CODE);
    faultNode.addNode(faultCodeNode);

    // <faultstring>UPnPError</faultstring>
    Node faultStringNode = new Node(SOAP.FAULT_STRING);
    faultStringNode.setValue(FAULT_STRING);
    faultNode.addNode(faultStringNode);

    // <detail>
    Node detailNode = new Node(SOAP.DETAIL);
    faultNode.addNode(detailNode);

    // <UPnPError xmlns="urn:schemas-upnp-org:control-1-0">
    Node upnpErrorNode = new Node(FAULT_STRING);
    upnpErrorNode.setAttribute("xmlns", Control.XMLNS);
    detailNode.addNode(upnpErrorNode);

    // <errorCode>error code</errorCode>
    Node errorCodeNode = new Node(SOAP.ERROR_CODE);
    errorCodeNode.setValue(errCode);
    upnpErrorNode.addNode(errorCodeNode);

    // <errorDescription>error string</errorDescription>
    Node errorDesctiprionNode = new Node(SOAP.ERROR_DESCRIPTION);
    errorDesctiprionNode.setValue(errDescr);
    upnpErrorNode.addNode(errorDesctiprionNode);

    return faultNode;
  }

  private Node createFaultResponseNode(int errCode) {
    return createFaultResponseNode(errCode, UPnPStatus.code2String(errCode));
  }

  ////////////////////////////////////////////////
  //	UPnP Error
  ////////////////////////////////////////////////

  private UPnPStatus upnpErr = new UPnPStatus();

  private Node getUPnPErrorNode() {
    Node detailNode = getFaultDetailNode();
    if (detailNode == null) return null;
    return detailNode.getNodeEndsWith(SOAP.UPNP_ERROR);
  }

  private Node getUPnPErrorCodeNode() {
    Node errorNode = getUPnPErrorNode();
    if (errorNode == null) return null;
    return errorNode.getNodeEndsWith(SOAP.ERROR_CODE);
  }

  private Node getUPnPErrorDescriptionNode() {
    Node errorNode = getUPnPErrorNode();
    if (errorNode == null) return null;
    return errorNode.getNodeEndsWith(SOAP.ERROR_DESCRIPTION);
  }

  /**
   * Returns the UPnP error code from the SOAP fault detail.
   * 
   * @return the error code, or -1 if not present or invalid
   */
  public int getUPnPErrorCode() {
    Node errorCodeNode = getUPnPErrorCodeNode();
    if (errorCodeNode == null) return -1;
    String errorCodeStr = errorCodeNode.getValue();
    try {
      return Integer.parseInt(errorCodeStr);
    } catch (Exception e) {
      return -1;
    }
  }

  /**
   * Returns the UPnP error description from the SOAP fault detail.
   * 
   * @return the error description, or an empty string if not present
   */
  public String getUPnPErrorDescription() {
    Node errorDescNode = getUPnPErrorDescriptionNode();
    if (errorDescNode == null) return "";
    return errorDescNode.getValue();
  }

  /**
   * Returns the UPnP error status from the SOAP fault.
   * 
   * <p>Extracts both the error code and description from the fault detail
   * and returns them in a {@link UPnPStatus} object.
   * 
   * @return the UPnP error status
   */
  public UPnPStatus getUPnPError() {
    int code = 0;
    String desc = "";
    code = getUPnPErrorCode();
    desc = getUPnPErrorDescription();
    upnpErr.setCode(code);
    upnpErr.setDescription(desc);
    return upnpErr;
  }
}
