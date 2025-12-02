/******************************************************************
 *
 * CyberLink for Java
 *
 * Copyright (C) Satoshi Konno 2002-2024
 *
 * This is licensed under BSD-style license, see file COPYING.
 *
 ******************************************************************/

package org.cybergarage.upnp.controlpoint;

import static org.junit.Assume.assumeTrue;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.upnp.testdevice.TestDevice;

/**
 * Test class for ControlPoint device discovery functionality.
 * Tests the SSDP search and device detection capabilities of the ControlPoint.
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
   * Check if network interface is available for testing.
   * Tests should be skipped if no network interface is available.
   *
   * @return true if network interface is available
   */
  private boolean isNetworkAvailable() {
    try {
      int nAddresses = HostInterface.getNHostAddresses();
      if (nAddresses <= 0) {
        return false;
      }
      // Check if we have at least one non-loopback interface
      for (int i = 0; i < nAddresses; i++) {
        String hostAddress = HostInterface.getHostAddress(i);
        if (hostAddress != null && !hostAddress.isEmpty() && !hostAddress.equals("127.0.0.1")) {
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Test ControlPoint device discovery functionality.
   * 
   * This test:
   * 1. Creates and starts a TestDevice
   * 2. Creates and starts a ControlPoint
   * 3. Performs an M-SEARCH for root devices
   * 4. Waits for device discovery
   * 5. Verifies that the TestDevice is discovered
   * 6. Properly cleans up resources
   * 
   * @throws Exception if an error occurs during the test
   */
  public void testDeviceDiscovery() throws Exception {
    // Skip test if network is not available
    assumeTrue("Network interface not available, skipping test", isNetworkAvailable());

    TestDevice testDevice = null;
    ControlPoint controlPoint = null;

    try {
      // 1. Create and start TestDevice
      testDevice = new TestDevice();
      boolean deviceStarted = testDevice.start();
      assertTrue("TestDevice should start successfully", deviceStarted);

      // Give the device time to initialize and send initial announcements
      Thread.sleep(500);

      // 2. Create and start ControlPoint
      controlPoint = new ControlPoint();
      boolean cpStarted = controlPoint.start();
      assertTrue("ControlPoint should start successfully", cpStarted);

      // 3. Perform M-SEARCH for root devices
      int mx = SSDP.DEFAULT_MSEARCH_MX;
      controlPoint.search(ST.ROOT_DEVICE, mx);

      // 4. Wait for device discovery
      // Wait time should be sufficient for M-SEARCH responses: MX * 1000 * 2
      // MX is in seconds, so we convert to milliseconds and double it for safety
      int waitTimeMs = mx * 1000 * 2;
      Thread.sleep(waitTimeMs);

      // 5. Verify that the TestDevice is discovered
      DeviceList deviceList = controlPoint.getDeviceList();
      assertNotNull("Device list should not be null", deviceList);

      // Debug output for troubleshooting
      if (deviceList.size() == 0) {
        System.err.println("WARNING: No devices discovered. This may indicate:");
        System.err.println("  - Network interface issues");
        System.err.println("  - Firewall blocking SSDP multicast");
        System.err.println("  - Port conflicts");
      } else {
        System.out.println("Discovered " + deviceList.size() + " device(s):");
        for (int i = 0; i < deviceList.size(); i++) {
          Device dev = deviceList.getDevice(i);
          System.out.println("  - " + dev.getFriendlyName() + " (" + dev.getDeviceType() + ")");
        }
      }

      // Look for the TestDevice in the discovered devices
      boolean testDeviceFound = false;
      for (int i = 0; i < deviceList.size(); i++) {
        Device dev = deviceList.getDevice(i);
        if (dev.isDeviceType(TestDevice.DEVICE_TYPE)) {
          testDeviceFound = true;
          assertEquals(
              "Friendly name should match", TestDevice.FRIENDLY_NAME, dev.getFriendlyName());
          assertEquals("Device type should match", TestDevice.DEVICE_TYPE, dev.getDeviceType());
          break;
        }
      }

      assertTrue(
          "TestDevice with type "
              + TestDevice.DEVICE_TYPE
              + " should be discovered by ControlPoint",
          testDeviceFound);

    } finally {
      // 6. Cleanup resources in proper order
      // Stop TestDevice first, then ControlPoint
      if (testDevice != null) {
        testDevice.stop();
      }

      if (controlPoint != null) {
        controlPoint.stop();
      }

      // Give time for cleanup
      Thread.sleep(200);
    }
  }

  /**
   * Test ControlPoint basic start and stop operations.
   * 
   * @throws Exception if an error occurs during the test
   */
  public void testControlPointStartStop() throws Exception {
    // Skip test if network is not available
    assumeTrue("Network interface not available, skipping test", isNetworkAvailable());

    ControlPoint controlPoint = new ControlPoint();

    // Test start
    boolean started = controlPoint.start();
    assertTrue("ControlPoint should start successfully", started);

    // Test that we can get an empty device list
    DeviceList deviceList = controlPoint.getDeviceList();
    assertNotNull("Device list should not be null even when empty", deviceList);

    // Test stop
    boolean stopped = controlPoint.stop();
    assertTrue("ControlPoint should stop successfully", stopped);
  }

  /**
   * Test TestDevice basic operations.
   * 
   * @throws Exception if an error occurs during the test
   */
  public void testTestDeviceCreation() throws Exception {
    // Skip test if network is not available
    assumeTrue("Network interface not available, skipping test", isNetworkAvailable());

    TestDevice testDevice = new TestDevice();
    assertNotNull("TestDevice should be created", testDevice);

    // Verify device properties
    assertEquals("Device type should match", TestDevice.DEVICE_TYPE, testDevice.getDeviceType());
    assertEquals(
        "Friendly name should match", TestDevice.FRIENDLY_NAME, testDevice.getFriendlyName());
    assertEquals("Manufacturer should match", TestDevice.MANUFACTURER, testDevice.getManufacture());

    // Test start and stop
    boolean started = testDevice.start();
    assertTrue("TestDevice should start successfully", started);

    boolean stopped = testDevice.stop();
    assertTrue("TestDevice should stop successfully", stopped);
  }
}
