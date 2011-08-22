package com.anibug.smsmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.anibug.smsmanager.database.DatabaseAdapter;

public class MessageManager extends Managerbase {
	DatabaseAdapter dbAdapter = new DatabaseAdapter(null);

	private MessageManager() {
	}
	
	public List<Message> getMessages(Contact contact) {
		ArrayList<Message> result = new ArrayList<Message>();
		
		// TODO: Support multi-phonenumber.
		Cursor cursor = dbAdapter.getMessageByPhoneNumber(contact.getPhoneNumber());

		// TODO: Any better way?
		final int indexPhoneNumber = cursor.getColumnIndex(Message.DataBase.PHONENUMBER);
		final int indexContent = cursor.getColumnIndex(Message.DataBase.CONTENT);
		final int indexStatus = cursor.getColumnIndex(Message.DataBase.STATUS);
		final int indexDateCreated = cursor.getColumnIndex(Message.DataBase.TIME);

		if (cursor.moveToFirst()) {
			do {
				result.add(new Message(
						cursor.getString(indexPhoneNumber), 
						new Date(cursor.getLong(indexDateCreated)), 
						cursor.getString(indexContent),
						cursor.getInt(indexStatus)));
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}

		return result;
	}
	
	public boolean Insert(Message message) {
		return true;
	}

}
