/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003
 *
 *	File : DIDLLite.java
 *
 *	Revision:
 *
 *	10/22/03
 *		- first revision.
 *	10/26/04
 *		- Brent Hills <bhills@openshores.com>
 *		- Removed a SOAP header from output().
 *	10/28/04
 *		- Brent Hills <bhills@openshores.com>
 *		- Removed a SOAP header from output().
 *	04/18/05
 *		- Matt <matthias@streams.ch>
 *		- Changed toString() using UTF-8 OutputStreamWriter.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.server.object;

import java.io.*;
import org.cybergarage.soap.*;
import org.cybergarage.util.*;

public class DIDLLite {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String NAME = "DIDL-Lite";
  public static final String XMLNS = "xmlns";
  public static final String XMLNS_URL = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/";
  public static final String XMLNS_DC = "xmlns:dc";
  public static final String XMLNS_DC_URL = "http://purl.org/dc/elements/1.1/";
  public static final String XMLNS_UPNP = "xmlns:upnp";
  public static final String XMLNS_UPNP_URL = "urn:schemas-upnp-org:metadata-1-0/upnp/";

  public static final String CONTAINER = "container";
  public static final String ID = "id";
  public static final String SEARCHABLE = "searchable";
  public static final String PARENTID = "parentID";
  public static final String RESTICTED = "restricted";

  public static final String OBJECT_CONTAINER = "object.container";

  public static final String RES = "res";
  public static final String RES_PROTOCOLINFO = "protocolInfo";

  ////////////////////////////////////////////////
  // Constrictor
  ////////////////////////////////////////////////

  public DIDLLite() {}

  ////////////////////////////////////////////////
  //	ContentNode
  ////////////////////////////////////////////////

  private ContentNodeList nodeList = new ContentNodeList();

  public void setContentNode(ContentNode node) {
    nodeList.clear();
    nodeList.add(node);
  }

  public void addContentNode(ContentNode node) {
    nodeList.add(node);
  }

  public int getNContentNodes() {
    return nodeList.size();
  }

  public ContentNode getContentNode(int n) {
    return nodeList.getContentNode(n);
  }

  ////////////////////////////////////////////////
  //	toString
  ////////////////////////////////////////////////

  public void output(PrintWriter ps) {
    // Thanks for Brent Hills (10/26/04)
    ps.print(SOAP.VERSION_HEADER);

    DIDLLiteNode didlNode = new DIDLLiteNode();

    String name = didlNode.getName();
    // String value = didlNode.getValue();

    ps.print("<" + name);
    didlNode.outputAttributes(ps);
    ps.println(">");

    int nNodes = getNContentNodes();
    for (int n = 0; n < nNodes; n++) {
      ContentNode contentNode = getContentNode(n);
      contentNode.output(ps, 1, false);
    }

    ps.println("</" + name + ">");
  }

  public String toString() {
    try {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      Writer writer = new OutputStreamWriter(byteOut, "UTF-8");
      PrintWriter pr = new PrintWriter(writer);
      output(pr);
      pr.flush();
      return byteOut.toString();
    } catch (UnsupportedEncodingException e) {
      Debug.warning(e);
    }
    return "";
  }
}
