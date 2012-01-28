/******************************************************************
*
*	RemoteUI for CyberLink
*
*	Copyright (C) Satoshi Konno 2006
*
*	File : XRTCommand.java
*
*	03/29/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.rui.xrt;

import java.io.*;
import java.net.Socket;
import javax.net.ssl.*;

import org.cybergarage.upnp.proxy.Command;
import org.cybergarage.upnp.rui.Session;
import org.cybergarage.util.Debug;

public class XRTCommand
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static short COMMAND_LENGTH_HEADER_LEN = 2;
	public final static short COMMAND_ID_HEADER_LEN = 2;
	
	////////////////////////////////////////////////
	// Command Constants
	////////////////////////////////////////////////
	
	public final static short NONE = 0;
	
	// Setup Commands
	public final static short REQUEST = 2;
	public final static short EXIT = 5;
	public final static short JUMBO = 6;
	
	// Cookie Commands
	public final static short COOKIE_GET = 7;
	public final static short COOKIE_STORE = 8;
	public final static short COOKIE_VALUE = 9;
	
	// Basic Display Remoting Commands
	public final static short GETSTATE = 1002;
	public final static short REPAINT = 1003;
	public final static short DRAWFILLBOX = 1004;
	public final static short DRAWIMAGE = 1005;
	public final static short ALLOCATE = 1007;
	public final static short SETFOCUS = 1008;
	public final static short HOLD = 1009;
	public final static short RELEASE = 1010;
	
	// Input Remoting Commands
	public final static short KEY_DOWN = 2001;
	public final static short KEY_UP = 2002;
	public final static short KEY_PRESS = 2003;
	public final static short MOUSE_DOWN = 2005;
	public final static short MOUSE_UP = 2006;
	
	// Media Control Commands
	public final static short MEDIA_OPEN = 3000;
	public final static short MEDIA_PLAYSTATE = 3001;
	public final static short MEDIA_VOLUME = 3002;
	public final static short MEDIA_POSITION = 3003;
	public final static short MEDIA_SEEK = 3004;
	public final static short MEDIA_ZOOM = 3005;
	public final static short MEDIA_METADATA = 3006;
	
	// Additional Commands
	public final static short PING = 30010;
	public final static short PONG = 30011;
	//public final static short EXTENSION = 40000;

	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public XRTCommand()
	{
		setCode(NONE);
		setData(null);
	}	

	////////////////////////////////////////////////
	// Command Length
	////////////////////////////////////////////////

	private short code;
	
	public void setCode(short value)
	{
		code = value;
	}

	public short getCode()
	{
		return code;
	}

	////////////////////////////////////////////////
	// Command Length
	////////////////////////////////////////////////

	public short getLength()
	{
		short commandLen = (short)(COMMAND_LENGTH_HEADER_LEN + COMMAND_ID_HEADER_LEN);
		if (hasData() == true)
			commandLen += getData().length;
		return commandLen;
	}
	
	////////////////////////////////////////////////
	// Data
	////////////////////////////////////////////////

	private byte data[];
	
	public void setData(byte value[])
	{
		data = value;
	}

	public byte[] getData()
	{
		return data;
	}

	public boolean hasData()
	{
		if (data == null)
			return false;
		return (0 < data.length) ? true : false;
	}
	
	////////////////////////////////////////////////
	// Convertor
	////////////////////////////////////////////////

	public final static byte[] toByte(short shortValue, byte byteValue[], int offset)
	{
		byteValue[0+offset] = (byte)(shortValue & 0xFF);
		byteValue[1+offset] = (byte)((shortValue >> 8) & 0xFF);
		return byteValue;
	}

	public final static short toShort(byte byteValue[], int offset)
	{
		short shortValue = 0;
		shortValue += (byteValue[1+offset] & 0xFF) << 8;
		shortValue += (byteValue[0+offset]& 0xFF);
		return shortValue;
	}

	public final static byte[] toByte(int intValue, byte byteValue[], int offset)
	{
		byteValue[0+offset] = (byte)(intValue & 0xFF);
		byteValue[1+offset] = (byte)((intValue >> 8) & 0xFF);
		byteValue[2+offset] = (byte)((intValue >> 16) & 0xFF);
		byteValue[3+offset] = (byte)((intValue >> 24) & 0xFF);
		return byteValue;
	}

	public final static int toInteger(byte byteValue[], int offset)
	{
		int intValue = 0;
		intValue += (byteValue[3+offset] & 0xFF) << 24;
		intValue += (byteValue[2+offset] & 0xFF) << 16;
		intValue += (byteValue[1+offset] & 0xFF) << 8;
		intValue += (byteValue[0+offset] & 0xFF);
		return intValue;
	}
	
	public final static byte[] toByte(short shortValue, byte byteValue[])
	{
		return toByte(shortValue, byteValue, 0);
	}

	public final static short toShort(byte byteValue[])
	{
		return toShort(byteValue, 0);
	}

	public final static byte[] toByte(int intValue, byte byteValue[])
	{
		return toByte(intValue, byteValue, 0);
	}

	public final static int toInteger(byte byteValue[])
	{
		return toInteger(byteValue, 0);
	}
	
	////////////////////////////////////////////////
	// send
	////////////////////////////////////////////////
	
	private byte cmdSendBuf[] = new byte[2];
	
	public synchronized boolean send(Session session)
	{
		if (session == null)
			return false;
		
		Socket sock = session.getSocket();
		if (sock == null)
			return false;

		try {
			OutputStream out = sock.getOutputStream();
			out.write(Command.toByte(getLength(), cmdSendBuf));
			out.write(Command.toByte(getCode(), cmdSendBuf));
			if (hasData() == true)
				out.write(getData());
			out.flush();
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		
		Debug.message("Send : " + getCode() + " (" + getLength() + ")");				
		if (Debug.isOn() == true)
			dump();
		
		return true;
	}

	public final static boolean send(Session session, short code, String dataStr)
	{
		XRTCommand xrtCmd = new XRTCommand();
		xrtCmd.setCode(code);
		if (dataStr != null) {
			ByteArrayOutputStream uriByteOut = new ByteArrayOutputStream();
			byte protoURIByte[] = dataStr.getBytes();
			uriByteOut.write(protoURIByte, 0, protoURIByte.length);
			byte nullByte[] = new byte[1];
			nullByte[0] = 0;
			uriByteOut.write(nullByte, 0, 1);
			xrtCmd.setData(uriByteOut.toByteArray());
		}
		return xrtCmd.send(session);
	}
	
	public final static boolean send(Session session, short code)
	{
		return send(session, code, null);
	}
	
	////////////////////////////////////////////////
	// command
	////////////////////////////////////////////////
	
	public final static boolean request(Session session, String protoURI)
	{
		return send(session, REQUEST, protoURI);
	}
	
	public final static boolean exit(Session session)
	{
		return send(session, EXIT);
	}
	
	////////////////////////////////////////////////
	// recv
	////////////////////////////////////////////////
	
	private byte cmdRecvBuf[] = new byte[2];
	
	public synchronized boolean recv(Session session)
	{
		if (session == null)
			return false;
		
		Socket sock = session.getSocket();
		if (sock == null)
			return false;
		
		try {
				InputStream in = sock.getInputStream();
				if (in.read(cmdRecvBuf) <= 0)
					return false;
				int cmdLen = XRTCommand.toShort(cmdRecvBuf);
				if (in.read(cmdRecvBuf) <= 0)
					return false;
				short cmdCode = XRTCommand.toShort(cmdRecvBuf);
				setCode(cmdCode);
				int dataLen = cmdLen - XRTCommand.COMMAND_LENGTH_HEADER_LEN - XRTCommand.COMMAND_ID_HEADER_LEN;
				if (0 < dataLen) {
					byte cmdData[] = new byte[dataLen];
					in.read(cmdData);
					setData(cmdData);
				}
				Debug.message("Recv : " + getCode() + " (" + getLength() + ")");
				if (Debug.isOn() == true)
					dump();
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		return true;
	}

	////////////////////////////////////////////////
	// dump
	////////////////////////////////////////////////
	
	private byte cmdDumpBuf[] = new byte[2];
	
	public synchronized void dump()
	{
		XRTCommand.toByte(getLength(), cmdDumpBuf);
		for (int n=0; n<cmdDumpBuf.length; n++)
			System.out.print(Integer.toHexString(cmdDumpBuf[n] & 0xFF) + " ");
		XRTCommand.toByte(getCode(), cmdDumpBuf);
		for (int n=0; n<cmdDumpBuf.length; n++)
			System.out.print(Integer.toHexString(cmdDumpBuf[n] & 0xFF) + " ");
		if (hasData() == true) {
			byte data[] = getData();
			for (int n=0; n<data.length; n++)
				System.out.print(Integer.toHexString(data[n] & 0xFF) + " ");
		}
		System.out.println("");
	}

	////////////////////////////////////////////////
	// dump
	////////////////////////////////////////////////
	
	public int getX() 
	{
		if (data.length < 2)
			return 0;
		return toShort(data, 0);
	}

	public int getY() 
	{
		if (data.length < 4)
			return 0;
		return toShort(data, 2);
	}

	public int getWidth() 
	{
		if (data.length < 6)
			return 0;
		return toShort(data, 4);
	}

	public int getHeight() 
	{
		if (data.length < 8)
			return 0;
		return toShort(data, 6);
	}

	public int getRed() 
	{
		if (data.length < 9)
			return 0;
		return (data[8] & 0xFF);
	}

	public int getGreen() 
	{
		if (data.length < 10)
			return 0;
		return (data[9] & 0xFF);
	}

	public int getBlue() 
	{
		if (data.length < 11)
			return 0;
		return (data[10] & 0xFF);
	}
}

