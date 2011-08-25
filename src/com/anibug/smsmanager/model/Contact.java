package com.anibug.smsmanager.model;


public class Contact extends ModelBase {
	private String phoneNumber = "";
	private int status = 0; // Reserved.

	// Read from contact list.
	private String firstName = "";
	private String listName = "";

	public Contact() {
		
	}

	public Contact(String phoneNumber, int status) {
		this.phoneNumber = phoneNumber;
		this.status = status;
	}

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

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public class DataBase {
		public static final String TABLE_NAME = "phone_numbers";
		public static final String PHONENUMBER = "phone_number";
		public static final String STATUS = "status";
	}
}
