package com.cs546group1.assignment3;

/**
 * Simple data structure for housing event information.
 * @author Jay
 *
 */
public class Event {
	
	private String myName;
	
	private String myDate;
	
	private String mySummary;
	
	/**
	 * Constructor.
	 * @param name
	 * @param date
	 * @param summary
	 */
	public Event(String name, String date, String summary) {
		myName = name;
		myDate = date;
		mySummary = summary;
	}
	
	/**
	 * Return event name.
	 * @return
	 */
	public String getName() {
		return myName;
	}
	
	/**
	 * Return event date.
	 * @return
	 */
	public String getDate() {
		return myDate;
	}
	
	/**
	 * Return event summary.
	 * @return
	 */
	public String getSummary() {
		return mySummary;
	}

}
