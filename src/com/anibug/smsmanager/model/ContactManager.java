package com.anibug.smsmanager.model;

import java.util.*;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.anibug.smsmanager.model.Contact.DataBase;
import com.anibug.smsmanager.model.filter.Filter;


public class ContactManager extends Manager<Contact> implements Filter {
	public ContactManager(Context context) {
		super(context);

		this.context = context;
	}

	Context context;
	public static final int MIN_PHONE_NUMBER_LENGTH_IN_CHINA = 11;

	private Set<String> phoneNumbers = null;

	public boolean hasPhoneNumber(String number) {
		if (phoneNumbers == null)
			phoneNumbers = getAllPhoneNumbers();

		return phoneNumbers.contains(normalizePhoneNumber(number));
	}

	// Whether it is good to truncate phone numbers?
	public String normalizePhoneNumber(String number) {
		if (number.length() <= MIN_PHONE_NUMBER_LENGTH_IN_CHINA)
			return number;
		return number.substring(number.length() - MIN_PHONE_NUMBER_LENGTH_IN_CHINA);
	}

	public Set<String> getAllPhoneNumbers() {
		final String[] columns = new String[] {DataBase.PHONE_NUMBER};
		final Cursor cursor = getSqliteDatabase().query(getTableName(), columns, null, null, null, null, null);

		final HashSet<String> numbers = new HashSet<String>();
		if (cursor.moveToFirst())
			do
				// We didn't normalize/truncate phone numbers before saving to database.
				numbers.add(normalizePhoneNumber(cursor.getString(0)));
			while (cursor.moveToNext());
		if (!cursor.isClosed())
			cursor.close();

		return numbers;
	}

	@Override
	public String getTableName() {
		return DataBase.TABLE_NAME;
	}

	@Override
	public String[] getTableDefinitionSQLs() {
		final String[] result = new String[1];
		final String tableFormat = "Create Table %s (" +
				"id INTEGER Primary Key," +
				"%s VARCHAR[50] Unique," +
				"%s VARCHAR[50] Unique," +
				"%s INTEGER" +
				")";
		final Formatter formatter = new Formatter();
		formatter.format(tableFormat, getTableName(), DataBase.NAME,
				DataBase.PHONE_NUMBER, DataBase.STATUS);
		result[0] = formatter.toString();
		formatter.close();

		return result;
	}

	@Override
	public ContentValues createRecord(Contact message) {
		final ContentValues values = new ContentValues();
		values.put(DataBase.NAME, message.getName());
		values.put(DataBase.PHONE_NUMBER, message.getPhoneNumber());
		values.put(DataBase.STATUS, message.getStatus());

		return values;
	}

	@Override
	public Contact createObject(Cursor cursor) {
        final int indexName = cursor.getColumnIndexOrThrow(DataBase.NAME);
		final int indexPhoneNumber = cursor.getColumnIndexOrThrow(DataBase.PHONE_NUMBER);
		final int indexStatus = cursor.getColumnIndexOrThrow(DataBase.STATUS);
		final Contact contact = new Contact(
				cursor.getString(indexName),
				cursor.getString(indexPhoneNumber),
				cursor.getInt(indexStatus));
        contact.setId(getObjectId(cursor));
		return contact;
	}

	public boolean match(Message message) {
		return hasPhoneNumber(message.getPhoneNumber());
	}

	public Contact getFromPickResult(Uri uri){
        final ContentResolver contentResolver = context.getContentResolver();
        final String[] cols = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};
		final Cursor contactCur = contentResolver.query(uri, cols, null, null, null);

		if (!contactCur.moveToFirst())
			return null;

        final String name = contactCur.getString(contactCur.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
		final String number = contactCur.getString(contactCur.getColumnIndexOrThrow(Phone.NUMBER));

		if(!contactCur.isClosed())
			contactCur.close();

		return new Contact(name, number.replace("-", ""));
	}

    private static Hashtable<String, Contact> cache = new Hashtable<String, Contact>();
    public Contact getByPhoneNumber(final String number) {
        if (number == null)
            return null;

        Contact contact = cache.get(number);
        if (contact != null)
            return contact;

        final String where = DataBase.PHONE_NUMBER + " LIKE '%' || ?";
        String[] whereArgs = new String[] {normalizePhoneNumber(number)};

        Cursor cursor = getSqliteDatabase().query(getTableName(), ALL, where, whereArgs, null, null, ID_DESC, "1");
        List<Contact> dbResult = fetchList(cursor);

        contact = dbResult.size() != 0 ? dbResult.get(0) : getNullContactOfPhoneNumber(number);
        cache.put(number, contact);
        return contact;
    }

    private static Contact getNullContactOfPhoneNumber(String number) {
        return new Contact(number, number);
    }

    public void clearCache() {
        cache.clear();
    }

    @Override
    public boolean save(Contact obj) {
        clearCache();
        return super.save(obj);
    }
}
