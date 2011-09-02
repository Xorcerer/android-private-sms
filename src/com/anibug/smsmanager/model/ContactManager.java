package com.anibug.smsmanager.model;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.anibug.smsmanager.model.Contact.DataBase;
import com.anibug.smsmanager.model.filter.Filter;


public class ContactManager extends Manager<Contact> implements Filter {
	public ContactManager(Context context) {
		super(context);
		
		this.context = context;
	}
	
	Context context;
	public static final int MIN_PHONENUMBER_LENGTH_IN_CHINA = 11;
	
	private Set<String> phoneNumbers = null;

	public boolean hasPhoneNumber(String number) {
		if (phoneNumbers == null)
			phoneNumbers = getAllPhoneNumbers();

		return phoneNumbers.contains(normalizePhoneNumber(number));
	}

	// Whether it is good to truncate phone numbers?
	public String normalizePhoneNumber(String number) {
		if (number.length() <= MIN_PHONENUMBER_LENGTH_IN_CHINA)
			return number;
		return number.substring(number.length() - MIN_PHONENUMBER_LENGTH_IN_CHINA);
	}

	public Set<String> getAllPhoneNumbers() {
		String[] columns = new String[] { DataBase.PHONENUMBER };
		Cursor cursor = getSqliteDatabase().query(getTableName(), columns, null, null, null, null, null);
		
		HashSet<String> numbers = new HashSet<String>();
		if (cursor.moveToFirst()) {
			do {
				// We didn't normalize/truncate phone numbers before saving to database. 
				numbers.add(normalizePhoneNumber(cursor.getString(0)));
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		
		return numbers;
	}
	
	@Override
	public String getTableName() {
		return DataBase.TABLE_NAME;
	}

	@Override
	public String[] getTableDefinitionSQLs() {
		String[] result = new String[1];
		String tableFormat = "Create Table %s (" +
				"id INTEGER Primary Key," +
				"%s VARCHAR[20] Unique," + 
				"%s INTEGER" +
				")";
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(),
				DataBase.PHONENUMBER, DataBase.STATUS);
		result[0] = formatter.toString();
		return result;
	}

	@Override
	public ContentValues createRecord(Contact message) {
		ContentValues values = new ContentValues();
		
		values.put(DataBase.PHONENUMBER, message.getPhoneNumber());
		values.put(DataBase.STATUS, message.getStatus());
		
		return values;	
	}

	@Override
	public Contact createObject(Cursor cursor) {
		final int indexPhoneNumber = cursor.getColumnIndex(DataBase.PHONENUMBER);
		final int indexStatus = cursor.getColumnIndex(DataBase.STATUS);
		return new Contact(
				cursor.getString(indexPhoneNumber), 
				cursor.getInt(indexStatus));
	}

	public boolean match(Message message) {
		return hasPhoneNumber(message.getPhoneNumber());
	}
	
	public Contact getContactFromPickResult(Uri data){
		
		try {
            ContentResolver contect_resolver = context.getContentResolver();
			Cursor c = contect_resolver.query(data, null, null, null, null);

			if (c.moveToFirst()) {
				String name = null;
				String number = null;

	            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
	                
                Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                
                if (phoneCur.moveToFirst()) {
                    name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    number = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

				name = c.getString(c
						.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				
				Contact selected = new Contact(number, -1);
				selected.setName(name);
				
				if(!c.isClosed())
					c.close();
				
				return selected;
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("IllegalArgumentException :: ", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error :: ", e.toString());
		}
		
		return null;
	}
}
