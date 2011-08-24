package com.anibug.smsmanager.model;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class MessageManager extends ManagerBase<Message> {

	public List<Message> getMessagesBy(String number) {
		return selectBy(Message.DataBase.PHONENUMBER, number);
	}
	
	@Override
	public String getTableName() {
		return Message.DataBase.TABLE_NAME;
	}

	@Override
	public String getTableDefinitionSQL() {
		return null;
	}

	@Override
	public ContentValues createRecord(Message message) {
		ContentValues values = new ContentValues();
		
		int timeInSecond = (int)(message.getDateCreated().getTime() / 1000L);
		values.put(Message.DataBase.CONTENT, message.getContent());
		values.put(Message.DataBase.DATE_CREATED, timeInSecond);
		values.put(Message.DataBase.PHONENUMBER, message.getPhoneNumber());
		values.put(Message.DataBase.STATUS, message.getStatus());
		
		return values;
	}
	
	@Override
	public Message createObject(Cursor cursor) {
		final int indexPhoneNumber = cursor.getColumnIndex(Message.DataBase.PHONENUMBER);
		final int indexContent = cursor.getColumnIndex(Message.DataBase.CONTENT);
		final int indexStatus = cursor.getColumnIndex(Message.DataBase.STATUS);
		final int indexDateCreated = cursor.getColumnIndex(Message.DataBase.DATE_CREATED);
		return new Message(
				cursor.getString(indexPhoneNumber), 
				new Date(cursor.getInt(indexDateCreated) * 1000L),
				cursor.getString(indexContent),
				cursor.getInt(indexStatus));
	}

}
