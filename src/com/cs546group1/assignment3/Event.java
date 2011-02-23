package com.cs546group1.assignment3;

public class Event {
	
	private String myName;
	
	private String myDate;
	
	private String mySummary;
	
	public Event(String name, String date, String summary) {
		myName = name;
		myDate = date;
		mySummary = summary;
	}
	
	public String getName() {
		return myName;
	}
	
	public String getDate() {
		return myDate;
	}
	
	public String getSummary() {
		return mySummary;
	}

}
