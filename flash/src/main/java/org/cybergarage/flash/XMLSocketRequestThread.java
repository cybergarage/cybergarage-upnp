/******************************************************************
*
*	CyberFlash for Java
*
*	Copyright (C) Satoshi Konno 2005
*
*	File: XMLSocketThread.java
*
*	Revision:
*
*	09/08/05
*		- first revision.
*	
******************************************************************/

package org.cybergarage.flash;

import java.io.*;
import java.net.*;

import org.cybergarage.util.*;
import org.cybergarage.xml.*;
import org.cybergarage.xml.parser.*;

public class XMLSocketRequestThread extends Thread
{
	////////////////////////////////////////////////
	//	Constant
	////////////////////////////////////////////////
	
	private final static int READ_BUF_SIZE = 1;
	
	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////
	
	private XMLSocketServer xmlSockServer;
	private Socket sock;
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public XMLSocketRequestThread(XMLSocketServer xmlSockServer, Socket sock)
	{
		this.xmlSockServer = xmlSockServer;
		this.sock = sock;
	}

	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	public void run()
	{
		Debug.message("XMLSocketRequestThread.run");

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		byte readBuf[] = new byte[READ_BUF_SIZE];
		ByteArrayOutputStream reqByte = new ByteArrayOutputStream();
		try {
			in = new BufferedInputStream(sock.getInputStream());
			
			int startTagCnt = 0;
			int endTagCnt = 0;
			
			boolean ltFound = false;
			boolean gtFound = false;
			boolean slFound = false;
			
			int readLen = in.read(readBuf, 0, READ_BUF_SIZE);
			while (0 < readLen) {
				reqByte.write(readBuf, 0, readLen);
				switch(readBuf[0]) {
				case '<':
					ltFound = true;
					break;
				case '>':
					gtFound = true;
					break;
				case '/':
					slFound = true;
					break;
				}
				if (ltFound == true && gtFound == true) {
					if (slFound == false)
						startTagCnt++;
					else
						endTagCnt++;
					ltFound = false;
					gtFound = false;
					slFound = false;
				}
				if (0 < startTagCnt && 0 < endTagCnt && startTagCnt == endTagCnt)
					break;
				readLen = in.read(readBuf, 0, READ_BUF_SIZE);
			}
			
		}
		catch (IOException ioe) {			
			Debug.warning(ioe);
		}
		
		String reqStr = reqByte.toString();

		Debug.message("XML Request Received\nreqByte = " + reqByte.size() + "\n" + reqStr);
		
		Node reqNode = null;
		try {
			Parser xmlParser = new XercesParser();
			reqNode = xmlParser.parse(reqStr);
		}
		catch (ParserException pe) {			
			Debug.warning(pe);
		}
		
		Node resNode = xmlSockServer.performRequestListener(reqNode);
		
		try {
			out = new BufferedOutputStream(sock.getOutputStream());
			if (resNode != null) {
				String resStr = resNode.toString();
				byte resByte[] = resStr.getBytes();
				out.write(resByte, 0, resByte.length);
			}
			out.write(0x00);
			out.flush();
		}
		catch (IOException ioe) {			
			Debug.warning(ioe);
		}

		try {
			in.close();
			out.close();
		}
		catch (IOException ioe) {			
			Debug.warning(ioe);
		}
		/*
		HTTPSocket httpSock = new HTTPSocket(sock);
		if (httpSock.open() == false)
			return;
		HTTPRequest httpReq = new HTTPRequest();
		httpReq.setSocket(httpSock);
		while (httpReq.read() == true) {
			httpServer.performRequestListener(httpReq);
			if (httpReq.isKeepAlive() == false)
				break;
		}
		httpSock.close();
		*/
	}
}
