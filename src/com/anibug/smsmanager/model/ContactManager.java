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
import android.widget.Toast;

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
				"%s VARCHAR[50] Unique," +
				"%s VARCHAR[20] Unique," + 
				"%s INTEGER" +
				")";
		Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(), DataBase.Name,
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
		final int indexName = cursor.getColumnIndexOrThrow(DataBase.Name);
		final int indexPhoneNumber = cursor.getColumnIndexOrThrow(DataBase.PHONENUMBER);
		final int indexStatus = cursor.getColumnIndexOrThrow(DataBase.STATUS);
		return new Contact(
				cursor.getString(indexName),
				cursor.getString(indexPhoneNumber),
				cursor.getInt(indexStatus));
	}

	public boolean match(Message message) {
		return hasPhoneNumber(message.getPhoneNumber());
	}
	
	public Contact getContactFromPickResult(Uri uri){
		
        ContentResolver contentResolver = context.getContentResolver();
        String[] cols = new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
		Cursor contactCur = contentResolver.query(uri, cols, null, null, null);

		if (contactCur.moveToFirst())
			return null;

		String number = "";

        String id = contactCur.getString(contactCur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
		String name = contactCur.getString(contactCur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

		if(!contactCur.isClosed())
			contactCur.close();

		// TODO: We may split this method from here.
		cols = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor phoneCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { id }, null);

        if (!phoneCur.moveToFirst()) {
        	Toast.makeText(context, "Contact " + name + " does not have a phone number.", Toast.LENGTH_SHORT).show();
        	return null;
        }

        number = phoneCur.getString(phoneCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

		Contact selected = new Contact(name, number);

		if(!phoneCur.isClosed())
			contactCur.close();

		return selected;
	}
}
