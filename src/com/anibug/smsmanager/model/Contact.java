package com.anibug.smsmanager.model;


public class Contact extends Model {
	private String phoneNumber;
	private int status = 0; // Reserved.

	// Read from contact list.
	private String name = null;

	public Contact() {

	}

	public Contact(String name, String phoneNumber, int status) {
		this(name, phoneNumber);
		this.status = status;
	}

	public Contact(String name, String phoneNumber) {
		this(phoneNumber);
		this.name = name;
	}

	public Contact(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public final String getName() {
		// TODO: Get the name from system contact list, if null.
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
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
		public static final String NAME = "name";
		public static final String PHONENUMBER = "phone_number";
		public static final String STATUS = "status";
	}
}
