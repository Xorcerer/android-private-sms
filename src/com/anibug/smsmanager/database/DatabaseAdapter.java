package com.anibug.smsmanager.database;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.filter.PhoneNumberFilter;


public class DatabaseAdapter {

	private DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private Context mContext;
	private static final String DB = "db.db";
	private static final int DB_VERSION = 1;
	
	private static final String[] ALL_COLUMNS = null;
	private static final String[] COLUMN_PHONENUMBER = new String[] { Message.DataBase.PHONENUMBER };
	

	public static final String TABLE_ID = "id";
	
	public DatabaseAdapter(Context context){
		
		mContext = context;
	}
	
	public void close() {
		
		mDatabaseHelper.close();
	}
	
	public DatabaseAdapter open(){
		
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		return this;
	}
	
	public Cursor getMessageByPhoneNumber(String number) {
		final String selection = Message.DataBase.PHONENUMBER + "=?" ;  
		final String[] selectionArgs = new String[] { number }; 
		
		return mSQLiteDatabase.query(Message.DataBase.TABLE_NAME, ALL_COLUMNS,
				selection, selectionArgs, null, null, Message.DataBase.PHONENUMBER);
	}
	
	public boolean deleteMessage(long id){
		final String where = TABLE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(id) }; 

		return mSQLiteDatabase.delete(Message.DataBase.TABLE_NAME, where, whereArgs) > 0;
	}
	
	public boolean hasPhoneNumber(String number) {
		final String selection = Message.DataBase.PHONENUMBER + "=?" ;  
		final String[] selectionArgs = new String[] { number }; 

		Cursor cursor = mSQLiteDatabase.query(Message.DataBase.TABLE_NAME, COLUMN_PHONENUMBER, selection, selectionArgs, null, null, null);
		return cursor.moveToFirst();
	}
	
	public Set<String> getAllPhoneNumbers() {
		Set<String> result = new HashSet<String>();
		
		Cursor cursor = mSQLiteDatabase.query(Message.DataBase.TABLE_NAME, COLUMN_PHONENUMBER, null,
				null, null, null, Message.DataBase.PHONENUMBER);

		if (cursor.moveToFirst()) {
			do {
				result.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}
	
	public long insertPhoneNumber(String number) {
		ContentValues values = new ContentValues();
		values.put(Message.DataBase.PHONENUMBER, number);
		return mSQLiteDatabase.insert(PhoneNumberFilter.DataBase.TABLE_NAME, null, values);
	}
	
	public boolean deletePhoneNumber(long id){
		final String where = TABLE_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(id) }; 
		
		return mSQLiteDatabase.delete(PhoneNumberFilter.DataBase.TABLE_NAME, where, whereArgs) > 0;
	};
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		StringBuilder mStringBuilderPhoneNumbers;
		StringBuilder mStringBuilderMessages;

		public DatabaseHelper (Context context) {
			
			super(context, DB, null, DB_VERSION);
		}
		
		public void initSQL() {
			
			mStringBuilderPhoneNumbers = new StringBuilder();

			mStringBuilderPhoneNumbers.append("create table ");
			mStringBuilderPhoneNumbers.append(PhoneNumberFilter.DataBase.TABLE_NAME);
			mStringBuilderPhoneNumbers.append("(");
			mStringBuilderPhoneNumbers.append(TABLE_ID );
			mStringBuilderPhoneNumbers.append(" integer primary key AUTOINCREMENT,");
			mStringBuilderPhoneNumbers.append(Message.DataBase.PHONENUMBER);
			mStringBuilderPhoneNumbers.append(" text ");
			mStringBuilderPhoneNumbers.append(" );");
			
			mStringBuilderMessages = new StringBuilder();
			//mStringBuilderMessages.delete(0, mStringBuilderMessages.length());
			mStringBuilderMessages.append("create table ");
			mStringBuilderMessages.append(Message.DataBase.TABLE_NAME);
			mStringBuilderMessages.append("(");
			mStringBuilderMessages.append(TABLE_ID );
			mStringBuilderMessages.append(" integer primary key AUTOINCREMENT,");
			mStringBuilderMessages.append(Message.DataBase.PHONENUMBER);
			mStringBuilderMessages.append(" text ,");
			mStringBuilderMessages.append(Message.DataBase.DATE_CREATED);
			mStringBuilderMessages.append(" datatime,");
			mStringBuilderMessages.append(Message.DataBase.CONTENT);
			mStringBuilderMessages.append(" text ,");
			mStringBuilderMessages.append(Message.DataBase.STATUS);
			mStringBuilderMessages.append(" text ");
			mStringBuilderMessages.append(" );");
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			
			initSQL();
			arg0.execSQL(mStringBuilderPhoneNumbers.toString());
			arg0.execSQL(mStringBuilderMessages.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

			arg0.execSQL("drop table if exists" + PhoneNumberFilter.DataBase.TABLE_NAME, null);
			arg0.execSQL("drop table if exists" + Message.DataBase.TABLE_NAME, null);
		}
	}
}
