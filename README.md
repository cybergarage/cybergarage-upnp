# cybergarage-upnp

## Overview

`cybergarage-upnp` is a UPnP™ development package for Java developers. controls these protocols automatically, and supports to create your devices and control points quickly.

UPnP™ \* architecture is an open network to enable discovery and control of networked devices and services, such as media servers and players at home.

UPnP™ \* protocols are based on many standard, such as GENA, SSDP, SOAP, HTTPU and HTTP. Therefore you have to understand and implement these protocols to create your devices of UPnP™.

\* UPnP ™ is a certification mark of the UPnP™ Implementers Corporation.

## References

- [Programming Guide](doc/cybergarage-upnp-prgguide.pdf)
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
    - [UPnP multicast dump utility](tools/upnpdump)
    - [UPnP control point utility](tools/control-point)
  - UPnP controller for UPnP stardard devices
    - [UPnP Internet gateway utility ](tools/igd-tool)
 
- UPnP device examples
  - UPnP stardard devices
    - [UPnP/AV media sever](examples/media-server)
  - UPnP non-standard devices
    - [UPnP clock device](examples/clock)
    - [UPnP light device](examples/light)
    - [UPnP remote controller device](examples/)
    - [UPnP television device](examples/tv)
    - [UPnP air conditioner device](examples/air-conditioner)
    - [UPnP washer device](examples/washer)
