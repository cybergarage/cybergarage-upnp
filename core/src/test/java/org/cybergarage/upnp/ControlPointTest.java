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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cybergarage.upnp.device.ST;

/**
 * ControlPoint test for device discovery functionality.
 * Based on the reference test from mUPnP (C version).
 */
public class ControlPointTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public ControlPointTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(ControlPointTest.class);
  }

  /**
   * Test that a ControlPoint can discover a test device.
   * 
   * This test:
   * 1. Creates and starts a ControlPoint
   * 2. Creates and starts a test Device
   * 3. Performs SSDP search for root devices
   * 4. Waits for discovery (MX * 2 seconds)
   * 5. Verifies the test device is found in the device list
   * 6. Properly cleans up resources
   */
  public void testDeviceDiscovery() throws Exception {
    ControlPoint cp = null;
    Device testDev = null;

    try {
      // Create and start ControlPoint
      cp = new ControlPoint();
      assertNotNull("ControlPoint should be created", cp);
      assertTrue("ControlPoint should start", cp.start());

      // Create and start test device
      testDev = TestDevice.create();
      assertNotNull("Test device should be created", testDev);
      assertTrue("Test device should start", testDev.start());

      // Perform SSDP search for root devices
      cp.search(ST.ROOT_DEVICE);

      // Wait for discovery - MX (search timeout) * 2
      // This allows time for SSDP multicast and device response
      int searchMx = cp.getSearchMx();
      int waitTimeMs = searchMx * 1000 * 2;
      Thread.sleep(waitTimeMs);

      // Verify at least one device was discovered
      DeviceList deviceList = cp.getDeviceList();
      int deviceCount = deviceList.size();
      assertTrue(
          "At least one device should be discovered, found: " + deviceCount, deviceCount > 0);

      // Verify our test device is in the list
      boolean testDeviceFound = false;
      for (int i = 0; i < deviceCount; i++) {
        Device dev = deviceList.getDevice(i);
        String deviceType = dev.getDeviceType();
        if (TestDevice.DEVICE_TYPE.equals(deviceType)) {
          testDeviceFound = true;
          break;
        }
      }
      assertTrue(
          "Test device with type "
              + TestDevice.DEVICE_TYPE
              + " should be found in device list",
          testDeviceFound);

    } finally {
      // Clean up resources in reverse order
      if (testDev != null) {
        testDev.stop();
      }
      if (cp != null) {
        cp.stop();
      }
    }
  }
}
