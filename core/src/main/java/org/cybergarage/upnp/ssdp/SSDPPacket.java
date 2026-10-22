/******************************************************************
 *
 *	CyberLink for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: SSDPPacket.java
 *
 *	Revision;
 *
 *	11/18/02
 *		- first revision.
 *	05/13/03
 *		- Added getLocalAddress().
 *	11/01/04
 *		- Theo Beisch <theo.beisch@gmx.de>
 *		- Fixed isRootDevice() to check the ST header.
 *	11/19/04
 *		- Theo Beisch <theo.beisch@gmx.de>
 *		- Changed getRemoteAddress() to return the adresss instead of the host name.
 *
 ******************************************************************/

package org.cybergarage.upnp.ssdp;

import java.net.*;

import org.cybergarage.http.*;

import org.cybergarage.upnp.device.*;

public class SSDPPacket {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  public SSDPPacket(byte[] buf, int length) {
    dgmPacket = new DatagramPacket(buf, length);
  }

  ////////////////////////////////////////////////
  //	DatagramPacket
  ////////////////////////////////////////////////

  private DatagramPacket dgmPacket = null;

  public DatagramPacket getDatagramPacket() {
    return dgmPacket;
  }

  ////////////////////////////////////////////////
  //	Received socket
  ////////////////////////////////////////////////

  private DatagramSocket recvSocket = null;

  /**
   * Sets the socket that received this packet, so that unicast responses can be sent from the
   * same local port (e.g. SSDP port 1900 for M-SEARCH requests).
   *
   * @param sock the socket that received this packet
   */
  public void setSocket(DatagramSocket sock) {
    recvSocket = sock;
  }

  /**
   * @return the socket that received this packet, or null if unknown
   */
  public DatagramSocket getSocket() {
    return recvSocket;
  }

  ////////////////////////////////////////////////
  //	addr
  ////////////////////////////////////////////////

  private String localAddr = "";

  public void setLocalAddress(String addr) {
    localAddr = addr;
  }

  public String getLocalAddress() {
    return localAddr;
  }

  ////////////////////////////////////////////////
  //	Time
  ////////////////////////////////////////////////

  private long timeStamp;

  public void setTimeStamp(long value) {
    timeStamp = value;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  ////////////////////////////////////////////////
  //	Remote host
  ////////////////////////////////////////////////

  public InetAddress getRemoteInetAddress() {
    return getDatagramPacket().getAddress();
  }

  public String getRemoteAddress() {
    // Thanks for Theo Beisch (11/09/04)
    return getDatagramPacket().getAddress().getHostAddress();
  }

  public int getRemotePort() {
    return getDatagramPacket().getPort();
  }

  ////////////////////////////////////////////////
  //	Access Methods
  ////////////////////////////////////////////////

  public byte[] packetBytes = null;

  public byte[] getData() {
    if (packetBytes != null) return packetBytes;

    DatagramPacket packet = getDatagramPacket();
    int packetLen = packet.getLength();
    String packetData = new String(packet.getData(), 0, packetLen);
    packetBytes = packetData.getBytes();

    return packetBytes;
  }

  ////////////////////////////////////////////////
  //	Access Methods
  ////////////////////////////////////////////////

  public String getHost() {
    return HTTPHeader.getValue(getData(), HTTP.HOST);
  }

  public String getCacheControl() {
    return HTTPHeader.getValue(getData(), HTTP.CACHE_CONTROL);
  }

  public String getLocation() {
    return HTTPHeader.getValue(getData(), HTTP.LOCATION);
  }

  public String getMAN() {
    return HTTPHeader.getValue(getData(), HTTP.MAN);
  }

  public String getST() {
    return HTTPHeader.getValue(getData(), HTTP.ST);
  }

  public String getNT() {
    return HTTPHeader.getValue(getData(), HTTP.NT);
  }

  public String getNTS() {
    return HTTPHeader.getValue(getData(), HTTP.NTS);
  }

  public String getServer() {
    return HTTPHeader.getValue(getData(), HTTP.SERVER);
  }

  public String getUSN() {
    return HTTPHeader.getValue(getData(), HTTP.USN);
  }

  public int getMX() {
    return HTTPHeader.getIntegerValue(getData(), HTTP.MX);
  }

  ////////////////////////////////////////////////
  //	Access Methods
  ////////////////////////////////////////////////

  public InetAddress getHostInetAddress() {
    String host = getHost();
    int colon = host.lastIndexOf(':');
    if (colon <= 0) return null;
    String address = host.substring(0, colon);
    if (address.startsWith("[")) {
      if (!address.endsWith("]") || address.length() <= 2) return null;
      address = address.substring(1, address.length() - 1);
    } else if (address.indexOf(':') >= 0) return null;
    // SSDP HOST is a numeric multicast address; never resolve peer-provided DNS names.
    if (!address.matches("[0-9a-fA-F:.]+")) return null;
    try {
      int port = Integer.parseInt(host.substring(colon + 1));
      if (port != SSDP.PORT) return null;
      if (address.indexOf(':') >= 0) return InetAddress.getByName(address);
      String[] octets = address.split("\\.", -1);
      if (octets.length != 4) return null;
      byte[] bytes = new byte[4];
      for (int i = 0; i < octets.length; i++) {
        if (!octets[i].matches("[0-9]{1,3}")) return null;
        int value = Integer.parseInt(octets[i]);
        if (value > 255) return null;
        bytes[i] = (byte) value;
      }
      return InetAddress.getByAddress(bytes);
    } catch (Exception e) {
      return null;
    }
  }

  ////////////////////////////////////////////////
  //	Access Methods (Extension)
  ////////////////////////////////////////////////

  public boolean isRootDevice() {
    if (NT.isRootDevice(getNT()) == true) return true;
    // Thanks for Theo Beisch (11/01/04)
    if (ST.isRootDevice(getST()) == true) return true;
    return USN.isRootDevice(getUSN());
  }

  public boolean isDiscover() {
    return MAN.isDiscover(getMAN());
  }

  public boolean isAlive() {
    return NTS.isAlive(getNTS());
  }

  public boolean isByeBye() {
    return NTS.isByeBye(getNTS());
  }

  public int getLeaseTime() {
    return SSDP.getLeaseTime(getCacheControl());
  }

  ////////////////////////////////////////////////
  //	toString
  ////////////////////////////////////////////////

  public String toString() {
    return new String(getData());
  }
}
