/******************************************************************
 *
 *	CyberUtil for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Debug.java
 *
 *	Revision;
 *
 *	11/18/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Debug {

    private static final Logger logger = LoggerFactory.getLogger(Debug.class);

    public static void message(String s) {
        logger.info(s);
    }

    public static void warning(String s) {
        logger.warn(s);
    }

    public static void warning(String m, Exception e) {
        logger.warn(m, e);
    }

    public static void warning(Exception e) {
        logger.warn("", e);
    }
}
