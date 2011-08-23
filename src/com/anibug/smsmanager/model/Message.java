package com.anibug.smsmanager.model;

import java.util.Date;

public class Message {
	public static final int STATUS_RECEIVED = 1;
	public static final int STATUS_SENT = 2;
	public static final int STATUS_DRAFT = 3;
	
	private long id; // In local database.
	private String phoneNumber;
	private Date dateCreated;
	private String content;
	private int status;

	public Message() {
		
	}
	
	public Message(String phoneNumber, Date dateCreated, String content, int status) {
		this.phoneNumber = phoneNumber;
		this.dateCreated = dateCreated;
		this.content = content;
		this.status = status;
	}


	
	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	public final String getPhoneNumber() {
		return phoneNumber;
	}

	public final void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public final Date getDateCreated() {
		return dateCreated;
	}

	public final void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public class DataBase
	{
		public static final String TABLE_NAME = "messages";
		public static final String PHONENUMBER = "phone_number";
		public static final String DATE_CREATED = "date_created";
		public static final String CONTENT = "content";
		public static final String STATUS = "status";
	}
}
