package com.anibug.smsmanager.model;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.anibug.smsmanager.model.Message.DataBase;


public class MessageManager extends Manager<Message> {
	WebClient client = new WebClient("10.0.2.2", 3000);

	public MessageManager(Context context) {
		super(context);
	}

	public List<Message> getMessages(String number) {
		return fetch(DataBase.PHONENUMBER, number);
	}

	public List<Message> getLastOneMessageForEachNumber() {
		Formatter formatter = new Formatter();
		formatter.format("Select * From %1$s Where id In (Select Max(id) From %1$s Group By %2$s)",
				DataBase.TABLE_NAME, DataBase.PHONENUMBER);
		Cursor cursor = getSqliteDatabase().rawQuery(formatter.toString(), null);
		
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
				"%s VARCHAR(20) Unique, " + 
				"%s TEXT, " +
				"%s NUMERIC, " +
				"%s INTEGER, " +
				"%s INTEGER Unique" +
				")";
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(),
				DataBase.PHONENUMBER, DataBase.CONTENT,
				DataBase.DATE_CREATED, DataBase.STATUS, DataBase.ONLINE_ID);
		result[0] = formatter.toString();
		
		String dateIndexFormat = "Create Index %1$s_index_%2$s on %1$s (%2$s)"; 
		formatter = new Formatter();
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
		values.put(DataBase.ONLINE_ID, message.getOnlineId());
		
		return values;
	}
	
	@Override
	public Message createObject(Cursor cursor) {
		final int indexPhoneNumber = cursor.getColumnIndex(DataBase.PHONENUMBER);
		final int indexContent = cursor.getColumnIndex(DataBase.CONTENT);
		final int indexStatus = cursor.getColumnIndex(DataBase.STATUS);
		final int indexDateCreated = cursor.getColumnIndex(DataBase.DATE_CREATED);
		final int indexOnlineId = cursor.getColumnIndex(DataBase.ONLINE_ID);
		return new Message(
				cursor.getString(indexPhoneNumber), 
				new Date(cursor.getInt(indexDateCreated) * 1000L),
				cursor.getString(indexContent),
				cursor.getInt(indexStatus),
				cursor.getInt(indexOnlineId));
	}

}
