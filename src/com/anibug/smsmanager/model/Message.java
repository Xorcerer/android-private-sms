package com.anibug.smsmanager.model;

import java.util.Date;

public class Message {
	private String phoneNumber;
	private Date dateCreated;
	private String content;

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

	public class DataBase
	{
		public static final String PHONENUMBER = "phone_number";
		
		public static final String TIME = "date_created";
		
		public static final String CONTENT = "content";
		
		public static final String STATUS = "status";
	}
}
