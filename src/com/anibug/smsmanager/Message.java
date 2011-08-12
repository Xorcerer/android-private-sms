package com.anibug.smsmanager;

import java.util.Date;

public class Message {

	private Date dateCreated = new Date();
	private String body = "";

	public final Date getDateCreated() {
		return dateCreated;
	}
	
	public final void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public final String getBody() {
		return body;
	}
	
	public final void setBody(String body) {
		this.body = body;
	}
}
