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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * ControlPointTest validates device discovery functionality.
 * This test verifies that a ControlPoint can discover UPnP devices on the network.
 */
public class ControlPointTest {
  private ControlPoint controlPoint;
  private TestDevice testDevice;

  /**
   * Check if there are usable network interfaces available.
   * This prevents test failures in CI environments without network access.
   * 
   * @return true if at least one usable network interface is available
   */
  private boolean hasUsableNetworkInterface() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      if (interfaces == null) {
        return false;
      }
      
      while (interfaces.hasMoreElements()) {
        NetworkInterface ni = interfaces.nextElement();
        // Check if interface is up and not loopback
        if (ni.isUp() && !ni.isLoopback()) {
          // Check if it has at least one IP address
          if (ni.getInetAddresses().hasMoreElements()) {
            return true;
          }
        }
      }
      return false;
    } catch (SocketException e) {
      return false;
    }
  }

  // Wait time constants for device discovery
  // Device needs time to start HTTP server and SSDP sockets before responding to searches
  private static final int DEVICE_STARTUP_DELAY_MS = 500;
  // Poll interval balances responsiveness vs CPU usage during discovery wait
  private static final int DISCOVERY_POLL_INTERVAL_MS = 500;
  // Multiplier provides buffer beyond MX value for network delays and processing
  private static final int MAX_DISCOVERY_WAIT_MULTIPLIER = 3;

  /**
   * Wait for device to be discovered with polling mechanism.
   * 
   * @param cp the ControlPoint instance
   * @param expectedDeviceType the device type to search for
   * @param maxWaitMs maximum time to wait in milliseconds
   * @return true if device was discovered, false otherwise
   */
  private boolean waitForDeviceDiscovery(ControlPoint cp, String expectedDeviceType, int maxWaitMs) 
      throws InterruptedException {
    long startTime = System.currentTimeMillis();
    long endTime = startTime + maxWaitMs;
    
    while (System.currentTimeMillis() < endTime) {
      DeviceList deviceList = cp.getDeviceList();
      if (deviceList != null) {
        for (int i = 0; i < deviceList.size(); i++) {
          Device device = deviceList.getDevice(i);
          if (device != null && expectedDeviceType.equals(device.getDeviceType())) {
            return true;
          }
        }
      }
      Thread.sleep(DISCOVERY_POLL_INTERVAL_MS);
    }
    return false;
  }

  @BeforeEach
  public void setUp() {
    controlPoint = null;
    testDevice = null;
  }

  @AfterEach
  public void tearDown() {
    // Ensure resources are cleaned up even if test fails
    if (testDevice != null) {
      try {
        testDevice.stop();
      } catch (Exception e) {
        // Expected: Socket exceptions during cleanup are normal when shutting down network resources
        // Only log if it's an unexpected exception type
        if (!(e instanceof java.net.SocketException)) {
          System.err.println("Unexpected exception during testDevice cleanup: " + 
                           e.getClass().getSimpleName() + ": " + e.getMessage());
        }
      }
      testDevice = null;
    }
    
    if (controlPoint != null) {
      try {
        controlPoint.stop();
      } catch (Exception e) {
        // Expected: Socket exceptions during cleanup are normal when shutting down network resources
        // Only log if it's an unexpected exception type
        if (!(e instanceof java.net.SocketException)) {
          System.err.println("Unexpected exception during controlPoint cleanup: " + 
                           e.getClass().getSimpleName() + ": " + e.getMessage());
        }
      }
      controlPoint = null;
    }
  }

  @Test
  public void testDeviceDiscovery() throws Exception {
    // Skip test if no usable network interfaces are available
    assumeTrue(hasUsableNetworkInterface(), 
               "Skipping test: No usable network interfaces available");

    // Create and start test device FIRST
    testDevice = new TestDevice();
    boolean deviceStarted = testDevice.start();
    assertTrue(deviceStarted, "TestDevice should start successfully");

    // Give device time to fully initialize and start advertising
    Thread.sleep(DEVICE_STARTUP_DELAY_MS);

    // Create and start ControlPoint
    controlPoint = new ControlPoint();
    boolean cpStarted = controlPoint.start();
    assertTrue(cpStarted, "ControlPoint should start successfully");

    // Calculate maximum wait time based on MX value
    // The start() method automatically triggers a search
    int mx = controlPoint.getSearchMx();
    int maxWaitMs = mx * 1000 * MAX_DISCOVERY_WAIT_MULTIPLIER;
    
    // Wait for device to be discovered using polling mechanism
    String expectedDeviceType = TestDevice.TEST_DEVICE_TYPE;
    boolean testDeviceFound = waitForDeviceDiscovery(controlPoint, expectedDeviceType, maxWaitMs);
    
    if (!testDeviceFound) {
      // Try one more search and wait cycle if initial discovery failed
      controlPoint.search("upnp:rootdevice");
      testDeviceFound = waitForDeviceDiscovery(controlPoint, expectedDeviceType, maxWaitMs);
    }

    // Verify that the test device was discovered
    assertTrue(testDeviceFound, 
               "TestDevice with type " + expectedDeviceType + 
               " should be discovered by ControlPoint.");

    // Additional verification - find the device and check its properties
    DeviceList deviceList = controlPoint.getDeviceList();
    assertNotNull(deviceList, "Device list should not be null");
    
    Device discoveredDevice = null;
    for (int i = 0; i < deviceList.size(); i++) {
      Device device = deviceList.getDevice(i);
      if (device != null && expectedDeviceType.equals(device.getDeviceType())) {
        discoveredDevice = device;
        break;
      }
    }
    
    assertNotNull(discoveredDevice, "Should be able to retrieve discovered device");
    assertNotNull(discoveredDevice.getFriendlyName(), "Device should have a friendly name");
    assertNotNull(discoveredDevice.getUDN(), "Device should have a UDN");
    assertEquals(TestDevice.TEST_FRIENDLY_NAME, discoveredDevice.getFriendlyName(), 
                 "Friendly name should match");
  }
}
