package com.anibug.smsmanager.model;

public class Contact {
	private String firstName = "";
	private String listName = "";
	private String phoneNumber = "";

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public final String getListName() {
		return listName;
	}
	
	public final void setListName(String listName) {
		this.listName = listName;
	}
	
	public final String getPhoneNumber() {
		return phoneNumber;
	}
	
	public final void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
