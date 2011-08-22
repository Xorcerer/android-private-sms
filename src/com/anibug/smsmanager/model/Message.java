package com.anibug.smsmanager.model;

import java.util.Date;

public class Message {
	public static final int STATUS_RECEIVED = 1;
	public static final int STATUS_SENT = 2;
	public static final int STATUS_DRAFT = 3;
	
	private int id; // In local database.
	private String phoneNumber;
	private Date dateCreated;
	private String content;
	private int status;

	public Message(String phoneNumber, Date dateCreated, String content, int status) {
		this.phoneNumber = phoneNumber;
		this.dateCreated = dateCreated;
		this.content = content;
		this.status = status;
	}

	public final int getId() {
		return id;
	}
	
	public final String getPhoneNumber() {
		return phoneNumber;
	}

	public final Date getDateCreated() {
		return dateCreated;
	}

	public final int getStatus() {
		return status;
	}

	public final String getContent() {
		return content;
	}
	
	public final void setContent(String content) {
		this.content = content;
	}

	public class DataBase
	{
		public static final String PHONENUMBER = "phone_number";
		
		public static final String TIME = "date_created";
		
		public static final String CONTENT = "content";
		
		public static final String STATUS = "status";
	}
}
