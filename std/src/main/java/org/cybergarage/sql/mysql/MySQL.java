/******************************************************************
*
*	CyberSQL for Java
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: MySQL.java
*
*	Revision;
*
*	02/10/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.sql.mysql;

import java.sql.*;
import org.cybergarage.sql.*;
import org.cybergarage.util.*;

public class MySQL extends Database
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public MySQL()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
	}

	////////////////////////////////////////////////
	//	abstract methods
	////////////////////////////////////////////////

	public boolean open(String host, String dbname, String user, String passwd)
	{	
		try {
			String dburl = "jdbc:mysql://" + host + "/" + dbname;
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			setConnection(con);
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		
		return true;
	}

}
