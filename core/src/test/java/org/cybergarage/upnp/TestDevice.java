/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2012
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.upnp.device.InvalidDescriptionException;

/**
 * TestDevice is a minimal UPnP device for testing purposes.
 * Based on the reference implementation in mUPnP (C version).
 */
public class TestDevice {
  public static final String DEVICE_TYPE = "urn:schemas-upnp-org:device:CyberGarageTestDevice:1";
  public static final String FRIENDLY_NAME = "CyberGarage Test Device";

  private static final String DEVICE_DESCRIPTION =
      "<?xml version=\"1.0\"?>\n"
          + "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n"
          + "  <specVersion>\n"
          + "    <major>1</major>\n"
          + "    <minor>0</minor>\n"
          + "  </specVersion>\n"
          + "  <device>\n"
          + "    <deviceType>" + DEVICE_TYPE + "</deviceType>\n"
          + "    <friendlyName>" + FRIENDLY_NAME + "</friendlyName>\n"
          + "    <manufacturer>CyberGarage</manufacturer>\n"
          + "    <manufacturerURL>http://www.cybergarage.org</manufacturerURL>\n"
          + "    <modelDescription>CyberGarage Test Device for Unit Testing</modelDescription>\n"
          + "    <modelName>TestDevice</modelName>\n"
          + "    <modelNumber>1.0</modelNumber>\n"
          + "    <UDN>uuid:cybergarage-test-device-1</UDN>\n"
          + "  </device>\n"
          + "</root>";

  /**
   * Create a new test device instance.
   * 
   * @return A new Device instance configured as a test device
   * @throws InvalidDescriptionException if the device description is invalid
   */
  public static Device create() throws InvalidDescriptionException {
    Device device = new Device();
    device.loadDescription(DEVICE_DESCRIPTION);
    return device;
  }
}
