package com.anibug.smsmanager.model;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.anibug.smsmanager.model.Message.DataBase;


public class MessageManager extends ManagerBase<Message> {

	public List<Message> getMessagesBy(String number) {
		return fetchBy(DataBase.PHONENUMBER, number);
	}

	public List<Message> getLastOneMessageForEachNumber() {
		Formatter formatter = new Formatter();
		formatter.format("Select * From %1$s Where id In (Select Max(id) From %1$s Group By %2$s)",
				DataBase.TABLE_NAME, DataBase.PHONENUMBER);
		Cursor cursor = sqliteDatabase.rawQuery(formatter.toString(), null);
		
		return fetchList(cursor);
	}
	
	@Override
	public String getTableName() {
		return DataBase.TABLE_NAME;
	}

	@Override
	public String[] getTableDefinitionSQLs() {
		String[] result = new String[2];
		String tableFormat = "Create Table %s (" +
				"id INTEGER Primary Key, " +
				"%s VARCHAR[20] Unique, " + 
				"%s TEXT, " +
				"%s NUMERIC, " +
				"%s INGTEGER" +
				")";
		String dateIndexFormat = "Create Index on %s (%s)"; 
		
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(),
				DataBase.PHONENUMBER, DataBase.CONTENT,
				DataBase.DATE_CREATED, DataBase.STATUS);
		result[0] = formatter.toString();
		
		formatter.flush();
		formatter.format(dateIndexFormat, getTableName(), DataBase.DATE_CREATED);
		result[1] = formatter.toString();
		return result;
	}

	@Override
	public ContentValues createRecord(Message message) {
		ContentValues values = new ContentValues();
		
		int timeInSecond = (int)(message.getDateCreated().getTime() / 1000L);
		values.put(DataBase.CONTENT, message.getContent());
		values.put(DataBase.DATE_CREATED, timeInSecond);
		values.put(DataBase.PHONENUMBER, message.getPhoneNumber());
		values.put(DataBase.STATUS, message.getStatus());
		
		return values;
	}
	
	@Override
	public Message createObject(Cursor cursor) {
		final int indexPhoneNumber = cursor.getColumnIndex(DataBase.PHONENUMBER);
		final int indexContent = cursor.getColumnIndex(DataBase.CONTENT);
		final int indexStatus = cursor.getColumnIndex(DataBase.STATUS);
		final int indexDateCreated = cursor.getColumnIndex(DataBase.DATE_CREATED);
		return new Message(
				cursor.getString(indexPhoneNumber), 
				new Date(cursor.getInt(indexDateCreated) * 1000L),
				cursor.getString(indexContent),
				cursor.getInt(indexStatus));
	}

}
