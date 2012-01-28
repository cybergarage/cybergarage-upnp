/******************************************************************
*
*	CyberSQL for Java
*
*	Copyright (C) Satoshi Konno 2004
*
*	File: Database.java
*
*	Revision;
*
*	02/10/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.sql;

import java.sql.*;
import org.cybergarage.util.*;


public abstract class Database
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public Database()
	{
		setConnection(null);
		setStatement(null);
		setResultSet(null);
	}

	////////////////////////////////////////////////
	// Connection
	////////////////////////////////////////////////
	
	private Connection con;

	protected void setConnection(Connection c)
	{
		con = c;
	}
	
	private Connection getConnection()
	{
		return con;
	}

	////////////////////////////////////////////////
	// Statement
	////////////////////////////////////////////////
	
	private Statement stmt;

	private void setStatement(Statement s)
	{
		stmt = s;
	}
	
	private Statement getStatement()
	{
		return stmt;
	}

	////////////////////////////////////////////////
	// ResultSet
	////////////////////////////////////////////////
	
	private ResultSet rs;

	private void setResultSet(ResultSet r)
	{
		rs = r;
	}
	
	private ResultSet getResultSet()
	{
		return rs;
	}

	////////////////////////////////////////////////
	// abstract methods
	////////////////////////////////////////////////

	public abstract boolean open(String host, String dbname, String user, String passwd);	

	////////////////////////////////////////////////
	// close
	////////////////////////////////////////////////
	
	public void close()
	{
		Connection con = getConnection();
		if (con != null) {
			try {
				con.close();
				setConnection(null);
			}
			catch (Exception e) {
				Debug.warning(e);
			}
		}
	}

	////////////////////////////////////////////////
	// query
	////////////////////////////////////////////////

	public boolean query(String sql)
	{
		try {
			Statement stmt = getStatement();
			if (stmt != null)
				stmt.close();
			ResultSet rs = getResultSet();
			if (rs != null)
				rs.close();
			Connection con = getConnection();
			if (con == null)
				return false;
			stmt = con.createStatement();
			setStatement(stmt);
			rs = stmt.executeQuery(sql);
			setResultSet(rs);
		}
		catch (Exception e) {
			Debug.warning(e);
			return false;
		}
		return true;
	}

	////////////////////////////////////////////////
	// fetch
	////////////////////////////////////////////////

	public boolean fetch()
	{
		boolean fetchRet = false;
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return false;
			fetchRet = rs.next();
			if (!fetchRet) {
				Statement stmt = getStatement();
				if (stmt != null) {
					stmt.close();
					setStatement(null);
				}
				rs.close();
				setResultSet(null);
			}
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return fetchRet;
	}
	
	////////////////////////////////////////////////
	// get*
	////////////////////////////////////////////////
	
	public String getString(String name)
	{
        try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return "";
            byte[] str_b = rs.getBytes(name);
            return new String(str_b);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return "";
	}

	public String getString(int n)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return "";
            byte[] str_b = rs.getBytes(n);
            return new String(str_b);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return "";
	}

	public int getInteger(String name)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			return rs.getInt(name);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public int getInteger(int n)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			return rs.getInt(n);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getLong(String name)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			return rs.getLong(name);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getLong(int n)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			return rs.getLong(n);
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getTimestamp(String name)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			Timestamp ts = rs.getTimestamp(name);
			return ts.getTime();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getTimestamp(int n)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			Timestamp ts = rs.getTimestamp(n);
			return ts.getTime();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getDate(String name)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			Date ts = rs.getDate(name);
			return ts.getTime();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}

	public long getDate(int n)
	{
		try {
			ResultSet rs = getResultSet();
			if (rs == null)
				return 0;
			Date ts = rs.getDate(n);
			return ts.getTime();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return 0;
	}
	
	////////////////////////////////////////////////
	// update
	////////////////////////////////////////////////

	public int update(String sql)
	{
		int numOfUpdated = 0;
		try {
			Statement stmt = getStatement();
			if (stmt != null) {
				stmt.close();
				setStatement(null);
			}
			ResultSet rs = getResultSet();
			if (rs != null) {
				rs.close();
				setResultSet(null);
			}
			Connection con = getConnection();
			if (con == null)
				return 0;
			stmt = con.createStatement();
			numOfUpdated = stmt.executeUpdate(sql);
			stmt.close();
		}
		catch (Exception e) {
			Debug.warning(e);
		}
		return numOfUpdated;
	}

}
