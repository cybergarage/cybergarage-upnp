/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File : Clock.java
*
******************************************************************/

import java.util.*;

public class Clock
{
	private Calendar cal;

	public Clock(Calendar cal)
	{
		this.cal = cal;
	}

	public Calendar getCalendar()
	{
		return cal;
	}

	////////////////////////////////////////////////
	//	Time
	////////////////////////////////////////////////

	public int getHour()
	{
		return getCalendar().get(Calendar.HOUR);
	}

	public int getMinute()
	{
		return getCalendar().get(Calendar.MINUTE);
	}

	public int getSecond()
	{
		return getCalendar().get(Calendar.SECOND);
	}
	
	////////////////////////////////////////////////
	//	paint
	////////////////////////////////////////////////

	public final static Clock getInstance()
	{
		return new Clock(Calendar.getInstance());
	}

	////////////////////////////////////////////////
	//	getDateString
	////////////////////////////////////////////////

	public final static String toClockString(int value)
	{
		if (value < 10)
			return "0" + Integer.toString(value);
		return Integer.toString(value);
	}

	private final static String MONTH_STRING[] = {
		"Jan",
		"Feb",
		"Mar",
		"Apr",
		"May",
		"Jun",
		"Jul",
		"Aug",
		"Sep",
		"Oct",
		"Nov",
		"Dec",
	};

	public final static String toMonthString(int value)
	{
		value -= Calendar.JANUARY;
		if (0 <= value && value < 12)
			return MONTH_STRING[value];
		return "";
	}
	
	private final static String WEEK_STRING[] = {
		"Sun",
		"Mon",
		"Tue",
		"Wed",
		"Thu",
		"Fri",
		"Sat",
	};

	public final static String toWeekString(int value)
	{
		value -= Calendar.SUNDAY;
		if (0 <= value && value < 7)
			return WEEK_STRING[value];
		return "";
	}
	
	public String getDateString()
	{
		Calendar cal = getCalendar();
		return
			toWeekString(cal.get(Calendar.DAY_OF_WEEK)) +", " + 
			toMonthString(cal.get(Calendar.MONTH)) + " " +
			Integer.toString(cal.get(Calendar.DATE)) + ", " +
			toClockString(cal.get(Calendar.YEAR) % 100);
	}

	////////////////////////////////////////////////
	//	getTimeString
	////////////////////////////////////////////////
	
	public String getTimeString()
	{
		Calendar cal = getCalendar();
		return
			toClockString(cal.get(Calendar.HOUR_OF_DAY)) +
			(((cal.get(Calendar.SECOND) % 2) == 0) ? ":" : " ") +
			toClockString(cal.get(Calendar.MINUTE));
	}

	////////////////////////////////////////////////
	//	toString
	////////////////////////////////////////////////

	public String toString()
	{
		Calendar cal = getCalendar();
		return
			getDateString() + ", " +
			toClockString(cal.get(Calendar.HOUR)) + ":" +
			toClockString(cal.get(Calendar.MINUTE)) + ":" +
			toClockString(cal.get(Calendar.SECOND));
	}
		
}

