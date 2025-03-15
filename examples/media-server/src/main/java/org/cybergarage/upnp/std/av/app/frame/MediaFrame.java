/******************************************************************
 *
 *	MediaGate for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2004
 *
 *	File: MediaFrame.java
 *
 *	Revision;
 *
 *	01/24/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.app.frame;

import org.cybergarage.upnp.std.av.app.*;

public abstract class MediaFrame {
  ////////////////////////////////////////////////
  // Constuctor
  ////////////////////////////////////////////////

  public MediaFrame(MediaServer server) {
    this.server = server;
  }

  ////////////////////////////////////////////////
  // Constuctor
  ////////////////////////////////////////////////

  private MediaServer server;

  public MediaServer getMediaServer() {
    return server;
  }
}
