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

package org.cybergarage.upnp.media.render;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;
import org.cybergarage.upnp.media.server.object.*;

public class AVTransport implements ActionListener, QueryListener
{
	////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////

	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:ConnectionManager:1";		
	
	// Browse Action	
	
	public final static String HTTP_GET = "http-get";
	
	public final static String GET_PROTOCOL_INFO = "GetProtocolInfo";
	public final static String SOURCE = "Source";
	public final static String SINK= "Sink";

	public final static String PREPARE_FOR_CONNECTION = "PrepareForConnection";
	public final static String REMOTE_PROTOCOL_INFO= "RemoteProtocolInfo";
	public final static String PEER_CONNECTION_MANAGER = "PeerConnectionManager";
	public final static String PEER_CONNECTION_ID = "PeerConnectionID";
	public final static String DIRECTION = "Direction";
	public final static String CONNECTION_ID = "ConnectionID";
	public final static String AV_TRNSPORT_ID = "AVTransportID";
	public final static String RCS_ID = "RcsID";
	
	public final static String CONNECTION_COMPLETE = "ConnectionComplete";

	public final static String GET_CURRENT_CONNECTION_IDS = "GetCurrentConnectionIDs";
	public final static String CONNECTION_IDS = "ConnectionIDs";

	public final static String GET_CURRENT_CONNECTION_INFO = "GetCurrentConnectionInfo";
	public final static String PROTOCOL_INFO= "ProtocolInfo";
	public final static String STATUS = "Status";
	
	public final static String SCPD = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" +
		"   <specVersion>\n" +
		"      <major>1</major>\n" +
		"      <minor>0</minor>\n" +
		"	</specVersion>\n" +
		"    <serviceStateTable>"+
		"        <stateVariable>"+
		"            <name>TransportState</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"            <allowedValueList>"+
		"                <allowedValue>STOPPED</allowedValue>"+
		"                <allowedValue>PLAYING</allowedValue>"+
		"            </allowedValueList>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>TransportStatus</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"            <allowedValueList>"+
		"                <allowedValue>OK</allowedValue>"+
		"                <allowedValue>ERROR_OCCURRED</allowedValue>           "+
		"            </allowedValueList>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>PlaybackStorageMedium</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"	     <stateVariable>"+
		"            <name>RecordStorageMedium</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"              </stateVariable>"+
		"        <stateVariable>"+
		"            <name>PossiblePlaybackStorageMedia</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>PossibleRecordStorageMedia</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentPlayMode</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"            <allowedValueList>"+
		"                <allowedValue>NORMAL</allowedValue>"+
		"            </allowedValueList>"+
		"            <defaultValue>NORMAL</defaultValue>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>TransportPlaySpeed</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"	         <allowedValueList>"+
		"                <allowedValue>1</allowedValue>"+
		"            </allowedValueList>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <name>RecordMediumWriteStatus </name>"+
		"            <dataType>string</dataType>"+
		"         </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentRecordQualityMode</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"          </stateVariable>"+
		"        <stateVariable>"+
		"            <name>PossibleRecordQualityModes</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>NumberOfTracks</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>ui4</dataType>"+
		"		     <allowedValueRange>"+
		"			     <minimum>0</minimum>"+
		"		     </allowedValueRange>"+
		"         </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentTrack</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>ui4</dataType>"+
		"		     <allowedValueRange>"+
		"			    <minimum>0</minimum>"+
		"			    <step>1</step>"+
		"		     </allowedValueRange>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentTrackDuration</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"	     <stateVariable>"+
		"            <name>CurrentMediaDuration</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentTrackMetaData</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>CurrentTrackURI</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>AVTransportURI</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>AVTransportURIMetaData</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>NextAVTransportURI</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>NextAVTransportURIMetaData</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>RelativeTimePosition</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>AbsoluteTimePosition</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>RelativeCounterPosition</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>i4</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>AbsoluteCounterPosition</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>i4</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"		<Optional/>"+
		"            <name>CurrentTransportActions</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>LastChange</name>"+
		"            <sendEventsAttribute>yes</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>A_ARG_TYPE_SeekMode</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"            <allowedValueList>"+
		"                 <allowedValue>TRACK_NR</allowedValue>"+
		"            </allowedValueList>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>A_ARG_TYPE_SeekTarget</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>string</dataType>"+
		"        </stateVariable>"+
		"        <stateVariable>"+
		"            <name>A_ARG_TYPE_InstanceID</name>"+
		"            <sendEventsAttribute>no</sendEventsAttribute>"+
		"            <dataType>ui4</dataType>"+
		"        </stateVariable>"+
		"    </serviceStateTable>"+
		"    <actionList>"+
		"        <action>"+
		"            <name>SetAVTransportURI</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentURI</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>AVTransportURI</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentURIMetaData</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>AVTransportURIMetaData</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>SetNextAVTransportURI</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NextURI</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>NextAVTransportURI</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NextURIMetaData</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>NextAVTransportURIMetaData</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>GetMediaInfo</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                 <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NrTracks</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>NumberOfTracks</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>MediaDuration</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentMediaDuration</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentURI</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>AVTransportURI</relatedStateVariable>"+
		"                </argument>"+
		"		         <argument>"+
		"                    <name>CurrentURIMetaData</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>AVTransportURIMetaData</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NextURI</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>NextAVTransportURI</relatedStateVariable>"+
		"                </argument>"+
		"		         <argument>"+
		"                    <name>NextURIMetaData</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>NextAVTransportURIMetaData</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>PlayMedium</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>PlaybackStorageMedium</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RecordMedium</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>RecordStorageMedium</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>WriteStatus</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>RecordMediumWriteStatus </relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>GetTransportInfo</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentTransportState</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>TransportState</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentTransportStatus</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>TransportStatus</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>CurrentSpeed</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>TransportPlaySpeed</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>GetPositionInfo</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>Track</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentTrack</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>TrackDuration</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentTrackDuration</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>TrackMetaData</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentTrackMetaData</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>TrackURI</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentTrackURI</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RelTime</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>RelativeTimePosition</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>AbsTime</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>AbsoluteTimePosition</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RelCount</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>RelativeCounterPosition</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>AbsCount</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>AbsoluteCounterPosition</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>GetDeviceCapabilities</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>PlayMedia</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>PossiblePlaybackStorageMedia</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RecMedia</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>PossibleRecordStorageMedia</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RecQualityModes</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>PossibleRecordQualityModes</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>GetTransportSettings</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>PlayMode</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentPlayMode</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>RecQualityMode</name>"+
		"                    <direction>out</direction>" +
		"                 <relatedStateVariable>CurrentRecordQualityMode</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>Stop</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>Play</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>Speed</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>TransportPlaySpeed</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>Pause</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>Record</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>Seek</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>Unit</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_SeekMode</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>Target</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_SeekTarget</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>Next</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>"+
		"            <name>Previous</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>SetPlayMode</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NewPlayMode</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>CurrentPlayMode</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>SetRecordQualityMode</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>NewRecordQualityMode</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>CurrentRecordQualityMode</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"        <action>	<Optional/>"+
		"            <name>GetCurrentTransportActions</name>"+
		"            <argumentList>"+
		"                <argument>"+
		"                    <name>InstanceID</name>"+
		"                    <direction>in</direction>" +
		"                    <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"                </argument>"+
		"                <argument>"+
		"                    <name>Actions</name>"+
		"                    <direction>out</direction>" +
		"                    <relatedStateVariable>CurrentTransportActions</relatedStateVariable>"+
		"                </argument>"+
		"            </argumentList>"+
		"        </action>"+
		"    </actionList>"		"</scpd>";	

	////////////////////////////////////////////////
	// Constructor 
	////////////////////////////////////////////////
	
	public ConnectionManager(MediaServer mserver)
	{
		setMediaServer(mserver);
		maxConnectionID = 0;
	}
	
	////////////////////////////////////////////////
	// Media Server
	////////////////////////////////////////////////

	private MediaServer mediaServer;
	
	private void setMediaServer(MediaServer mserver)
	{
		mediaServer = mserver;	
	}
	
	public MediaServer getMediaServer()
	{
		return mediaServer;	
	}

	public ContentDirectory getContentDirectory()
	{
		return getMediaServer().getContentDirectory();	
	}
	
	////////////////////////////////////////////////
	// Mutex
	////////////////////////////////////////////////
	
	private Mutex mutex = new Mutex();
	
	public void lock()
	{
		mutex.lock();
	}
	
	public void unlock()
	{
		mutex.unlock();
	}
	
	////////////////////////////////////////////////
	// ConnectionID
	////////////////////////////////////////////////
	
	private int maxConnectionID;
	
	public int getNextConnectionID()
	{
		lock();
		maxConnectionID++;
		unlock();
		return maxConnectionID;
	}
	
	////////////////////////////////////////////////
	// ConnectionInfoList
	////////////////////////////////////////////////
	
	// Thanks for Brian Owens (12/02/04)
	private ConnectionInfoList conInfoList = new ConnectionInfoList();;
	
	public ConnectionInfoList getConnectionInfoList()
	{
		return conInfoList;
	}
	
	public ConnectionInfo getConnectionInfo(int id)
	{
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (info.getID() == id)
				return info;
		}
		return null;
	}
	
	public void addConnectionInfo(ConnectionInfo info)
	{
		lock();
		conInfoList.add(info);
		unlock();
	}
	
	public void removeConnectionInfo(int id)
	{
		lock();
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (info.getID() == id) {
				conInfoList.remove(info);
				break;
			}
		}
		unlock();
	}
	
	public void removeConnectionInfo(ConnectionInfo info)
	{
		lock();
		conInfoList.remove(info);
		unlock();
	}
	
	////////////////////////////////////////////////
	// ActionListener
	////////////////////////////////////////////////

	public boolean actionControlReceived(Action action)
	{
		//action.print();
		
		String actionName = action.getName();
		
		if (actionName.equals(GET_PROTOCOL_INFO) == true) {
			// Source
			String sourceValue = "";
			int mimeTypeCnt = getContentDirectory().getNFormats();
			for (int n=0; n<mimeTypeCnt; n++) {
				if (0 < n)
					sourceValue += ",";
				Format format = getContentDirectory().getFormat(n);
				String mimeType = format.getMimeType();
				sourceValue += HTTP_GET + ":*:" + mimeType + ":*";
			}
			action.getArgument(SOURCE).setValue(sourceValue);
			// Sink
			action.getArgument(SINK).setValue("");
			return true;
		}

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
		
		return false;
	}

	////////////////////////////////////////////////
	// GetCurrentConnectionIDs
	////////////////////////////////////////////////
	
	private boolean getCurrentConnectionIDs(Action action)
	{
		String conIDs = "";
		lock();
		int size = conInfoList.size();
		for (int n=0; n<size; n++) {
			ConnectionInfo info = conInfoList.getConnectionInfo(n);
			if (0 < n)
				conIDs += ",";
			conIDs += Integer.toString(info.getID());
		}
		action.getArgument(CONNECTION_IDS).setValue(conIDs);
		unlock();
		return true;
	}
	
	////////////////////////////////////////////////
	// GetCurrentConnectionInfo
	////////////////////////////////////////////////
	
	private boolean getCurrentConnectionInfo(Action action)
	{
		int id = action.getArgument(RCS_ID).getIntegerValue();
		lock();
		ConnectionInfo info = getConnectionInfo(id);
		if (info != null) {
			action.getArgument(RCS_ID).setValue(info.getRcsID());
			action.getArgument(AV_TRNSPORT_ID).setValue(info.getAVTransportID());
			action.getArgument(PEER_CONNECTION_MANAGER).setValue(info.getPeerConnectionManager());
			action.getArgument(PEER_CONNECTION_ID).setValue(info.getPeerConnectionID());
			action.getArgument(DIRECTION).setValue(info.getDirection());
			action.getArgument(STATUS).setValue(info.getStatus());
		}
		else {
			action.getArgument(RCS_ID).setValue(-1);
			action.getArgument(AV_TRNSPORT_ID).setValue(-1);
			action.getArgument(PEER_CONNECTION_MANAGER).setValue("");
			action.getArgument(PEER_CONNECTION_ID).setValue(-1);
			action.getArgument(DIRECTION).setValue(ConnectionInfo.OUTPUT);
			action.getArgument(STATUS).setValue(ConnectionInfo.UNKNOWN);
		}
		unlock();
		return true;
	}
	
	////////////////////////////////////////////////
	// QueryListener
	////////////////////////////////////////////////

	public boolean queryControlReceived(StateVariable stateVar)
	{
		return false;
	}
}

