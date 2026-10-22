# Historical Java adoption and use

`cybergarage-upnp` is the Java project previously published under names including **CyberLink for Java**, **CyberLinkForJava** and **CyberLink4Java**. These are historical names of the same Java project, not separate libraries or adoption counts. The following public records document its historical use. Sources were checked on October 22, 2026. These records do not establish continued use in current releases, endorsement or a partnership. C/C++ implementations are outside this list.

## Commercial applications and products

### [Panasonic Blu-ray Remote 2012 — Android](https://av.jpn.support.panasonic.com/support/global/cs/bd/bremote/2012/android/agreement.html)

**Evidence: Official license notice.** Sections 1 and 10 of Panasonic’s EULA identify the Android application and list CyberLinkForJava with Satoshi Konno’s copyright notice. This documents an app component, not adoption in the connected Blu-ray player’s firmware.

### [Panasonic DIGA Player — Android](https://av.jpn.support.panasonic.com/support/global/cs/bd/diga_player/2013/android/agreement.html)

**Evidence: Official license notice.** Panasonic’s EULA lists CyberLinkForJava among the Android app’s open-source components in section 10. The document is dated April 1, 2012; the URL’s 2013 directory does not establish when adoption began. This is separate from Blu-ray Remote and VIERA remote.

### [Panasonic ELUGA-related smartphone integration engine](https://panasonic.jp/car/cus/ACC/eluga/opensource.html)

**Evidence: Official license notice.** Panasonic’s notice lists CyberLink for Java in its smartphone external-device integration engine (3). The notice supports this engine-level use, without establishing inclusion in every ELUGA handset or any particular model.

### [Geniatech Netstream4X](https://opensource.geniatech.eu/Netstream4X/1.1.0.402r1/Geniatech_Netstream4x_OPEN%20SOURCE%20SOFTWARE%20NOTICE_v20170930_v5_DVD.pdf#page=183)

**Evidence: Official license notice.** Page 183 of the OSS notice for Netstream4X/1.1.0-402r1 (DVD) lists CyberLink4Java master-20121110 and Satoshi Konno’s copyright. This is a product-specific notice, not evidence of an Android application or other product versions.

## SDKs and reference implementations

### [NHK / nhkrd — Hybridcast-Connect](https://github.com/nhkrd/hyconet4j)

**Evidence: Official development documentation.** The Java reference SDK’s README and the related [Android sample README](https://github.com/nhkrd/hyconet-android-sample) list `cybergarage-upnp-core-2.1.1.jar`, link to this project and explain bundling a JAR built from source with Maven. The sample’s [license](https://github.com/nhkrd/hyconet-android-sample/blob/master/LICENSE.txt) identifies NHK. The SDK and sample form one related case; these sources do not establish use across NHK’s consumer apps or broadcasting equipment.

### [POLYV — Android video SDK](https://help.polyv.net/vod/android/)

**Evidence: Official development documentation.** The January 8, 2019 changelog entry for v2.7.0 introduced screencasting and included CyberGarage-upnp obfuscation settings for `org.cybergarage.**`. This documents integration configuration, without establishing a direct dependency, continued use in the latest SDK or adoption by all customer apps.

## OSS use and derived libraries

### [geniusgithub/MediaPlayer](https://github.com/geniusgithub/MediaPlayer#thanks)

**Evidence: Public source.** The Android DLNA player’s README credits CyberGarage as its UPnP framework and identifies CyberLink4Java as its source. This is an OSS application example.

### [xiongwei-git/AndroidVideoPlayer](https://github.com/xiongwei-git/AndroidVideoPlayer)

**Evidence: Public source.** Its [DLNAService.java](https://github.com/xiongwei-git/AndroidVideoPlayer/blob/master/wkvideoplayer/src/main/java/com/android/tedcoder/wkvideoplayer/dlna/service/DLNAService.java) imports and instantiates `org.cybergarage.upnp.ControlPoint`, then passes it to a background discovery thread. This establishes published source use, not a commercial deployment.

### [CharonChui/CyberLink4Android](https://github.com/CharonChui/CyberLink4Android#cyberlink4android)

**Evidence: Project README.** The project describes combining CyberLink4Java with CyberGarage’s Android JAR to create an Android-oriented derivative library. It is a library derivation, not a separate finished-app adoption claim.

### [I2P](https://github.com/i2p/i2p.i2p)

**Evidence: Public source.** The Java [package documentation](https://github.com/i2p/i2p.i2p/blob/master/router/java/src/org/cybergarage/package-info.java) identifies the bundled UPnP library as CyberGarage CyberLink version 3.0 with modifications for I2P. This supports embedded and modified OSS use; it does not establish inclusion in an Android release.

## Earlier examples awaiting source revalidation

The following older website claims are recorded here as unresolved leads, not verified adoption cases. Adopter-side evidence could not be revalidated in this update. They are not additional confirmed cases.

### Apache Felix

The previous CyberGarage website described the [Apache Felix](http://felix.apache.org/site/index.html) UPnP base driver as using this Java library. The [historical architecture link](http://felix.apache.org/site/upnp-driver-architecture.html) and its [current documentation location](https://felix.apache.org/documentation/subprojects/apache-felix-upnp/upnp-driver-architecture.html) could not be retrieved for verification. Its OSS-use attribution remains pending primary-source revalidation.

## Evidence limits

The records above establish only the documented app, product version, SDK integration, published source use or derivation. They do not establish sales, user or customer counts, contracts, manufacturer endorsement or a partnership. A fork, compatibility list or interoperability test alone is not adoption evidence. Nero is not included: a license statement that software may contain a component does not identify a particular adopting product.

[Back to the project README](../README.md)
