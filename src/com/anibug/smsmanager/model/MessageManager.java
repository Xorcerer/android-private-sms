package com.anibug.smsmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.anibug.smsmanager.model.Message.DataBase;


public class MessageManager extends Manager<Message> {
	public static final String PREF_BLOCKING = "Blocking";

	public MessageManager(Context context) {
		super(context);
	}

	public List<Message> getMessages(String number) {
		if (number == null)
			return new ArrayList<Message>();
		return fetch(DataBase.PHONE_NUMBER, number);
	}

    public List<Message> getFakeMessages() {
        ArrayList<Message> list = new ArrayList<Message>();
        list.add(Message.FAKE_MESSAGE);
        return list;
    }

    public List<Message> getLastOneMessageForEachNumber() {
		final Formatter formatter = new Formatter();
		formatter.format("Select * From %1$s Where id In (Select Max(id) From %1$s Group By %2$s)",
				DataBase.TABLE_NAME, DataBase.PHONE_NUMBER);
		final Cursor cursor = getSqliteDatabase().rawQuery(formatter.toString(), null);
		formatter.close();

		return fetchList(cursor);
	}

	@Override
	public String getTableName() {
		return DataBase.TABLE_NAME;
	}

	@Override
	public String[] getTableDefinitionSQLs() {
		final String[] result = new String[2];
		final String tableFormat = "Create Table %s (" +
				"id INTEGER Primary Key, " +
				"%s VARCHAR(20), " +
				"%s TEXT, " +
				"%s NUMERIC, " +
				"%s INTEGER, " +
				"%s INTEGER" + // TODO: Should be unique.
				")";
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(),
				DataBase.PHONE_NUMBER, DataBase.CONTENT,
				DataBase.DATE_CREATED, DataBase.STATUS, DataBase.ONLINE_ID);
		result[0] = formatter.toString();
		formatter.close();

		final String dateIndexFormat = "Create Index %1$s_index_%2$s on %1$s (%2$s)";
		formatter = new Formatter();
		formatter.format(dateIndexFormat, getTableName(), DataBase.DATE_CREATED);
		result[1] = formatter.toString();
		formatter.close();

		return result;
	}

	@Override
	public ContentValues createRecord(Message message) {
		final ContentValues values = new ContentValues();

		final int timeInSecond = (int)(message.getDateCreated().getTime() / 1000L);
		values.put(DataBase.CONTENT, message.getContent());
		values.put(DataBase.DATE_CREATED, timeInSecond);
		values.put(DataBase.PHONE_NUMBER, message.getPhoneNumber());
		values.put(DataBase.STATUS, message.getStatus());
		values.put(DataBase.ONLINE_ID, message.getOnlineId());

		return values;
	}

	@Override
	public Message createObject(Cursor cursor) {
		final int indexPhoneNumber = cursor.getColumnIndexOrThrow(DataBase.PHONE_NUMBER);
		final int indexContent = cursor.getColumnIndexOrThrow(DataBase.CONTENT);
		final int indexStatus = cursor.getColumnIndexOrThrow(DataBase.STATUS);
		final int indexDateCreated = cursor.getColumnIndexOrThrow(DataBase.DATE_CREATED);
		final int indexOnlineId = cursor.getColumnIndexOrThrow(DataBase.ONLINE_ID);
		return new Message(
				cursor.getString(indexPhoneNumber),
				new Date(cursor.getInt(indexDateCreated) * 1000L),
				cursor.getString(indexContent),
				cursor.getInt(indexStatus),
				cursor.getInt(indexOnlineId));
	}

	public int deleteAllByPhoneNumber(String number) {
		final String[] whereArgs = new String[] {number};
		return getSqliteDatabase().delete(getTableName(), DataBase.PHONE_NUMBER + "= ?", whereArgs);
	}
}
