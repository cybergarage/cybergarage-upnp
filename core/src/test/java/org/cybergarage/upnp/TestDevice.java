/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2024
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.upnp.device.InvalidDescriptionException;

/**
 * TestDevice is a minimal UPnP device for testing purposes.
 * It creates a simple root device with basic attributes for device discovery testing.
 */
public class TestDevice {
  public static final String TEST_DEVICE_TYPE = "urn:schemas-upnp-org:device:CyberGarageTestDevice:1";
  public static final String TEST_FRIENDLY_NAME = "CyberGarage Test Device";
  public static final String TEST_MANUFACTURER = "CyberGarage";
  public static final String TEST_MODEL_NAME = "TestDevice";
  
  private Device device;

  public TestDevice() throws InvalidDescriptionException {
    // Create a minimal but complete UPnP device description XML
    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
       .append("<root xmlns=\"urn:schemas-upnp-org:device-1-0\">")
       .append("  <specVersion>")
       .append("    <major>1</major>")
       .append("    <minor>0</minor>")
       .append("  </specVersion>")
       .append("  <device>")
       .append("    <deviceType>").append(TEST_DEVICE_TYPE).append("</deviceType>")
       .append("    <friendlyName>").append(TEST_FRIENDLY_NAME).append("</friendlyName>")
       .append("    <manufacturer>").append(TEST_MANUFACTURER).append("</manufacturer>")
       .append("    <manufacturerURL>http://www.cybergarage.org</manufacturerURL>")
       .append("    <modelDescription>Test device for ControlPoint testing</modelDescription>")
       .append("    <modelName>").append(TEST_MODEL_NAME).append("</modelName>")
       .append("    <modelNumber>1.0</modelNumber>")
       .append("    <modelURL>http://www.cybergarage.org</modelURL>")
       .append("    <serialNumber>TEST-001</serialNumber>")
       .append("    <UDN>uuid:").append(UPnP.createUUID()).append("</UDN>")
       .append("  </device>")
       .append("</root>");
    
    // Create device instance and load description
    device = new Device();
    device.loadDescription(xml.toString());
  }

  /**
   * Start the test device
   * @return true if the device started successfully
   */
  public boolean start() {
    return device.start();
  }

  /**
   * Stop the test device
   * @return true if the device stopped successfully
   */
  public boolean stop() {
    return device.stop();
  }

  /**
   * Get the underlying Device instance
   * @return the Device object
   */
  public Device getDevice() {
    return device;
  }

  /**
   * Get the device type
   * @return the device type string
   */
  public String getDeviceType() {
    return device.getDeviceType();
  }

  /**
   * Get the UDN (Unique Device Name)
   * @return the UDN string
   */
  public String getUDN() {
    return device.getUDN();
  }
}
