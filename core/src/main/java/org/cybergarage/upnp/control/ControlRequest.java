/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ControlRequest.java
 *
 *	Revision:
 *
 *	01/29/03
 *		- first revision.
 *	05/22/03
 *		- Giordano Sassaroli <sassarol@cefriel.it>
 *		- Description: inserted a check at the beginning of the setRequestHost method
 *		- Problem : If the host does not start with a '/', the device could refuse the control action
 *		- Error : it is not an error, but adding the '/' when missing allows the integration with the Intel devices
 *	09/02/03
 *		- Giordano Sassaroli <sassarol@cefriel.it> / Suzan Foster
 *		- Problem : NullpointerException thrown for devices whose description use absolute urls
 *		- Error : the presence of a base url is not mandatory, the API code makes the assumption that control and event subscription urls are relative.
 *		  If the baseUrl is not present, the request host and port should be extracted from the control/subscription url
 *		- Description: The method setRequestHost/setService should be changed as follows
 *	02/17/04
 *		- Rob van den Boomen <rob.van.den.boomen@philips.com>
 *		- Fixed to set a URLBase from the SSDP header when the URLBase of the description is null.
 *	02/18/04
 *		- Andre <andre@antiheld.net>
 *		- The xml nodes controlUrl and eventSubUrl can contain absolut urls, but these absolut urls may have
 *		  different ports than the base url! (so seen on my SMC 7004ABR Barricade Router, where xml files are
 *		  requested from port 80, but soap requests are made on port 5440). Therefore whenever a request is made,
 *		  the port specified by the controlUrl or eventSubUrl node should be used, else no response will be returned
 *		  (oddly, there was a response returned even on port 80, but with empty body tags. but the correct response
 *		  finally came from port 5440).
 *		- Fixed to get the port from the control url when it is absolute.
 *	03/20/04
 *		- Thanks for Thomas Schulz <tsroyale at users.sourceforge.net>
 *		- Fixed setRequestHost() for Sony's UPnP stack when the URLBase has the path.
 *
 ******************************************************************/

package org.cybergarage.upnp.control;

import java.net.*;

import org.cybergarage.http.*;
import org.cybergarage.soap.*;

import org.cybergarage.upnp.*;

/**
 * Base class for UPnP control requests.
 * 
 * <p>This class extends {@link SOAPRequest} to provide common functionality
 * for both action requests and state variable query requests. It handles
 * URL resolution, including absolute and relative URLs, URL base paths,
 * and port extraction.
 * 
 * @see ActionRequest
 * @see QueryRequest
 * @see SOAPRequest
 */
public class ControlRequest extends SOAPRequest {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an empty control request.
   */
  public ControlRequest() {}

  /**
   * Constructs a control request from an HTTP request.
   * 
   * @param httpReq the HTTP request to wrap
   */
  public ControlRequest(HTTPRequest httpReq) {
    set(httpReq);
  }

  ////////////////////////////////////////////////
  //	Query
  ////////////////////////////////////////////////

  /**
   * Checks if this is a state variable query request.
   * 
   * @return {@code true} if this is a QueryStateVariable request,
   *         {@code false} otherwise
   */
  public boolean isQueryControl() {
    return isSOAPAction(Control.QUERY_SOAPACTION);
  }

  /**
   * Checks if this is an action control request.
   * 
   * @return {@code true} if this is an action invocation request,
   *         {@code false} if it's a query request
   */
  public boolean isActionControl() {
    return !isQueryControl();
  }

  ////////////////////////////////////////////////
  //	setRequest
  ////////////////////////////////////////////////

  /**
   * Sets the request host and port from the service's control URL.
   * 
   * <p>This method resolves the control URL, considering:
   * <ul>
   *   <li>Absolute vs relative URLs</li>
   *   <li>URL base paths from the device description</li>
   *   <li>Port numbers in control URLs</li>
   *   <li>Fallback to device location if URLBase is not set</li>
   * </ul>
   * 
   * @param service the service whose control URL to use
   */
  protected void setRequestHost(Service service) {
    String ctrlURL = service.getControlURL();

    // Thanks for Thomas Schulz (2004/03/20)
    String urlBase = service.getRootDevice().getURLBase();
    if (urlBase != null && 0 < urlBase.length()) {
      try {
        URL url = new URL(urlBase);
        String basePath = url.getPath();
        int baseLen = basePath.length();
        if (0 < baseLen) {
          if (1 < baseLen || (basePath.charAt(0) != '/')) ctrlURL = basePath + ctrlURL;
        }
      } catch (MalformedURLException e) {
      }
    }

    // Thanks for Giordano Sassaroli <sassarol@cefriel.it> (05/21/03)
    setURI(ctrlURL, true);

    // Thanks for Giordano Sassaroli <sassarol@cefriel.it> and Suzan Foster (09/02/03)
    // Thanks for Andre <andre@antiheld.net> (02/18/04)
    String postURL = "";
    if (HTTP.isAbsoluteURL(ctrlURL) == true) postURL = ctrlURL;

    if (postURL == null || postURL.length() <= 0) postURL = service.getRootDevice().getURLBase();

    // Thanks for Rob van den Boomen <rob.van.den.boomen@philips.com> (02/17/04)
    // BUGFIX, set urlbase from location string if not set in description.xml
    if (postURL == null || postURL.length() <= 0) postURL = service.getRootDevice().getLocation();

    String reqHost = HTTP.getHost(postURL);
    int reqPort = HTTP.getPort(postURL);

    setHost(reqHost, reqPort);
    setRequestHost(reqHost);
    setRequestPort(reqPort);
  }
}
