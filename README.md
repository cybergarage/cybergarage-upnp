# cybergarage-upnp

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/cybergarage/cybergarage-upnp)
[![](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/maven.yml/badge.svg)](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/maven.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/org.cybergarage.upnp/core)](https://central.sonatype.com/search?q=g:org.cybergarage.upnp&smo=true)
[![doxygen](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/doxygen.yml/badge.svg)](http://cybergarage.github.io/cybergarage-upnp/)

## Overview

The UPnP™ [^1] architecture is an open network protocol that enables the discovery and control of networked devices and services, such as media servers and players, within a home network. UPnP™ protocols are based on several standards, including GENA, SSDP, SOAP, HTTPU, and HTTP. Therefore, understanding and implementing these protocols is essential for creating UPnP™ devices.

`cybergarage-upnp` is a UPnP™ development package designed for Java developers. It automates the control of these protocols and supports the rapid creation of UPnP™ devices and control points. This package simplifies the development process by handling the complex aspects of UPnP™ protocol implementation, allowing developers to focus on the functionality of their devices and services.

## References

- [Programming Guide (Doxygen)](http://cybergarage.github.io/cybergarage-upnp/)
- [API Reference (Doxygen)](http://cybergarage.github.io/cybergarage-upnp/)

## Repositories

The `cybergarage-upnp` package is available in the Maven Central Repository under the group ID `org.cybergarage.upnp`. It consists of the following modules:

- [`org.cybergarage.upnp:core`](https://search.maven.org/artifact/org.cybergarage.upnp/core)
  - This module contains the core UPnP standard protocol packages, providing the fundamental building blocks for UPnP™ device and control point development.
- [`org.cybergarage.upnp:std`](https://search.maven.org/artifact/org.cybergarage.upnp/std)
  - This module includes packages for standard UPnP devices, built on top of the core module. It offers pre-defined implementations of common UPnP™ devices, facilitating quicker development.

## Examples

The `cybergarage-upnp` package includes several examples to help developers get started with UPnP™ development. These examples demonstrate how to create both control points and devices using the package.

- UPnP control point examples:
  - Basic controllers for UPnP devices:
    - [UPnP multicast dump utility](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/upnpdump): A utility for dumping multicast messages from UPnP devices.
    - [UPnP control point utility](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/control-point): A utility for controlling UPnP devices.
  - Controllers for standard UPnP devices:
    - [UPnP Internet gateway utility](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/igd-tool): A tool for managing UPnP Internet Gateway Devices (IGD).

- UPnP device examples:
  - Standard UPnP devices:
    - [UPnP/AV media server](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/media-server): An example of a media server that complies with the UPnP/AV standard.
  - Non-standard UPnP devices:
    - [UPnP clock device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/clock): An example of a custom clock device.
    - [UPnP light device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/light): An example of a custom light device.
    - [UPnP remote controller device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/): An example of a custom remote controller device.
    - [UPnP television device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/tv): An example of a custom television device.
    - [UPnP air conditioner device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/air-conditioner): An example of a custom air conditioner device.
    - [UPnP washer device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/washer): An example of a custom washer device.

[^1]: UPnP™ is a certification mark of the UPnP™ Implementers Corporation.