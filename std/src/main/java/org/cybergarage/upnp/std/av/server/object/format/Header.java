/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003
 **
 *	File: FileFormat.java
 *
 *	Revision;
 *
 *	12/04/03
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.server.object.format;

import java.io.*;

import org.cybergarage.util.*;

public abstract class Header {
  ////////////////////////////////////////////////
  // Header ID
  ////////////////////////////////////////////////

  public static final byte[] getID(InputStream inputStream, int headerOffset, int headerSize) {
    int headerCnt = headerOffset + headerSize;
    byte header[] = new byte[headerCnt];
    try {
      DataInputStream dataIn = new DataInputStream(inputStream);
      for (int n = 0; n < headerCnt; n++) {
        byte readByte = dataIn.readByte();
        if (n < headerOffset) continue;
        header[n - headerOffset] = readByte;
      }
      dataIn.close();
    } catch (EOFException eofe) {
    } catch (Exception e) {
      Debug.warning(e);
    }
    return header;
  }

  public static final byte[] getID(InputStream inputStream, int headerSize) {
    return getID(inputStream, 0, headerSize);
  }

  public static final String getIDString(
      InputStream inputStream, int headerOffset, int headerSize) {
    return new String(getID(inputStream, headerOffset, headerSize));
  }

  public static final String getIDString(InputStream inputStream, int headerSize) {
    return new String(getID(inputStream, headerSize));
  }

  public static final byte[] getID(File file, int headerSize) {
    try {
      return getID(new FileInputStream(file), headerSize);
    } catch (Exception e) {
      Debug.warning(e);
      return new byte[0];
    }
  }

  public static final String getIDString(File file, int headerOffset, int headerSize) {
    try {
      return getIDString(new FileInputStream(file), headerOffset, headerSize);
    } catch (Exception e) {
      Debug.warning(e);
      return "";
    }
  }

  public static final String getIDString(File file, int headerSize) {
    return getIDString(file, 0, headerSize);
  }

  ////////////////////////////////////////////////
  // Suffix
  ////////////////////////////////////////////////

  public static final String getSuffix(File file) {
    String fname = file.getName();
    int idx = fname.lastIndexOf(".");
    if (idx < 0) return "";
    return fname.substring(idx + 1, fname.length());
  }
}
