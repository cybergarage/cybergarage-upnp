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

  /**
   * Get the search MX value from ControlPoint using reflection to handle API differences.
   * Tries getSearchMx() first, then getSSDPSearchMX() as fallback.
   * 
   * @param cp the ControlPoint instance
   * @return the MX value, defaults to 3 if not found
   */
  private int getSearchMxValue(ControlPoint cp) {
    try {
      // Try getSearchMx() first
      java.lang.reflect.Method method = cp.getClass().getMethod("getSearchMx");
      return (Integer) method.invoke(cp);
    } catch (Exception e1) {
      try {
        // Try getSSDPSearchMX() as fallback
        java.lang.reflect.Method method = cp.getClass().getMethod("getSSDPSearchMX");
        return (Integer) method.invoke(cp);
      } catch (Exception e2) {
        // Default to 3 seconds if methods not found
        return 3;
      }
    }
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
        // Ignore cleanup exceptions
      }
      testDevice = null;
    }
    
    if (controlPoint != null) {
      try {
        controlPoint.stop();
      } catch (Exception e) {
        // Ignore cleanup exceptions
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
    Thread.sleep(1000);

    // Create and start ControlPoint
    controlPoint = new ControlPoint();
    boolean cpStarted = controlPoint.start();
    assertTrue(cpStarted, "ControlPoint should start successfully");

    // Get MX value and calculate wait time
    // Wait for MX * 2 seconds to allow for device discovery
    // The start() method automatically triggers a search
    int mx = getSearchMxValue(controlPoint);
    int waitTimeMs = mx * 1000 * 2;
    
    // Wait for device to be discovered
    Thread.sleep(waitTimeMs);

    // Search again to ensure we capture the device
    controlPoint.search("upnp:rootdevice");
    
    // Wait again for search response
    Thread.sleep(waitTimeMs);

    // Verify that the test device was discovered
    DeviceList deviceList = controlPoint.getDeviceList();
    assertNotNull(deviceList, "Device list should not be null");

    // Search for our test device in the discovered devices
    boolean testDeviceFound = false;
    String expectedDeviceType = TestDevice.TEST_DEVICE_TYPE;
    
    for (int i = 0; i < deviceList.size(); i++) {
      Device device = deviceList.getDevice(i);
      if (device != null) {
        String deviceType = device.getDeviceType();
        if (expectedDeviceType.equals(deviceType)) {
          testDeviceFound = true;
          // Additional verification
          assertNotNull(device.getFriendlyName(), "Device should have a friendly name");
          assertNotNull(device.getUDN(), "Device should have a UDN");
          break;
        }
      }
    }

    assertTrue(testDeviceFound, 
               "TestDevice with type " + expectedDeviceType + 
               " should be discovered by ControlPoint. Found " + 
               deviceList.size() + " device(s).");
  }
}
