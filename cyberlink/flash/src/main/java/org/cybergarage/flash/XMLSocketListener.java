/******************************************************************
*
*	CyberFlash for Java
*
*	Copyright (C) Satoshi Konno 2005
*
*	File: XMLSocketListener.java
*
*	Revision:
*
*	09/08/05
*		- first revision.
*	
******************************************************************/

package org.cybergarage.flash;

import org.cybergarage.xml.*;

public interface XMLSocketListener
{
	public Node xmlRequestRecieved(Node rootNode);
}
