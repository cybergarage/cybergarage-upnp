/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: Command.java
*
*	Revision:
*
*	04/25/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import org.cybergarage.upnp.ssdp.*;
import org.cybergarage.util.Debug;

public class Command
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static short COMMAND_LENGTH_HEADER_LEN = 2;
	public final static short COMMAND_ID_HEADER_LEN = 2;
	public final static short COMMAND_UUID_LEN = (128/4);
	
	public final static short LOGIN_ACCOUNT_LENGTH = 8;
	public final static short LOGIN_PASSWORD_LENGTH = 8;
	
	////////////////////////////////////////////////
	// Command Constants
	////////////////////////////////////////////////
	
	public final static short NONE = 0;
	
	// Setup Commands
	public final static short PING = 1;
	public final static short PONG = 2;
	
	// Response Commands
	public final static short OK = 200;
	public final static short BAD_REQUEST = 400;
	public final static short UNAUTHORIZED = 401;
	
	// Auth Commands
	public final static short LOGIN = 1001;
	public final static short LOGOUT = 1002;
	
	// SSDP Commands
	public final static short NOTIFY = 4001;
	public final static short SEARCH = 4002;
	public final static short SEARCH_RESPONSE = 4003;
	
	// HTTP Commands
	public final static short HTTP_REQUEST = 8001;
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public Command()
	{
		setCode(NONE);
		setUUID(new byte[0]);
		setData(new byte[0]);
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
	// isCommand
	////////////////////////////////////////////////
	
	public boolean isCommand(short value)
	{
		return (code == value) ? true : false;
	}
	
	public boolean isValid()
	{
		switch (getCode()) {
		case LOGIN:
			{
				if (getLength() < (4 + 4 + (128/8)))
					return false;
			}
			break;
		case LOGOUT:
			{
				if (getLength() < (128/8))
					return false;
			}
			break;
		}
		return true;
	}
	
	public boolean isNotifyCommand()
	{
		return isCommand(NOTIFY);
	}

	public boolean isSearchCommand()
	{
		return isCommand(SEARCH);
	}
	
	public boolean isSearchResponseCommand()
	{
		return isCommand(SEARCH_RESPONSE);
	}
	
	public boolean isHttpRequestCommand()
	{
		return isCommand(HTTP_REQUEST);
	}
	
	////////////////////////////////////////////////
	// Command Length
	////////////////////////////////////////////////

	public short getLength()
	{
		short commandLen = (short)(COMMAND_LENGTH_HEADER_LEN + COMMAND_ID_HEADER_LEN + COMMAND_UUID_LEN);
		if (hasData() == true)
			commandLen += getData().length;
		return commandLen;
	}
	
	////////////////////////////////////////////////
	// Data
	////////////////////////////////////////////////

	private byte uuid[];
	
	public void setUUID(byte value[])
	{
		uuid = value;
	}

	public byte[] getUUID()
	{
		return uuid;
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

	public int getDataLength()
	{
		if (data == null)
			return 0;
		return data.length;
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

	public final static void copyBytes(byte destBytes[], int destOffset, byte srcBytes[], int srcOffset, int copyLength)
	{
		for (int n=0; n<copyLength; n++) {
			if (destBytes.length <= (destOffset + n))
				break;
			if (srcBytes.length <= (srcOffset + n))
				break;
			destBytes[destOffset + n] = srcBytes[srcOffset + n];
		}
	}
	
	public final static void clearBytes(byte dataBytes[])
	{
		for (int n=0; n<dataBytes.length; n++)
			dataBytes[n] = 0;
	}
	
	////////////////////////////////////////////////
	// send
	////////////////////////////////////////////////
	
	private final static byte nullData[] = new byte[0];
	private byte cmdSendBuf[] = new byte[2];
	
	public synchronized boolean send(Session session)
	{
		if (session == null)
			return false;
		
		if (session.isConnected() == false)
			return false;
			
		Socket sock = session.getSocket();
		if (sock == null)
			return false;

		try {
			OutputStream out = sock.getOutputStream();
			out.write(Command.toByte(getLength(), cmdSendBuf));
			out.write(Command.toByte(getCode(), cmdSendBuf));
			out.write(session.getUUID());
			if (hasData() == true)
				out.write(getData());
			out.flush();
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}

		if (Debug.isOn() == true)
			dump();
		
		return true;
	}

	public final static boolean send(Session session, byte uuid[], short code, byte dataByte[])
	{
		Command proxyCmd = new Command();
		proxyCmd.setCode(code);
		proxyCmd.setUUID(uuid);
		if (dataByte != null && 0 < dataByte.length) {
			ByteArrayOutputStream uriByteOut = new ByteArrayOutputStream();
			uriByteOut.write(dataByte, 0, dataByte.length);
			byte nullByte[] = new byte[1];
			nullByte[0] = 0;
			uriByteOut.write(nullByte, 0, 1);
			proxyCmd.setData(uriByteOut.toByteArray());
		}
		return proxyCmd.send(session);
	}
	
	public final static boolean send(Session session, byte uuid[], short code, String dataStr)
	{
		if (dataStr != null)
			return send(session, uuid, code, dataStr.getBytes());
		else
			return send(session, uuid, code, nullData);
	}
	
	public final static boolean send(Session session, byte uuid[], short code)
	{
		return send(session, uuid, code, nullData);
	}
	
	////////////////////////////////////////////////
	// recv
	////////////////////////////////////////////////
	
	private byte cmdRecvBuf[] = new byte[2];
	
	public synchronized boolean recv(Socket sock)
	{
		if (sock == null)
			return false;
		
		try {
				InputStream in = sock.getInputStream();
				if (in.read(cmdRecvBuf) <= 0)
					return false;
				int cmdLen = Command.toShort(cmdRecvBuf);
				if (in.read(cmdRecvBuf) <= 0)
					return false;
				// Code
				short cmdCode = Command.toShort(cmdRecvBuf);
				setCode(cmdCode);
				// UUID
				byte uuid[] = new byte[Command.COMMAND_UUID_LEN];
				in.read(uuid);
				setUUID(uuid);
				// Data
				int dataLen = cmdLen - Command.COMMAND_LENGTH_HEADER_LEN - Command.COMMAND_ID_HEADER_LEN - Command.COMMAND_UUID_LEN;
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
	// command
	////////////////////////////////////////////////
	
	public final static boolean notify(Session session, byte uuid[], String dataStr)
	{
		return send(session, uuid, NOTIFY, dataStr);
	}
	
	public final static boolean search(Session session, byte uuid[], String target, int mx)
	{
		SSDPSearchRequest ssdpReq = new SSDPSearchRequest(target, mx);
		return send(session, uuid, SEARCH, ssdpReq.toString());
	}

	public final static boolean search(Session session, byte uuid[], String target)
	{
		return search(session, uuid, target, SSDP.DEFAULT_MSEARCH_MX);
	}
	
	public final static boolean searchResponse(Session session, byte uuid[], String dataStr)
	{
		return send(session, uuid, SEARCH_RESPONSE, dataStr);
	}
	
	public final static boolean httpRequest(Session session, byte uuid[], String dataStr)
	{
		return send(session, uuid, HTTP_REQUEST, dataStr);
	}

	public final static boolean ok(Session session, byte uuid[])
	{
		return send(session, uuid, OK);
	}

	public final static boolean badRequest(Session session, byte uuid[])
	{
		return send(session, uuid, BAD_REQUEST);
	}

	public final static boolean unauthorized(Session session, byte uuid[])
	{
		return send(session, uuid, UNAUTHORIZED);
	}
	
	////////////////////////////////////////////////
	// Login
	////////////////////////////////////////////////
	
	public boolean isLoginCommand()
	{
		return isCommand(LOGIN);
	}
	
	public final static boolean login(Session session, byte uuid[], String passwd)
	{
		if (passwd == null)
			return false;
		
		byte dataBytes[] = new byte[LOGIN_PASSWORD_LENGTH];
		clearBytes(dataBytes);
		copyBytes(dataBytes, 0, passwd.getBytes(), 0, Math.min(passwd.length(), LOGIN_PASSWORD_LENGTH));
		return send(session, uuid, LOGIN, dataBytes);
	}

	public String getPasswd()
	{
		try {
			byte passwdBytes[] = new byte[LOGIN_PASSWORD_LENGTH];
			copyBytes(passwdBytes, 0, data, 0, LOGIN_PASSWORD_LENGTH);
			return new String(passwdBytes);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return "";		
	}
	
	////////////////////////////////////////////////
	// Logout
	////////////////////////////////////////////////
	
	public boolean isLogoutCommand()
	{
		return isCommand(LOGOUT);
	}
	
	public final static boolean logout(Session session, byte uuid[])
	{
		return send(session, uuid, LOGOUT, uuid);
	}
	
	////////////////////////////////////////////////
	// dump
	////////////////////////////////////////////////
	
	private byte cmdDumpBuf[] = new byte[2];
	
	public synchronized void dump()
	{
		Command.toByte(getLength(), cmdDumpBuf);
		for (int n=0; n<cmdDumpBuf.length; n++)
			System.out.print(Integer.toHexString(cmdDumpBuf[n] & 0xFF) + " ");
		Command.toByte(getCode(), cmdDumpBuf);
		for (int n=0; n<cmdDumpBuf.length; n++)
			System.out.print(Integer.toHexString(cmdDumpBuf[n] & 0xFF) + " ");
		if (hasData() == true) {
			byte data[] = getData();
			for (int n=0; n<data.length; n++)
				System.out.print(Integer.toHexString(data[n] & 0xFF) + " ");
		}
		System.out.println("");
	}
}

