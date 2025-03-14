# cybergarage-upnp

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/cybergarage/cybergarage-upnp)
[![](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/maven.yml/badge.svg)](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/maven.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/org.cybergarage.upnp/core)](https://central.sonatype.com/search?q=g:org.cybergarage.upnp&smo=true)
[![doxygen](https://github.com/cybergarage/cybergarage-upnp/actions/workflows/doxygen.yml/badge.svg)](http://cybergarage.github.io/cybergarage-upnp/)

## Overview

UPnP™ [^1] architecture is an open network to enable discovery and control of networked devices and services, such as media servers and players at home. UPnP™ [^1] protocols are based on many standard, such as GENA, SSDP, SOAP, HTTPU and HTTP. Therefore you have to understand and implement these protocols to create your devices of UPnP™.

`cybergarage-upnp` is a UPnP™ development package for Java developers. controls these protocols automatically, and supports to create your devices and control points quickly.

[^1]: UPnP ™ is a certification mark of the UPnP™ Implementers Corporation.

## References

- [Programming Guide (Doxygen)](http://cybergarage.github.io/cybergarage-upnp/)
- [API Reference (Doxygen)](http://cybergarage.github.io/cybergarage-upnp/)

## Repositories

- [Maven Central Repository (org.cybergarage.upnp)](https://search.maven.org/search?q=g:org.cybergarage.upnp)
  - [`org.cybergarage.upnp:core`](https://search.maven.org/artifact/org.cybergarage.upnp/core)
    - UPnP standard protocol packages 
  - [`org.cybergarage.upnp:std`](https://search.maven.org/artifact/org.cybergarage.upnp/std)
    - UPnP standard devices packages based on `org.cybergarage.upnp:core`

## Examples

- UPnP control point examples
  - UPnP basic controller for UPnP devices
    - [UPnP multicast dump utility](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/upnpdump)
    - [UPnP control point utility](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/control-point)
  - UPnP controller for UPnP stardard devices
    - [UPnP Internet gateway utility ](https://github.com/cybergarage/cybergarage-upnp/tree/master/tools/igd-tool)
 
- UPnP device examples
  - UPnP stardard devices
    - [UPnP/AV media sever](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/media-server)
  - UPnP non-standard devices
    - [UPnP clock device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/clock)
    - [UPnP light device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/light)
    - [UPnP remote controller device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/)
    - [UPnP television device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/tv)
    - [UPnP air conditioner device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/air-conditioner)
    - [UPnP washer device](https://github.com/cybergarage/cybergarage-upnp/tree/master/examples/washer)
