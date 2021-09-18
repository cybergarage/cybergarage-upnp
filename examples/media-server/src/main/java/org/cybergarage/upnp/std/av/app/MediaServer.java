/******************************************************************
*
*	MediaGate for CyberLink
*
*	Copyright (C) Satoshi Konno 2004
*
*	File : MediaServer.java
*
*	10/22/03
*		- first revision.
*
******************************************************************/

package org.cybergarage.upnp.std.av.app;

import java.util.prefs.*;

import org.cybergarage.util.*;
import org.cybergarage.upnp.device.*;
import org.cybergarage.upnp.std.av.server.*;
import org.cybergarage.upnp.std.av.server.directory.file.*;
import org.cybergarage.upnp.std.av.server.directory.mythtv.*;
import org.cybergarage.upnp.std.av.app.frame.*;
import org.cybergarage.upnp.std.av.app.frame.swing.*;
import org.cybergarage.upnp.std.av.server.object.format.*;

public class MediaServer extends org.cybergarage.upnp.std.av.server.MediaServer
{
    ////////////////////////////////////////////////
    // Constants
    ////////////////////////////////////////////////

    /**** Mode Option ****/
    private final static int MODE_OPT_MASK = 0x00FF;
    private final static int FILESYS_MODE = 0x0000;
    private final static int MYTHTV_MODE = 0x0001;

    /**** Support Option ****/
    private final static int SUPPORT_OPT_MASK = 0xFF00;
    private final static int FLASH_SUPPORT = 0x0100;

    private final static String MYTHTV_OPT_STRING_OLD = "-mythtv";
    private final static String MYTHTV_OPT_STRING = "--mythtv";
    private final static String VERBOSE_OPT_STRING = "-v";
    private final static String FLASH_OPT_STRING = "--flash";
    private static final String CONSOLE_OPT_STRING = "-console";

    ////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////

    public MediaServer(int option, boolean need_gui) throws InvalidDescriptionException 
    {
        super(MediaServer.DESCRIPTION, ContentDirectory.SCPD, ConnectionManager.SCPD);
        try {
            setOption(option);

            switch (getModeOption()){
            case FILESYS_MODE:
                {
                    addPlugIn(new ID3Format());
                    addPlugIn(new GIFFormat());
                    addPlugIn(new JPEGFormat());
                    addPlugIn(new PNGFormat());
                    addPlugIn(new MPEGFormat());
                    loadUserDirectories();
                }
                break;
            case MYTHTV_MODE:
                {
                    addPlugIn(new DefaultFormat());
                    MythDirectory mythDir = new MythDirectory();
                    addContentDirectory(mythDir);
                }
                break;
            }

            if ( need_gui ) {
                mediaFrame = new SwingFrame(this, isFileSystemMode());
            } else if ( Debug.isOn() ) {
                Debug.message("Starting in console mode");
                for (int n=0; n < getNContentDirectories(); n++) {
                    Directory dir = getContentDirectory(n);
                    Debug.message("serving content dir: " + dir.getFriendlyName());
                    int nItems = dir.getNContentNodes();
                    for (int x = 0; x < nItems; x++) {
                        Debug.message("\n" + dir.getNode(x));
                    }
                }

            }
        }
        catch (Exception e) {
            Debug.warning(e);
        }
    }

    ////////////////////////////////////////////////
    // Mode
    ////////////////////////////////////////////////

    private int option;

    private void setOption(int value)
    {
        option = value;
    }

    private int getOption()
    {
        return option;
    }

    private int getModeOption()
    {
        return (option & MODE_OPT_MASK);
    }

    private boolean isFileSystemMode()
    {
        return ((getModeOption() & MODE_OPT_MASK) == FILESYS_MODE) ? true : false;
    }

    private int getSupportOption()
    {
        return (option & SUPPORT_OPT_MASK);
    }

    ////////////////////////////////////////////////
    // Preferences (FileSystem)
    ////////////////////////////////////////////////

    private final static String DIRECTORY_PREFS_NAME = "directory";

    private Preferences prefs = null;

    private Preferences getUserPreferences()
    {
        if (prefs == null)
            prefs =	Preferences.userNodeForPackage(this.getClass());
        return prefs;
    }

    private Preferences getUserDirectoryPreferences()
    {
        return getUserPreferences().node(DIRECTORY_PREFS_NAME);
    }

    private void clearUserDirectoryPreferences()
    {
        try {
            Preferences dirPref = getUserDirectoryPreferences();
            String dirName[] = dirPref.keys();
            int dirCnt = dirName.length;
            for (int n=0; n<dirCnt; n++)
                dirPref.remove(dirName[n]);
        }
        catch (Exception e) {
            Debug.warning(e);
        }
    }

    private void loadUserDirectories()
    {
        try {
            Preferences dirPref = getUserDirectoryPreferences();
            String dirName[] = dirPref.keys();
            int dirCnt = dirName.length;
            Debug.message("Loadin Directories (" + dirCnt + ") ....");
            for (int n=0; n<dirCnt; n++) {
                String name = dirName[n];
                String path = dirPref.get(name, "");
                FileDirectory fileDir = new FileDirectory(name, path);
                addContentDirectory(fileDir);
                Debug.message("[" + n + "] = " + name + "," + path);
            }
        }
        catch (Exception e) {
            Debug.warning(e);
        }
    }

    private void saveUserDirectories()
    {
        clearUserDirectoryPreferences();

        ContentDirectory conDir = getContentDirectory();
        try {
            Preferences dirPref = getUserDirectoryPreferences();
            int dirCnt = conDir.getNDirectories();
            for (int n=0; n<dirCnt; n++) {
                Directory dir = conDir.getDirectory(n);
                if (!(dir instanceof FileDirectory))
                    continue;
                FileDirectory fileDir = (FileDirectory)dir;
                dirPref.put(fileDir.getFriendlyName(), fileDir.getPath());
            }
        }
        catch (Exception e) {
            Debug.warning(e);
        }
    }

    ////////////////////////////////////////////////
    // MediaServer
    ////////////////////////////////////////////////

    private MediaFrame mediaFrame;

    public MediaFrame getMediaFrame()
    {
        return mediaFrame;
    }

    ////////////////////////////////////////////////
    // start/stop
    ////////////////////////////////////////////////

    public boolean start()
    {
        return super.start();
    }

    public boolean stop()
    {
        if (getOption() == FILESYS_MODE)
            saveUserDirectories();
        return super.stop();
    }

    ////////////////////////////////////////////////
    // Debug
    ////////////////////////////////////////////////

    public static void debug(MediaServer mgate)
    {
        /*
          String sortCriteria = "+dc:date,+dc:title,+upnp:class";
          mgate.getContentDirectory().sortContentNodeList(new ContentNodeList(), sortCriteria);
        */
    }

    ////////////////////////////////////////////////
    // main
    ////////////////////////////////////////////////

    public static void main(String args[])
    {
        Debug.off();

        boolean need_gui = true;
        int mode = FILESYS_MODE;
        Debug.message("args = " + args.length);
        for (int n=0; n<args.length; n++) {
            Debug.message("  [" + n + "] = " + args[n]);
            if (MYTHTV_OPT_STRING.compareTo(args[n]) == 0)
                mode = MYTHTV_MODE;
            if (MYTHTV_OPT_STRING_OLD.compareTo(args[n]) == 0)
                mode = MYTHTV_MODE;
            if (VERBOSE_OPT_STRING.compareTo(args[n]) == 0)
                Debug.on();
            if (CONSOLE_OPT_STRING.compareTo(args[n]) == 0)
                need_gui = false;
        }

        try {
            MediaServer server = new MediaServer(mode, need_gui);
            debug(server);
            server.start();
        }
        catch (Exception e) {
            Debug.warning(e);
        }
    }

}

