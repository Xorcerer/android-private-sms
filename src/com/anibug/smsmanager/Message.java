package com.anibug.smsmanager;

import java.util.Date;

import android.telephony.SmsMessage;

public class Message {

	public Message() {
	}
	
	public Message(SmsMessage message) {
		setBody(message.getMessageBody());
		setSender(message.getOriginatingAddress());
	}
	
	private String sender = "";
	private String body = "";
	private Date dateCreated = new Date();
	
	public final String getSender() {
		return sender;
	}

	public final void setSender(String sender) {
		this.sender = sender;
	}

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
