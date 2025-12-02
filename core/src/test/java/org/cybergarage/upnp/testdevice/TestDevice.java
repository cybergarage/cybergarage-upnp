/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2024
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp.testdevice;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.InvalidDescriptionException;

/**
 * Test UPnP device for testing ControlPoint discovery functionality.
 * This is a minimal device implementation with a custom device type
 * used for verifying SSDP search and device detection.
 */
public class TestDevice extends Device {
  public static final String DEVICE_TYPE = "urn:schemas-upnp-org:device:CyberGarageTestDevice:1";
  public static final String FRIENDLY_NAME = "CyberGarage Test Device";
  public static final String MANUFACTURER = "CyberGarage";
  public static final String MANUFACTURER_URL = "http://www.cybergarage.org";
  public static final String MODEL_DESCRIPTION = "Test Device for ControlPoint Testing";
  public static final String MODEL_NAME = "TestDevice";
  public static final String MODEL_NUMBER = "1.0";

  private static final String DEVICE_DESCRIPTION =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n"
          + "  <specVersion>\n"
          + "    <major>1</major>\n"
          + "    <minor>0</minor>\n"
          + "  </specVersion>\n"
          + "  <device>\n"
          + "    <deviceType>" + DEVICE_TYPE + "</deviceType>\n"
          + "    <friendlyName>" + FRIENDLY_NAME + "</friendlyName>\n"
          + "    <manufacturer>" + MANUFACTURER + "</manufacturer>\n"
          + "    <manufacturerURL>" + MANUFACTURER_URL + "</manufacturerURL>\n"
          + "    <modelDescription>" + MODEL_DESCRIPTION + "</modelDescription>\n"
          + "    <modelName>" + MODEL_NAME + "</modelName>\n"
          + "    <modelNumber>" + MODEL_NUMBER + "</modelNumber>\n"
          + "    <UDN>uuid:cybergarage-test-device-001</UDN>\n"
          + "  </device>\n"
          + "</root>";

  /**
   * Creates a new TestDevice instance.
   * 
   * @throws InvalidDescriptionException if the device description is invalid
   */
  public TestDevice() throws InvalidDescriptionException {
    super();
    loadDescription(DEVICE_DESCRIPTION);
    // Set a reasonable lease time for testing (default is 1800 seconds)
    // Using shorter lease time for tests to avoid long waits
    setLeaseTime(60);
  }

  /**
   * Starts the test device.
   * 
   * @return true if the device started successfully, false otherwise
   */
  @Override
  public boolean start() {
    return super.start();
  }

  /**
   * Stops the test device.
   * 
   * @return true if the device stopped successfully, false otherwise
   */
  @Override
  public boolean stop() {
    return super.stop();
  }

  /**
   * Gets the device type of this test device.
   * 
   * @return the device type string
   */
  public String getDeviceType() {
    return DEVICE_TYPE;
  }
}
