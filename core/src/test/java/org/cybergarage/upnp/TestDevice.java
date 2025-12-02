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
    String deviceDescription = 
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
      "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">" +
      "  <specVersion>" +
      "    <major>1</major>" +
      "    <minor>0</minor>" +
      "  </specVersion>" +
      "  <device>" +
      "    <deviceType>" + TEST_DEVICE_TYPE + "</deviceType>" +
      "    <friendlyName>" + TEST_FRIENDLY_NAME + "</friendlyName>" +
      "    <manufacturer>" + TEST_MANUFACTURER + "</manufacturer>" +
      "    <manufacturerURL>http://www.cybergarage.org</manufacturerURL>" +
      "    <modelDescription>Test device for ControlPoint testing</modelDescription>" +
      "    <modelName>" + TEST_MODEL_NAME + "</modelName>" +
      "    <modelNumber>1.0</modelNumber>" +
      "    <modelURL>http://www.cybergarage.org</modelURL>" +
      "    <serialNumber>TEST-001</serialNumber>" +
      "    <UDN>uuid:" + UPnP.createUUID() + "</UDN>" +
      "  </device>" +
      "</root>";
    
    // Create device instance and load description
    device = new Device();
    device.loadDescription(deviceDescription);
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
