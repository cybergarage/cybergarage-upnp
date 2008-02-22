/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2003
*
*	File : RenderingControl.java
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

public class RenderingControl implements ActionListener, QueryListener
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
		"  <serviceStateTable>"+
		"    <stateVariable>"+
		"      <name>PresetNameList</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>string</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable> "+
		"      <name>LastChange</name> <sendEventsAttribute>yes</sendEventsAttribute>"+
		"      <dataType>string</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Brightness</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Contrast</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Sharpness</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>RedVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>GreenVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>BlueVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>RedVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>GreenVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>BlueVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>ColorTemperature</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>HorizontalKeystone</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>i2</dataType>"+
		"	<allowedValueRange>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>VerticalKeystone</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>i2</dataType>"+
		"	<allowedValueRange>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Mute</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>boolean</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Volume</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui2</dataType>"+
		"	<allowedValueRange>"+
		"		<minimum>0</minimum>"+
		"		<step>1</step>"+
		"	</allowedValueRange>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>VolumeDB</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>i2</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>Loudness</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>boolean</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>A_ARG_TYPE_Channel</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>string</dataType>"+
		"      <allowedValueList>"+
		"        <allowedValue>Master</allowedValue>"+
		"      </allowedValueList>"+
		"    </stateVariable>"+
		"    <stateVariable><Optional/>"+
		"      <name>A_ARG_TYPE_InstanceID</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>ui4</dataType>"+
		"    </stateVariable>"+
		"    <stateVariable>"+
		"      <name>A_ARG_TYPE_PresetName</name> <sendEventsAttribute>no</sendEventsAttribute>"+
		"      <dataType>string</dataType>"+
		"      <allowedValueList>"+
		"        <allowedValue>FactoryDefaults</allowedValue>"+
		"      </allowedValueList>"+
		"    </stateVariable>"+
		"  </serviceStateTable>"+
		"  <actionList>"+
		"    <action>"+
		"    <name>ListPresets</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentPresetNameList</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>PresetNameList</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action>"+
		"    <name>SelectPreset</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>PresetName</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_PresetName</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetBrightness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentBrightness</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Brightness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetBrightness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredBrightness</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Brightness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetContrast</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentContrast</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Contrast</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetContrast</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredContrast</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Contrast</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetSharpness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentSharpness</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Sharpness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetSharpness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredSharpness</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Sharpness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetRedVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentRedVideoGain</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>RedVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetRedVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredRedVideoGain</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>RedVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetGreenVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentGreenVideoGain</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>GreenVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetGreenVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredGreenVideoGain</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>GreenVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetBlueVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentBlueVideoGain</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>BlueVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetBlueVideoGain</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredBlueVideoGain</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>BlueVideoGain</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"   <action><Optional/>"+
		"    <name>GetRedVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentRedVideoBlackLevel</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>RedVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetRedVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredRedVideoBlackLevel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>RedVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetGreenVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentGreenVideoBlackLevel</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>GreenVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetGreenVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredGreenVideoBlackLevel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>GreenVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetBlueVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentBlueVideoBlackLevel</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>BlueVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetBlueVideoBlackLevel</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredBlueVideoBlackLevel</name>"+
		"    <direction>in</direction>"+
		"  <relatedStateVariable>BlueVideoBlackLevel</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetColorTemperature </name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentColorTemperature</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>ColorTemperature</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetColorTemperature</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredColorTemperature</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>ColorTemperature</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetHorizontalKeystone</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentHorizontalKeystone</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>HorizontalKeystone</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetHorizontalKeystone</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredHorizontalKeystone</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>HorizontalKeystone</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetVerticalKeystone</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentVerticalKeystone</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>VerticalKeystone</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetVerticalKeystone</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredVerticalKeystone</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>VerticalKeystone</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetMute</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentMute</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Mute</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetMute</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredMute</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Mute</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetVolume</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentVolume</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Volume</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetVolume</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredVolume</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Volume</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetVolumeDB</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentVolume</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>VolumeDB</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetVolumeDB</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredVolume</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>VolumeDB</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetVolumeDBRange</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>MinValue</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>VolumeDB</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>MaxValue</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>VolumeDB</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>GetLoudness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>CurrentLoudness</name>"+
		"          <direction>out</direction>"+
		"          <relatedStateVariable>Loudness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"    <action><Optional/>"+
		"    <name>SetLoudness</name>"+
		"      <argumentList>"+
		"        <argument>"+
		"          <name>InstanceID</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>Channel</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"+
		"        </argument>"+
		"        <argument>"+
		"          <name>DesiredLoudness</name>"+
		"          <direction>in</direction>"+
		"          <relatedStateVariable>Loudness</relatedStateVariable>"+
		"        </argument>"+
		"      </argumentList>"+
		"    </action>"+
		"  </actionList>"+
		"</scpd>";	

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

