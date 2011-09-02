package com.anibug.smsmanager.model;


public class Contact extends Model {
	private String phoneNumber = "";
	private int status = 0; // Reserved.

	// Read from contact list.
	private String name = "";

	public Contact() {

	}

	public Contact(String name, String phoneNumber) {
		this(phoneNumber);

		this.name = name;
	}

	public Contact(String phoneNumber) {
		this.phoneNumber = phoneNumber;

	}
	
	public final String getName() {
		// TODO: Get name from phone contact list if `name` is null. 
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
		public static final String PHONENUMBER = "phone_number";
		public static final String STATUS = "status";
	}
}
