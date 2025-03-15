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

package org.cybergarage.upnp.std.av.renderer;

import org.cybergarage.util.*;
import org.cybergarage.upnp.*;
import org.cybergarage.upnp.control.*;

public class RenderingControl implements ActionListener, QueryListener {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String SERVICE_TYPE = "urn:schemas-upnp-org:service:RenderingControl:1";

  // Browse Action

  public static final String PRESETNAMELIST = "PresetNameList";
  public static final String LASTCHANGE = "LastChange";
  public static final String BRIGHTNESS = "Brightness";
  public static final String CONTRAST = "Contrast";
  public static final String SHARPNESS = "Sharpness";
  public static final String REDVIDEOGAIN = "RedVideoGain";
  public static final String GREENVIDEOGAIN = "GreenVideoGain";
  public static final String BLUEVIDEOGAIN = "BlueVideoGain";
  public static final String REDVIDEOBLACKLEVEL = "RedVideoBlackLevel";
  public static final String GREENVIDEOBLACKLEVEL = "GreenVideoBlackLevel";
  public static final String BLUEVIDEOBLACKLEVEL = "BlueVideoBlackLevel";
  public static final String COLORTEMPERATURE = "ColorTemperature";
  public static final String HORIZONTALKEYSTONE = "HorizontalKeystone";
  public static final String VERTICALKEYSTONE = "VerticalKeystone";
  public static final String MUTE = "Mute";
  public static final String VOLUME = "Volume";
  public static final String VOLUMEDB = "VolumeDB";
  public static final String LOUDNESS = "Loudness";
  public static final String LISTPRESETS = "ListPresets";
  public static final String INSTANCEID = "InstanceID";
  public static final String CURRENTPRESETNAMELIST = "CurrentPresetNameList";
  public static final String SELECTPRESET = "SelectPreset";
  public static final String PRESETNAME = "PresetName";
  public static final String GETBRIGHTNESS = "GetBrightness";
  public static final String CURRENTBRIGHTNESS = "CurrentBrightness";
  public static final String SETBRIGHTNESS = "SetBrightness";
  public static final String DESIREDBRIGHTNESS = "DesiredBrightness";
  public static final String GETCONTRAST = "GetContrast";
  public static final String CURRENTCONTRAST = "CurrentContrast";
  public static final String SETCONTRAST = "SetContrast";
  public static final String DESIREDCONTRAST = "DesiredContrast";
  public static final String GETSHARPNESS = "GetSharpness";
  public static final String CURRENTSHARPNESS = "CurrentSharpness";
  public static final String SETSHARPNESS = "SetSharpness";
  public static final String DESIREDSHARPNESS = "DesiredSharpness";
  public static final String GETREDVIDEOGAIN = "GetRedVideoGain";
  public static final String CURRENTREDVIDEOGAIN = "CurrentRedVideoGain";
  public static final String SETREDVIDEOGAIN = "SetRedVideoGain";
  public static final String DESIREDREDVIDEOGAIN = "DesiredRedVideoGain";
  public static final String GETGREENVIDEOGAIN = "GetGreenVideoGain";
  public static final String CURRENTGREENVIDEOGAIN = "CurrentGreenVideoGain";
  public static final String SETGREENVIDEOGAIN = "SetGreenVideoGain";
  public static final String DESIREDGREENVIDEOGAIN = "DesiredGreenVideoGain";
  public static final String GETBLUEVIDEOGAIN = "GetBlueVideoGain";
  public static final String CURRENTBLUEVIDEOGAIN = "CurrentBlueVideoGain";
  public static final String SETBLUEVIDEOGAIN = "SetBlueVideoGain";
  public static final String DESIREDBLUEVIDEOGAIN = "DesiredBlueVideoGain";
  public static final String GETREDVIDEOBLACKLEVEL = "GetRedVideoBlackLevel";
  public static final String CURRENTREDVIDEOBLACKLEVEL = "CurrentRedVideoBlackLevel";
  public static final String SETREDVIDEOBLACKLEVEL = "SetRedVideoBlackLevel";
  public static final String DESIREDREDVIDEOBLACKLEVEL = "DesiredRedVideoBlackLevel";
  public static final String GETGREENVIDEOBLACKLEVEL = "GetGreenVideoBlackLevel";
  public static final String CURRENTGREENVIDEOBLACKLEVEL = "CurrentGreenVideoBlackLevel";
  public static final String SETGREENVIDEOBLACKLEVEL = "SetGreenVideoBlackLevel";
  public static final String DESIREDGREENVIDEOBLACKLEVEL = "DesiredGreenVideoBlackLevel";
  public static final String GETBLUEVIDEOBLACKLEVEL = "GetBlueVideoBlackLevel";
  public static final String CURRENTBLUEVIDEOBLACKLEVEL = "CurrentBlueVideoBlackLevel";
  public static final String SETBLUEVIDEOBLACKLEVEL = "SetBlueVideoBlackLevel";
  public static final String DESIREDBLUEVIDEOBLACKLEVEL = "DesiredBlueVideoBlackLevel";
  public static final String GETCOLORTEMPERATURE = "GetColorTemperature";
  public static final String CURRENTCOLORTEMPERATURE = "CurrentColorTemperature";
  public static final String SETCOLORTEMPERATURE = "SetColorTemperature";
  public static final String DESIREDCOLORTEMPERATURE = "DesiredColorTemperature";
  public static final String GETHORIZONTALKEYSTONE = "GetHorizontalKeystone";
  public static final String CURRENTHORIZONTALKEYSTONE = "CurrentHorizontalKeystone";
  public static final String SETHORIZONTALKEYSTONE = "SetHorizontalKeystone";
  public static final String DESIREDHORIZONTALKEYSTONE = "DesiredHorizontalKeystone";
  public static final String GETVERTICALKEYSTONE = "GetVerticalKeystone";
  public static final String CURRENTVERTICALKEYSTONE = "CurrentVerticalKeystone";
  public static final String SETVERTICALKEYSTONE = "SetVerticalKeystone";
  public static final String DESIREDVERTICALKEYSTONE = "DesiredVerticalKeystone";
  public static final String GETMUTE = "GetMute";
  public static final String CHANNEL = "Channel";
  public static final String CURRENTMUTE = "CurrentMute";
  public static final String SETMUTE = "SetMute";
  public static final String DESIREDMUTE = "DesiredMute";
  public static final String GETVOLUME = "GetVolume";
  public static final String CURRENTVOLUME = "CurrentVolume";
  public static final String SETVOLUME = "SetVolume";
  public static final String DESIREDVOLUME = "DesiredVolume";
  public static final String GETVOLUMEDB = "GetVolumeDB";
  public static final String SETVOLUMEDB = "SetVolumeDB";
  public static final String GETVOLUMEDBRANGE = "GetVolumeDBRange";
  public static final String MINVALUE = "MinValue";
  public static final String MAXVALUE = "MaxValue";
  public static final String GETLOUDNESS = "GetLoudness";
  public static final String CURRENTLOUDNESS = "CurrentLoudness";
  public static final String SETLOUDNESS = "SetLoudness";
  public static final String DESIREDLOUDNESS = "DesiredLoudness";

  public static final String MASTER = "Master";
  public static final String FACTORYDEFAULTS = "FactoryDefaults";

  public static final String SCPD =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
          + "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n"
          + "   <specVersion>\n"
          + "      <major>1</major>\n"
          + "      <minor>0</minor>\n"
          + "	</specVersion>\n"
          + "  <serviceStateTable>"
          + "    <stateVariable>"
          + "      <name>PresetNameList</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>string</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable> "
          + "      <name>LastChange</name> <sendEventsAttribute>yes</sendEventsAttribute>"
          + "      <dataType>string</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Brightness</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Contrast</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Sharpness</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>RedVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>GreenVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>BlueVideoGain</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>RedVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>GreenVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>BlueVideoBlackLevel</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>ColorTemperature</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>HorizontalKeystone</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>i2</dataType>"
          + "	<allowedValueRange>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>VerticalKeystone</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>i2</dataType>"
          + "	<allowedValueRange>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Mute</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>boolean</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Volume</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui2</dataType>"
          + "	<allowedValueRange>"
          + "		<minimum>0</minimum>"
          + "		<step>1</step>"
          + "	</allowedValueRange>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>VolumeDB</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>i2</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>Loudness</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>boolean</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>A_ARG_TYPE_Channel</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>string</dataType>"
          + "      <allowedValueList>"
          + "        <allowedValue>Master</allowedValue>"
          + "      </allowedValueList>"
          + "    </stateVariable>"
          + "    <stateVariable><Optional/>"
          + "      <name>A_ARG_TYPE_InstanceID</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>ui4</dataType>"
          + "    </stateVariable>"
          + "    <stateVariable>"
          + "      <name>A_ARG_TYPE_PresetName</name> <sendEventsAttribute>no</sendEventsAttribute>"
          + "      <dataType>string</dataType>"
          + "      <allowedValueList>"
          + "        <allowedValue>FactoryDefaults</allowedValue>"
          + "      </allowedValueList>"
          + "    </stateVariable>"
          + "  </serviceStateTable>"
          + "  <actionList>"
          + "    <action>"
          + "    <name>ListPresets</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentPresetNameList</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>PresetNameList</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action>"
          + "    <name>SelectPreset</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>PresetName</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_PresetName</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetBrightness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentBrightness</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Brightness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetBrightness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredBrightness</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Brightness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetContrast</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentContrast</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Contrast</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetContrast</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredContrast</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Contrast</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetSharpness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentSharpness</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Sharpness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetSharpness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredSharpness</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Sharpness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetRedVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentRedVideoGain</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>RedVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetRedVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredRedVideoGain</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>RedVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetGreenVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentGreenVideoGain</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>GreenVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetGreenVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredGreenVideoGain</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>GreenVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetBlueVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentBlueVideoGain</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>BlueVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetBlueVideoGain</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredBlueVideoGain</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>BlueVideoGain</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "   <action><Optional/>"
          + "    <name>GetRedVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentRedVideoBlackLevel</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>RedVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetRedVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredRedVideoBlackLevel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>RedVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetGreenVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentGreenVideoBlackLevel</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>GreenVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetGreenVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredGreenVideoBlackLevel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>GreenVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetBlueVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentBlueVideoBlackLevel</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>BlueVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetBlueVideoBlackLevel</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredBlueVideoBlackLevel</name>"
          + "    <direction>in</direction>"
          + "  <relatedStateVariable>BlueVideoBlackLevel</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetColorTemperature </name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentColorTemperature</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>ColorTemperature</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetColorTemperature</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredColorTemperature</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>ColorTemperature</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetHorizontalKeystone</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentHorizontalKeystone</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>HorizontalKeystone</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetHorizontalKeystone</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredHorizontalKeystone</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>HorizontalKeystone</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetVerticalKeystone</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentVerticalKeystone</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>VerticalKeystone</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetVerticalKeystone</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredVerticalKeystone</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>VerticalKeystone</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetMute</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentMute</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Mute</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetMute</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredMute</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Mute</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetVolume</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentVolume</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Volume</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetVolume</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredVolume</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Volume</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetVolumeDB</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentVolume</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>VolumeDB</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetVolumeDB</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredVolume</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>VolumeDB</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetVolumeDBRange</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>MinValue</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>VolumeDB</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>MaxValue</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>VolumeDB</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>GetLoudness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>CurrentLoudness</name>"
          + "          <direction>out</direction>"
          + "          <relatedStateVariable>Loudness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "    <action><Optional/>"
          + "    <name>SetLoudness</name>"
          + "      <argumentList>"
          + "        <argument>"
          + "          <name>InstanceID</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_InstanceID</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>Channel</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>A_ARG_TYPE_Channel</relatedStateVariable>"
          + "        </argument>"
          + "        <argument>"
          + "          <name>DesiredLoudness</name>"
          + "          <direction>in</direction>"
          + "          <relatedStateVariable>Loudness</relatedStateVariable>"
          + "        </argument>"
          + "      </argumentList>"
          + "    </action>"
          + "  </actionList>"
          + "</scpd>";

  ////////////////////////////////////////////////
  // Constructor
  ////////////////////////////////////////////////

  public RenderingControl(MediaRenderer render) {
    setMediaRenderer(render);
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
  // ActionListener
  ////////////////////////////////////////////////

  public boolean actionControlReceived(Action action) {
    boolean isActionSuccess;

    String actionName = action.getName();

    if (actionName == null) return false;

    isActionSuccess = false;
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
