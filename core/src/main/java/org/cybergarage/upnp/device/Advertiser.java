/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Advertiser.java
 *
 *	Revision;
 *
 *	12/24/03
 *		- first revision.
 *	06/18/04
 *		- Changed to advertise every 25%-50% of the periodic notification cycle for NMPR;
 *
 ******************************************************************/

package org.cybergarage.upnp.device;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;

/**
 * Background thread that periodically advertises a UPnP device's presence.
 *
 * <p>The advertiser sends SSDP NOTIFY messages at regular intervals to announce the device's
 * availability on the network. The announcement interval is randomized between 25%-50% of the
 * device's lease time to distribute network traffic when multiple devices are present.
 *
 * <p>This class is used internally by {@link Device} to implement the Network Media Player
 * Requirement (NMPR) for periodic advertisements.
 *
 * @see Device
 */
public class Advertiser extends ThreadCore {
  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an advertiser for the specified device.
   *
   * @param dev the device to advertise
   */
  public Advertiser(Device dev) {
    setDevice(dev);
  }

  ////////////////////////////////////////////////
  //	Member
  ////////////////////////////////////////////////

  private Device device;

  /**
   * Sets the device to be advertised.
   *
   * @param dev the device to advertise
   */
  public void setDevice(Device dev) {
    device = dev;
  }

  /**
   * Returns the device being advertised.
   *
   * @return the device associated with this advertiser
   */
  public Device getDevice() {
    return device;
  }

  ////////////////////////////////////////////////
  //	Thread
  ////////////////////////////////////////////////

  public void run() {
    Device dev = getDevice();
    long leaseTime = dev.getLeaseTime();
    long notifyInterval;
    while (isRunnable() == true) {
      notifyInterval = (leaseTime / 4) + (long) ((float) leaseTime * (Math.random() * 0.25f));
      notifyInterval *= 1000;
      try {
        Thread.sleep(notifyInterval);
      } catch (InterruptedException e) {
      }
      dev.announce();
    }
  }
}
