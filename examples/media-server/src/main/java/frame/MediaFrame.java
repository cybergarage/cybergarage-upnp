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

package org.cybergarage.upnp.app.media.frame;

import org.cybergarage.upnp.app.media.*;

public abstract class MediaFrame
{
	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	public MediaFrame(MediaGate mgate)
	{
		mediaGate = mgate;
	}

	////////////////////////////////////////////////
	// Constuctor
	////////////////////////////////////////////////
	
	private MediaGate mediaGate;
	
	public MediaGate getMediaGate()
	{
		return mediaGate;
	}
}
