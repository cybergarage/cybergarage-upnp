/******************************************************************
 *
 *	CyberXML for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Parser.java
 *
 *	Revision;
 *
 *	11/26/2003
 *		- first revision.
 *	03/30/2005
 *		- Change parse(String) to use StringBufferInputStream instead of URL.
 *	11/11/2009
 *		- Changed Parser::parser() to use ByteArrayInputStream instead of StringBufferInputStream because of bugs in Android v1.6.
 *
 ******************************************************************/

package org.cybergarage.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;

import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;

public abstract class Parser {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  public Parser() {}

  ////////////////////////////////////////////////
  //	parse
  ////////////////////////////////////////////////

  public abstract Node parse(InputStream inStream) throws ParserException;

  ////////////////////////////////////////////////
  //	parse (URL)
  ////////////////////////////////////////////////

  public Node parse(URL locationURL) throws ParserException {
    URL current = locationURL;
    long deadline = System.nanoTime() + HTTP.DEFAULT_TIMEOUT * 1000000000L;
    for (int redirects = 0; redirects <= 5; redirects++) {
      long remaining = (deadline - System.nanoTime()) / 1000000L;
      if (remaining <= 0) throw new ParserException("Description deadline exceeded");
      HTTPRequest request = new HTTPRequest();
      request.setMethod(HTTP.GET);
      request.setResponseTimeout((int) Math.min(Integer.MAX_VALUE, remaining));
      HTTPResponse response;
      try {
        response = request.post(current);
      } catch (IllegalArgumentException e) {
        throw new ParserException(e);
      }
      int status = response.getStatusCode();
      if (status == 301 || status == 302 || status == 303 || status == 307 || status == 308) {
        try {
          URL next = new URL(current, response.getHeaderValue(HTTP.LOCATION));
          if (!current.getProtocol().equalsIgnoreCase(next.getProtocol()))
            throw new ParserException("Cross-protocol description redirect");
          current = next;
          continue;
        } catch (java.net.MalformedURLException e) {
          throw new ParserException(e);
        }
      }
      if (!response.isSuccessful())
        throw new ParserException("Unable to retrieve description: " + current);
      return parse(new ByteArrayInputStream(response.getContent()));
    }
    throw new ParserException("Too many description redirects");
  }

  ////////////////////////////////////////////////
  //	parse (File)
  ////////////////////////////////////////////////

  public Node parse(File descriptionFile) throws ParserException {
    try (InputStream fileIn = new FileInputStream(descriptionFile)) {
      return parse(fileIn);

    } catch (Exception e) {
      throw new ParserException(e);
    }
  }

  ////////////////////////////////////////////////
  //	parse (Memory)
  ////////////////////////////////////////////////

  public Node parse(String descr) throws ParserException {
    try {
      InputStream decrIn = new ByteArrayInputStream(descr.getBytes());
      Node root = parse(decrIn);
      return root;
    } catch (Exception e) {
      throw new ParserException(e);
    }
  }
}
