/******************************************************************
*
*	CyberLink Proxy for Java
*
*	Copyright (C) Satoshi Konno 2002-2006
*
*	File: Proxy.java
*
*	Revision:
*
*	04/24/06
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.cybergarage.xml.*;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPServerThread;
import org.cybergarage.util.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.xml.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.event.*;

public class Proxy implements NotifyListener, EventListener, SearchResponseListener, CommandListener, Runnable
{
	////////////////////////////////////////////////
	//	Constants
	////////////////////////////////////////////////
	
	public final static int DEFAULT_SERVER_PORT = 48020;
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public Proxy()
	{
		setLocalPort(DEFAULT_SERVER_PORT);
		
		initUUID();
		
		ControlPoint ctrlPoint = new ControlPoint();
		setControlPoint(ctrlPoint);
	}

	////////////////////////////////////////////////
	//	UUID
	////////////////////////////////////////////////

	private byte uuid[];

	private void initUUID()
	{
		UUID uuidRandom = UUID.randomUUID();
		uuid = uuidRandom.toString().getBytes();
	}
	
	public byte[] getUUID()
	{
		return uuid;
	}

	////////////////////////////////////////////////
	//	Control Point
	////////////////////////////////////////////////
	
	private ControlPoint ctrlPoint;
	
	public void setControlPoint(ControlPoint cp)
	{
		ctrlPoint = cp;
	}
	
	public ControlPoint getControlPoint()
	{
		return ctrlPoint;
	}
	
	////////////////////////////////////////////////
	//	Local Port
	////////////////////////////////////////////////

	private int localPort;
	
	public void setLocalPort(int port)
	{
		localPort = port;
	}
	
	public int getLocalPort()
	{
		return localPort;
	}
	
	////////////////////////////////////////////////
	//	Server List
	////////////////////////////////////////////////
	
	private ServerList serverList = new ServerList();
	
	public ServerList getServerList()
	{
		return serverList;
	}
	
	////////////////////////////////////////////////
	//	Client List
	////////////////////////////////////////////////
	
	private ClientList clientList = new ClientList();
	
	public ClientList getClientList()
	{
		return clientList;
	}
	
	////////////////////////////////////////////////
	//	open/close
	////////////////////////////////////////////////
	
	private ServerSocket serverSock = null;
	
	public boolean open(String addr, int port)
	{
		if (isOpened() == true)
			return true;
		try {
			serverSock = new ServerSocket(getLocalPort());
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean close()
	{
		if (isOpened() == false)
			return true;
		try {
			serverSock.close();
			serverSock = null;
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		return true;
	}

	public Socket accept()
	{
		if (isOpened() == false)
			return null;
		try {
			Socket sock = serverSock.accept();
			return sock;
		}
		catch (Exception e) {
			return null;
		}
	}

	public boolean isOpened()
	{
		return (serverSock != null) ? true : false;
	}

	////////////////////////////////////////////////
	//	run	
	////////////////////////////////////////////////

	private Thread serverThread = null;
		
	public void run()
	{
		if (isOpened() == false)
			return;
			
		Thread thisThread = Thread.currentThread();
		
		while (serverThread == thisThread) {
			Thread.yield();
			Socket sock;
			try {
				Debug.message("accept ...");
				sock = accept();
				if (sock != null)
					Debug.message("sock = " + sock.getRemoteSocketAddress());
			}
			catch (Exception e){
				Debug.warning(e);
				break;
			}
			ProxyServerThread proxyServThread = new ProxyServerThread(this, sock);
			proxyServThread.start(); 
			Debug.message("proxyServThread ...");
		}
	}
	
	public boolean start()
	{
		serverThread = new Thread(this);
		serverThread.start();
		return true;
	}
	
	public boolean stop()
	{
		serverThread = null;
		return true;
	}
	
	////////////////////////////////////////////////
	//	Listener (UPnP)
	////////////////////////////////////////////////
	
	public void deviceNotifyReceived(SSDPPacket packet)
	{
		System.out.println(packet.toString());
		
		if (packet.isDiscover() == true) {
			String st = packet.getST();
		}
		else if (packet.isAlive() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
			String url = packet.getLocation();
		}
		else if (packet.isByeBye() == true) {
			String usn = packet.getUSN();
			String nt = packet.getNT();
		}
	}
	
	public void deviceSearchResponseReceived(SSDPPacket packet)
	{
		String uuid = packet.getUSN();
		String st = packet.getST();
		String url = packet.getLocation();

		ClientList clientList = getClientList();
		clientList.lock();
		int clientNum = clientList.size();
		for (int n=0; n<clientNum; n++) {
			Client client = clientList.getClient(n);
			if (client == null)
				continue;
			if (client.open() == false)
				continue;
			Command cmd = new Command();
			cmd.setCode(Command.SEARCH_RESPONSE);
			cmd.send(client);
			client.close();
		}
		clientList.unlock();
	}
	
	public void eventNotifyReceived(String uuid, long seq, String name, String value)
	{
	}

	////////////////////////////////////////////////
	//	Listener (Command)
	////////////////////////////////////////////////
	
	public void commandReceived(Command cmd)
	{
		switch (cmd.getCode()){
		case Command.LOGIN:
			{
				Client client = new Client();
				client.setUUID(cmd.getUUID());
				
				ClientList clientList = getClientList();
				clientList.lock();
				clientList.add(client);
				clientList.unlock();
				
			}
			break;
		case Command.SEARCH:
			{
				ControlPoint ctrlPoint = getControlPoint();
				ctrlPoint.search(new String(cmd.getUUID()));
			}
			break;
		case Command.NOTIFY:
			{
				ControlPoint ctrlPoint = getControlPoint();
				ctrlPoint.search(new String(cmd.getUUID()));
			}
			break;
		}
	}
	
	////////////////////////////////////////////////
	//	Server
	////////////////////////////////////////////////
	
	public boolean addServer(String host, int port, String passwd)
	{
		Server server = new Server(host, port);
		
		if (server.login(getUUID(), passwd)) {
		}
		
		return false;
	}
	
	////////////////////////////////////////////////
	//	Remote
	////////////////////////////////////////////////

	public void searchRemote()
	{
		ServerList serverList = getServerList();
		serverList.search(ST.ROOT_DEVICE);
	}
}
