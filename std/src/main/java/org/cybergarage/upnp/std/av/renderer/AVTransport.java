/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003
 *
 *	File : AVTransport.java
 *
 *	Revision:
 *
 *	02/22/08
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.renderer;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;

public class AVTransport implements ActionListener, QueryListener {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String SERVICE_TYPE = "urn:schemas-upnp-org:service:AVTransport:1";

  // Browse Action

  public static final String TRANSPORTSTATE = "TransportState";
  public static final String TRANSPORTSTATUS = "TransportStatus";
  public static final String PLAYBACKSTORAGEMEDIUM = "PlaybackStorageMedium";
  public static final String RECORDSTORAGEMEDIUM = "RecordStorageMedium";
  public static final String POSSIBLEPLAYBACKSTORAGEMEDIA = "PossiblePlaybackStorageMedia";
  public static final String POSSIBLERECORDSTORAGEMEDIA = "PossibleRecordStorageMedia";
  public static final String CURRENTPLAYMODE = "CurrentPlayMode";
  public static final String TRANSPORTPLAYSPEED = "TransportPlaySpeed";
  public static final String RECORDMEDIUMWRITESTATUS = "RecordMediumWriteStatus";
  public static final String CURRENTRECORDQUALITYMODE = "CurrentRecordQualityMode";
  public static final String POSSIBLERECORDQUALITYMODES = "PossibleRecordQualityModes";
  public static final String NUMBEROFTRACKS = "NumberOfTracks";
  public static final String CURRENTTRACK = "CurrentTrack";
  public static final String CURRENTTRACKDURATION = "CurrentTrackDuration";
  public static final String CURRENTMEDIADURATION = "CurrentMediaDuration";
  public static final String CURRENTTRACKMETADATA = "CurrentTrackMetaData";
  public static final String CURRENTTRACKURI = "CurrentTrackURI";
  public static final String AVTRANSPORTURI = "AVTransportURI";
  public static final String AVTRANSPORTURIMETADATA = "AVTransportURIMetaData";
  public static final String NEXTAVTRANSPORTURI = "NextAVTransportURI";
  public static final String NEXTAVTRANSPORTURIMETADATA = "NextAVTransportURIMetaData";
  public static final String RELATIVETIMEPOSITION = "RelativeTimePosition";
  public static final String ABSOLUTETIMEPOSITION = "AbsoluteTimePosition";
  public static final String RELATIVECOUNTERPOSITION = "RelativeCounterPosition";
  public static final String ABSOLUTECOUNTERPOSITION = "AbsoluteCounterPosition";
  public static final String CURRENTTRANSPORTACTIONS = "CurrentTransportActions";
  public static final String LASTCHANGE = "LastChange";
  public static final String SETAVTRANSPORTURI = "SetAVTransportURI";
  public static final String INSTANCEID = "InstanceID";
  public static final String CURRENTURI = "CurrentURI";
  public static final String CURRENTURIMETADATA = "CurrentURIMetaData";
  public static final String SETNEXTAVTRANSPORTURI = "SetNextAVTransportURI";
  public static final String NEXTURI = "NextURI";
  public static final String NEXTURIMETADATA = "NextURIMetaData";
  public static final String GETMEDIAINFO = "GetMediaInfo";
  public static final String NRTRACKS = "NrTracks";
  public static final String MEDIADURATION = "MediaDuration";
  public static final String PLAYMEDIUM = "PlayMedium";
  public static final String RECORDMEDIUM = "RecordMedium";
  public static final String WRITESTATUS = "WriteStatus";
  public static final String GETTRANSPORTINFO = "GetTransportInfo";
  public static final String CURRENTTRANSPORTSTATE = "CurrentTransportState";
  public static final String CURRENTTRANSPORTSTATUS = "CurrentTransportStatus";
  public static final String CURRENTSPEED = "CurrentSpeed";
  public static final String GETPOSITIONINFO = "GetPositionInfo";
  public static final String TRACK = "Track";
  public static final String TRACKDURATION = "TrackDuration";
  public static final String TRACKMETADATA = "TrackMetaData";
  public static final String TRACKURI = "TrackURI";
  public static final String RELTIME = "RelTime";
  public static final String ABSTIME = "AbsTime";
  public static final String RELCOUNT = "RelCount";
  public static final String ABSCOUNT = "AbsCount";
  public static final String GETDEVICECAPABILITIES = "GetDeviceCapabilities";
  public static final String PLAYMEDIA = "PlayMedia";
  public static final String RECMEDIA = "RecMedia";
  public static final String RECQUALITYMODES = "RecQualityModes";
  public static final String GETTRANSPORTSETTINGS = "GetTransportSettings";
  public static final String PLAYMODE = "PlayMode";
  public static final String RECQUALITYMODE = "RecQualityMode";
  public static final String STOP = "Stop";
  public static final String PLAY = "Play";
  public static final String SPEED = "Speed";
  public static final String PAUSE = "Pause";
  public static final String RECORD = "Record";
  public static final String SEEK = "Seek";
  public static final String UNIT = "Unit";
  public static final String TARGET = "Target";
  public static final String NEXT = "Next";
  public static final String PREVIOUS = "Previous";
  public static final String SETPLAYMODE = "SetPlayMode";
  public static final String NEWPLAYMODE = "NewPlayMode";
  public static final String SETRECORDQUALITYMODE = "SetRecordQualityMode";
  public static final String NEWRECORDQUALITYMODE = "NewRecordQualityMode";
  public static final String GETCURRENTTRANSPORTACTIONS = "GetCurrentTransportActions";
  public static final String ACTIONS = "Actions";

  public static final String STOPPED = "STOPPED";
  public static final String PLAYING = "PLAYING";
  public static final String OK = "OK";
  public static final String ERROR_OCCURRED = "ERROR_OCCURRED";
  public static final String NORMAL = "NORMAL";
  public static final String TRACK_NR = "TRACK_NR";

  public static final String SCPD =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
          + "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n"
          + "   <specVersion>\n"
          + "      <major>1</major>\n"
          + "      <minor>0</minor>\n"
          + "	</specVersion>\n"
          + "    <serviceStateTable>        <stateVariable>            <name>TransportState</name> "
          + "           <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <dataType>string</dataType>            <allowedValueList>               "
          + " <allowedValue>STOPPED</allowedValue>               "
          + " <allowedValue>PLAYING</allowedValue>            </allowedValueList>       "
          + " </stateVariable>        <stateVariable>            <name>TransportStatus</name>      "
          + "      <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <dataType>string</dataType>            <allowedValueList>               "
          + " <allowedValue>OK</allowedValue>               "
          + " <allowedValue>ERROR_OCCURRED</allowedValue>                       </allowedValueList>"
          + "        </stateVariable>        <stateVariable>           "
          + " <name>PlaybackStorageMedium</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>	     <stateVariable>           "
          + " <name>RecordStorageMedium</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "             </stateVariable>        <stateVariable>           "
          + " <name>PossiblePlaybackStorageMedia</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>PossibleRecordStorageMedia</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>            <name>CurrentPlayMode</name>"
          + "            <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <dataType>string</dataType>            <allowedValueList>               "
          + " <allowedValue>NORMAL</allowedValue>            </allowedValueList>           "
          + " <defaultValue>NORMAL</defaultValue>        </stateVariable>        <stateVariable>   "
          + "         <name>TransportPlaySpeed</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType>	"
          + "         <allowedValueList>                <allowedValue>1</allowedValue>           "
          + " </allowedValueList>        </stateVariable>        <stateVariable>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <name>RecordMediumWriteStatus </name>            <dataType>string</dataType>        "
          + " </stateVariable>        <stateVariable>           "
          + " <name>CurrentRecordQualityMode</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "         </stateVariable>        <stateVariable>           "
          + " <name>PossibleRecordQualityModes</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>            <name>NumberOfTracks</name> "
          + "           <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <dataType>ui4</dataType>		     <allowedValueRange>			     <minimum>0</minimum>		    "
          + " </allowedValueRange>         </stateVariable>        <stateVariable>           "
          + " <name>CurrentTrack</name>            <sendEventsAttribute>no</sendEventsAttribute>   "
          + "         <dataType>ui4</dataType>		     <allowedValueRange>			    <minimum>0</minimum>"
          + "			    <step>1</step>		     </allowedValueRange>        </stateVariable>       "
          + " <stateVariable>            <name>CurrentTrackDuration</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>	     <stateVariable>           "
          + " <name>CurrentMediaDuration</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>CurrentTrackMetaData</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>            <name>CurrentTrackURI</name>"
          + "            <sendEventsAttribute>no</sendEventsAttribute>           "
          + " <dataType>string</dataType>        </stateVariable>        <stateVariable>           "
          + " <name>AVTransportURI</name>            <sendEventsAttribute>no</sendEventsAttribute> "
          + "           <dataType>string</dataType>        </stateVariable>        <stateVariable> "
          + "           <name>AVTransportURIMetaData</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>NextAVTransportURI</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>NextAVTransportURIMetaData</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>RelativeTimePosition</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>AbsoluteTimePosition</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>RelativeCounterPosition</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>i4</dataType>     "
          + "   </stateVariable>        <stateVariable>           "
          + " <name>AbsoluteCounterPosition</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>i4</dataType>     "
          + "   </stateVariable>        <stateVariable>		<Optional/>           "
          + " <name>CurrentTransportActions</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>            <name>LastChange</name>     "
          + "       <sendEventsAttribute>yes</sendEventsAttribute>           "
          + " <dataType>string</dataType>        </stateVariable>        <stateVariable>           "
          + " <name>A_ARG_TYPE_SeekMode</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "           <allowedValueList>                 <allowedValue>TRACK_NR</allowedValue>   "
          + "         </allowedValueList>        </stateVariable>        <stateVariable>           "
          + " <name>A_ARG_TYPE_SeekTarget</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>string</dataType> "
          + "       </stateVariable>        <stateVariable>           "
          + " <name>A_ARG_TYPE_InstanceID</name>           "
          + " <sendEventsAttribute>no</sendEventsAttribute>            <dataType>ui4</dataType>    "
          + "    </stateVariable>    </serviceStateTable>    <actionList>        <action>          "
          + "  <name>SetAVTransportURI</name>            <argumentList>                <argument>  "
          + "                  <name>InstanceID</name>                    <direction>in</direction>"
          + "                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "                </argument>                <argument>                   "
          + " <name>CurrentURI</name>                    <direction>in</direction>                 "
          + "   <relatedStateVariable>AVTransportURI</relatedStateVariable>               "
          + " </argument>                <argument>                   "
          + " <name>CurrentURIMetaData</name>                    <direction>in</direction>         "
          + "           <relatedStateVariable>AVTransportURIMetaData</relatedStateVariable>        "
          + "        </argument>            </argumentList>        </action>        <action>"
          + "	<Optional/>            <name>SetNextAVTransportURI</name>            <argumentList>  "
          + "              <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>NextURI</name>        "
          + "            <direction>in</direction>                   "
          + " <relatedStateVariable>NextAVTransportURI</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>NextURIMetaData</name>"
          + "                    <direction>in</direction>                   "
          + " <relatedStateVariable>NextAVTransportURIMetaData</relatedStateVariable>              "
          + "  </argument>            </argumentList>        </action>        <action>           "
          + " <name>GetMediaInfo</name>            <argumentList>                <argument>        "
          + "            <name>InstanceID</name>                    <direction>in</direction>      "
          + "           <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>         "
          + "       </argument>                <argument>                    <name>NrTracks</name> "
          + "                   <direction>out</direction>                   "
          + " <relatedStateVariable>NumberOfTracks</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>MediaDuration</name>  "
          + "                  <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentMediaDuration</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>CurrentURI</name>     "
          + "               <direction>out</direction>                   "
          + " <relatedStateVariable>AVTransportURI</relatedStateVariable>               "
          + " </argument>		         <argument>                    <name>CurrentURIMetaData</name>  "
          + "                  <direction>out</direction>                   "
          + " <relatedStateVariable>AVTransportURIMetaData</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>NextURI</name>        "
          + "            <direction>out</direction>                   "
          + " <relatedStateVariable>NextAVTransportURI</relatedStateVariable>               "
          + " </argument>		         <argument>                    <name>NextURIMetaData</name>     "
          + "               <direction>out</direction>                   "
          + " <relatedStateVariable>NextAVTransportURIMetaData</relatedStateVariable>              "
          + "  </argument>                <argument>                    <name>PlayMedium</name>    "
          + "                <direction>out</direction>                   "
          + " <relatedStateVariable>PlaybackStorageMedium</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>RecordMedium</name>   "
          + "                 <direction>out</direction>                   "
          + " <relatedStateVariable>RecordStorageMedium</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>WriteStatus</name>    "
          + "                <direction>out</direction>                   "
          + " <relatedStateVariable>RecordMediumWriteStatus </relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>GetTransportInfo</name>            <argumentList>                <argument>    "
          + "                <name>InstanceID</name>                    <direction>in</direction>  "
          + "                  <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>  "
          + "              </argument>                <argument>                   "
          + " <name>CurrentTransportState</name>                    <direction>out</direction>     "
          + "               <relatedStateVariable>TransportState</relatedStateVariable>            "
          + "    </argument>                <argument>                   "
          + " <name>CurrentTransportStatus</name>                    <direction>out</direction>    "
          + "                <relatedStateVariable>TransportStatus</relatedStateVariable>          "
          + "      </argument>                <argument>                   "
          + " <name>CurrentSpeed</name>                    <direction>out</direction>              "
          + "      <relatedStateVariable>TransportPlaySpeed</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>GetPositionInfo</name>            <argumentList>                <argument>     "
          + "               <name>InstanceID</name>                    <direction>in</direction>   "
          + "                 <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>   "
          + "             </argument>                <argument>                   "
          + " <name>Track</name>                    <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentTrack</relatedStateVariable>                </argument> "
          + "               <argument>                    <name>TrackDuration</name>               "
          + "     <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentTrackDuration</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>TrackMetaData</name>  "
          + "                  <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentTrackMetaData</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>TrackURI</name>       "
          + "             <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentTrackURI</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>RelTime</name>        "
          + "            <direction>out</direction>                   "
          + " <relatedStateVariable>RelativeTimePosition</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>AbsTime</name>        "
          + "            <direction>out</direction>                   "
          + " <relatedStateVariable>AbsoluteTimePosition</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>RelCount</name>       "
          + "             <direction>out</direction>                   "
          + " <relatedStateVariable>RelativeCounterPosition</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>AbsCount</name>       "
          + "             <direction>out</direction>                   "
          + " <relatedStateVariable>AbsoluteCounterPosition</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>GetDeviceCapabilities</name>            <argumentList>               "
          + " <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>PlayMedia</name>      "
          + "              <direction>out</direction>                   "
          + " <relatedStateVariable>PossiblePlaybackStorageMedia</relatedStateVariable>            "
          + "    </argument>                <argument>                    <name>RecMedia</name>    "
          + "                <direction>out</direction>                   "
          + " <relatedStateVariable>PossibleRecordStorageMedia</relatedStateVariable>              "
          + "  </argument>                <argument>                   "
          + " <name>RecQualityModes</name>                    <direction>out</direction>           "
          + "         <relatedStateVariable>PossibleRecordQualityModes</relatedStateVariable>      "
          + "          </argument>            </argumentList>        </action>        <action>     "
          + "       <name>GetTransportSettings</name>            <argumentList>               "
          + " <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>PlayMode</name>       "
          + "             <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentPlayMode</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>RecQualityMode</name> "
          + "                   <direction>out</direction>                "
          + " <relatedStateVariable>CurrentRecordQualityMode</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>Stop</name>            <argumentList>                <argument>                "
          + "    <name>InstanceID</name>                    <direction>in</direction>              "
          + "      <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>              "
          + "  </argument>            </argumentList>        </action>        <action>           "
          + " <name>Play</name>            <argumentList>                <argument>                "
          + "    <name>InstanceID</name>                    <direction>in</direction>              "
          + "      <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>              "
          + "  </argument>                <argument>                    <name>Speed</name>         "
          + "           <direction>in</direction>                   "
          + " <relatedStateVariable>TransportPlaySpeed</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>	<Optional/>  "
          + "          <name>Pause</name>            <argumentList>                <argument>      "
          + "              <name>InstanceID</name>                    <direction>in</direction>    "
          + "                <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>    "
          + "            </argument>            </argumentList>        </action>        <action>"
          + "	<Optional/>            <name>Record</name>            <argumentList>               "
          + " <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>Seek</name>            <argumentList>                <argument>                "
          + "    <name>InstanceID</name>                    <direction>in</direction>              "
          + "      <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>              "
          + "  </argument>                <argument>                    <name>Unit</name>          "
          + "          <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_SeekMode</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>Target</name>         "
          + "           <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_SeekTarget</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>           "
          + " <name>Next</name>            <argumentList>                <argument>                "
          + "    <name>InstanceID</name>                    <direction>in</direction>              "
          + "      <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>              "
          + "  </argument>            </argumentList>        </action>        <action>           "
          + " <name>Previous</name>            <argumentList>                <argument>            "
          + "        <name>InstanceID</name>                    <direction>in</direction>          "
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>          "
          + "      </argument>            </argumentList>        </action>        <action>"
          + "	<Optional/>            <name>SetPlayMode</name>            <argumentList>            "
          + "    <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>NewPlayMode</name>    "
          + "                <direction>in</direction>                   "
          + " <relatedStateVariable>CurrentPlayMode</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>        <action>	<Optional/>  "
          + "          <name>SetRecordQualityMode</name>            <argumentList>               "
          + " <argument>                    <name>InstanceID</name>                   "
          + " <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                   "
          + " <name>NewRecordQualityMode</name>                    <direction>in</direction>       "
          + "             <relatedStateVariable>CurrentRecordQualityMode</relatedStateVariable>    "
          + "            </argument>            </argumentList>        </action>        <action>"
          + "	<Optional/>            <name>GetCurrentTransportActions</name>           "
          + " <argumentList>                <argument>                    <name>InstanceID</name>  "
          + "                  <direction>in</direction>                   "
          + " <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>               "
          + " </argument>                <argument>                    <name>Actions</name>        "
          + "            <direction>out</direction>                   "
          + " <relatedStateVariable>CurrentTransportActions</relatedStateVariable>               "
          + " </argument>            </argumentList>        </action>    </actionList></scpd>";

  ////////////////////////////////////////////////
  // Constructor
  ////////////////////////////////////////////////

  public AVTransport(MediaRenderer render) {
    setMediaRenderer(render);

    avTransInfoList = new AVTransportInfoList();
  }

  ////////////////////////////////////////////////
  // MediaRender
  ////////////////////////////////////////////////

  private MediaRenderer mediaRenderer;

  private void setMediaRenderer(MediaRenderer render) {
    mediaRenderer = render;
  }

  public MediaRenderer getMediaRenderer() {
    return mediaRenderer;
  }

  ////////////////////////////////////////////////
  // Mutex
  ////////////////////////////////////////////////

  private Mutex mutex = new Mutex();

  public void lock() {
    mutex.lock();
  }

  public void unlock() {
    mutex.unlock();
  }

  ////////////////////////////////////////////////
  // AVTransportInfoList
  ////////////////////////////////////////////////

  private AVTransportInfoList avTransInfoList;

  public AVTransportInfoList getAvTransInfoList() {
    return avTransInfoList;
  }

  ////////////////////////////////////////////////
  // AVTransportInfo (Current)
  ////////////////////////////////////////////////

  public void setCurrentAvTransInfo(AVTransportInfo avTransInfo) {
    AVTransportInfoList avTransInfoList = getAvTransInfoList();
    synchronized (avTransInfoList) {
      if (1 <= avTransInfoList.size()) avTransInfoList.remove(0);
      avTransInfoList.insertElementAt(avTransInfo, 0);
    }
  }

  public AVTransportInfo getCurrentAvTransInfo() {
    AVTransportInfo avTransInfo = null;
    synchronized (avTransInfoList) {
      if (avTransInfoList.size() < 1) return null;
      avTransInfo = avTransInfoList.getAVTransportInfo(0);
    }
    return avTransInfo;
  }

  ////////////////////////////////////////////////
  // AVTransportInfo (Current)
  ////////////////////////////////////////////////

  public void setNextAvTransInfo(AVTransportInfo avTransInfo) {
    synchronized (avTransInfoList) {
      if (2 <= avTransInfoList.size()) avTransInfoList.remove(0);
      avTransInfoList.insertElementAt(avTransInfo, 1);
    }
  }

  public AVTransportInfo getNextAvTransInfo() {
    AVTransportInfo avTransInfo = null;
    synchronized (avTransInfoList) {
      if (avTransInfoList.size() < 2) return null;
      avTransInfo = avTransInfoList.getAVTransportInfo(1);
    }
    return avTransInfo;
  }

  ////////////////////////////////////////////////
  // ActionListener
  ////////////////////////////////////////////////

  public boolean actionControlReceived(Action action) {
    boolean isActionSuccess;

    String actionName = action.getName();

    if (actionName == null) return false;

    isActionSuccess = false;

    if (actionName.equals(SETAVTRANSPORTURI) == true) {
      AVTransportInfo avTransInfo = new AVTransportInfo();
      avTransInfo.setInstanceID(action.getArgument(INSTANCEID).getIntegerValue());
      avTransInfo.setURI(action.getArgument(CURRENTURI).getValue());
      avTransInfo.setURIMetaData(action.getArgument(CURRENTURIMETADATA).getValue());
      setCurrentAvTransInfo(avTransInfo);
      isActionSuccess = true;
    }

    if (actionName.equals(SETNEXTAVTRANSPORTURI) == true) {
      AVTransportInfo avTransInfo = new AVTransportInfo();
      avTransInfo.setInstanceID(action.getArgument(INSTANCEID).getIntegerValue());
      avTransInfo.setURI(action.getArgument(NEXTURI).getValue());
      avTransInfo.setURIMetaData(action.getArgument(NEXTURIMETADATA).getValue());
      setNextAvTransInfo(avTransInfo);
      isActionSuccess = true;
    }

    if (actionName.equals(GETMEDIAINFO) == true) {
      int instanceID = action.getArgument(INSTANCEID).getIntegerValue();
      synchronized (avTransInfoList) {
        int avTransInfoCnt = avTransInfoList.size();
        for (int n = 0; n < avTransInfoCnt; n++) {
          AVTransportInfo avTransInfo = avTransInfoList.getAVTransportInfo(n);
          if (avTransInfo == null) continue;
          if (avTransInfo.getInstanceID() != instanceID) continue;
          action.getArgument(CURRENTURI).setValue(avTransInfo.getURI());
          action.getArgument(CURRENTURIMETADATA).setValue(avTransInfo.getURIMetaData());
          isActionSuccess = true;
        }
      }
      return false;
    }

    if (actionName.equals(PLAY) == true) {
      int instanceID = action.getArgument(INSTANCEID).getIntegerValue();
      int speed = action.getArgument(SPEED).getIntegerValue();
      isActionSuccess = true;
    }

    if (actionName.equals(STOP) == true) {
      int instanceID = action.getArgument(INSTANCEID).getIntegerValue();
      isActionSuccess = true;
    }

    if (actionName.equals(PAUSE) == true) {
      int instanceID = action.getArgument(INSTANCEID).getIntegerValue();
      isActionSuccess = true;
    }

    /*
    		if (actionName.equals(PREPARE_FOR_CONNECTION) == true) {
    			action.getArgument(CONNECTION_ID).setValue(-1);
    			action.getArgument(AV_TRNSPORT_ID).setValue(-1);
    			action.getArgument(RCS_ID).setValue(-1);
    			return true;
    		}

    		if (actionName.equals(CONNECTION_COMPLETE) == true) {
    			return true;
    		}

    		if (actionName.equals(GET_CURRENT_CONNECTION_INFO) == true)
    			return getCurrentConnectionInfo(action);


    		if (actionName.equals(GET_CURRENT_CONNECTION_IDS) == true)
    			return getCurrentConnectionIDs(action);
    */

    MediaRenderer dmr = getMediaRenderer();
    if (dmr != null) {
      ActionListener listener = dmr.getActionListener();
      if (listener != null) listener.actionControlReceived(action);
    }

    return isActionSuccess;
  }

  ////////////////////////////////////////////////
  // QueryListener
  ////////////////////////////////////////////////

  public boolean queryControlReceived(StateVariable stateVar) {
    return false;
  }
}
