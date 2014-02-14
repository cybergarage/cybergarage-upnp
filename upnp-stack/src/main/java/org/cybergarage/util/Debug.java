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

import java.io.PrintStream;

public final class Debug {

    public static final Debug debug = new Debug();

    private PrintStream out = System.out;


    private Debug() {

    }

    public synchronized PrintStream getOut() {
        return out;
    }

    public synchronized void setOut(PrintStream out) {
        this.out = out;
    }


    public static boolean enabled = false;

    public static Debug getDebug() {
        return Debug.debug;
    }

    public static void on() {
        enabled = true;
    }

    public static void off() {
        enabled = false;
    }

    public static boolean isOn() {
        return enabled;
    }

    public static void message(String s) {
        if (enabled) {
            Debug.debug.getOut().println("CyberGarage message : " + s);
        }
    }

    public static void message(String m1, String m2) {
        if (enabled) {
            Debug.debug.getOut().println("CyberGarage message : ");
            Debug.debug.getOut().println(m1);
            Debug.debug.getOut().println(m2);
        }
    }

    public static void warning(String s) {
        Debug.debug.getOut().println("CyberGarage warning : " + s);
    }

    public static void warning(String m, Exception e) {
        if (e.getMessage() == null) {
            Debug.debug.getOut().println("CyberGarage warning : " + m + " START");
            e.printStackTrace(Debug.debug.getOut());
            Debug.debug.getOut().println("CyberGarage warning : " + m + " END");
        } else {
            Debug.debug.getOut().println("CyberGarage warning : " + m + " (" + e.getMessage() + ")");
            e.printStackTrace(Debug.debug.getOut());
        }
    }

    public static void warning(Exception e) {
        warning(e.getMessage());
        e.printStackTrace(Debug.debug.getOut());
    }
}
