package com.anibug.smsmanager.model;

import com.anibug.smsmanager.database.DatabaseAdapter;

public class MessageManager extends Managerbase {
	private static final int TYPE_INBOX = 1;
	private static final int TYPE_OUTBOX = 2;
	private static final int TYPE_DRAFT = 3;
	
	private final int type;
	
	DatabaseAdapter dbAdapter = new DatabaseAdapter(null);
	
	public MessageManager(int type) {
		this.type = type;
	}
	
	public boolean save(Message message) {
		return true;
	}
}
