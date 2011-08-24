package com.anibug.smsmanager.model;

import java.util.Date;
import java.util.Formatter;
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
	public String[] getTableDefinitionSQLs() {
		String[] result = new String[2];
		String tableFormat = "Create Table %s (" +
				"%s INTEGER Primary Key," +
				"%s VARCHAR[20] Unique," + 
				"%s TEXT," +
				"%s NUMERIC" +
				")";
		String dateIndexFormat = "Create Index on %s (%s)"; 
		
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, Message.DataBase.TABLE_NAME, TABLE_ID,
				Message.DataBase.PHONENUMBER, Message.DataBase.CONTENT,
				Message.DataBase.DATE_CREATED);
		result[0] = formatter.toString();
		
		formatter.flush();
		formatter.format(dateIndexFormat, Message.DataBase.TABLE_NAME, Message.DataBase.DATE_CREATED);
		result[1] = formatter.toString();
		return result;
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
