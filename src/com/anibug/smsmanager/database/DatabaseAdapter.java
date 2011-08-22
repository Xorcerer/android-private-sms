package com.anibug.smsmanager.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anibug.smsmanager.model.Message;


public class DatabaseAdapter {

	private DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private Context mContext;
	private static final String DB = "db.db";
	private static final int DB_VERSION = 1;
	
	public static final String PHONENUMBERS_TABLE = "PhoneNumbers";
	public static final String MESSAGES_TABLE = "Messages";
	public static final String[] ALL_COLUMNS = null;

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
	
	public Set<String> getAllPhoneNumbers() {
		final String[] columns = new String[] { Message.DataBase.PHONENUMBER };
		Set<String> result = new HashSet<String>();
		
		Cursor cursor = mSQLiteDatabase.query(MESSAGES_TABLE, columns, null,
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
	
	public Cursor getAllMessages(){
		
		return mSQLiteDatabase.rawQuery("select * from " + MESSAGES_TABLE, null);
	}
	
	public List<Message> getMessageByPhoneNumber(String number) {
		final String selection = Message.DataBase.PHONENUMBER + "=?" ;  
		final String[] selectionArgs = new String[] { number }; 
		
		ArrayList<Message> result = new ArrayList<Message>();
		
		Cursor cursor = mSQLiteDatabase.query(MESSAGES_TABLE, ALL_COLUMNS,
				selection, selectionArgs, null, null, Message.DataBase.PHONENUMBER);
		if (cursor.moveToFirst()) {
			do {
				// TODO: queryset => List<message>
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}
	
	//operate messages
	public Long addMessage(Message info){
		
		ContentValues values = new ContentValues();
		values.put(Message.DataBase.PHONENUMBER, info.getPhoneNumber());
		values.put(Message.DataBase.TIME, info.getTime());
		values.put(Message.DataBase.CONTENT, info.getContent());
		values.put(Message.DataBase.STATUS, info.getStatus());
		long rowID = mSQLiteDatabase.insert(MESSAGES_TABLE, null, values);
		return rowID;	
	}
	
	public boolean deleteMessage(long rowID){
		
		return mSQLiteDatabase.delete(MESSAGES_TABLE, TABLE_ID+"="+rowID, null) > 0;
	}
	
	public boolean deleteMessage(long rowID, String column, String number){
		
		return mSQLiteDatabase.delete(MESSAGES_TABLE, TABLE_ID+"=?"+rowID, new String[]{number}) > 0;
	}
	
	public void deleteMessageTable(){
		
		mSQLiteDatabase.execSQL("delete from "+ MESSAGES_TABLE);
	}
	
	//operate numbers
	public Long addPhoneNumber(String number){
		
		ContentValues values = new ContentValues();
		values.put(Message.DataBase.PHONENUMBER, number);
		long rowID = mSQLiteDatabase.insert(PHONENUMBERS_TABLE, null, values);
		return rowID;
	}
	
	public boolean deletePhoneNumber(long rowID){
		
		return mSQLiteDatabase.delete(PHONENUMBERS_TABLE, TABLE_ID+"="+rowID, null) > 0;
	};
	
	public void deletePhoneNumberTable(){
		
		mSQLiteDatabase.execSQL("delete from "+ PHONENUMBERS_TABLE);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		StringBuilder mStringBuilderPhoneNumbers;
		StringBuilder mStringBuilderMessages;

		public DatabaseHelper (Context context) {
			
			super(context, DB, null, DB_VERSION);
		}
		
		public void initSQL() {
			
			mStringBuilderPhoneNumbers = new StringBuilder();
			//mStringBuilderPhoneNumbers.delete(0, mStringBuilderPhoneNumbers.length());
			mStringBuilderPhoneNumbers.append("create table ");
			mStringBuilderPhoneNumbers.append(PHONENUMBERS_TABLE);
			mStringBuilderPhoneNumbers.append("(");
			mStringBuilderPhoneNumbers.append(TABLE_ID );
			mStringBuilderPhoneNumbers.append(" integer primary key AUTOINCREMENT,");
			mStringBuilderPhoneNumbers.append(Message.DataBase.PHONENUMBER);
			mStringBuilderPhoneNumbers.append(" text ");
			mStringBuilderPhoneNumbers.append(" );");
			
			mStringBuilderMessages = new StringBuilder();
			//mStringBuilderMessages.delete(0, mStringBuilderMessages.length());
			mStringBuilderMessages.append("create table ");
			mStringBuilderMessages.append(MESSAGES_TABLE);
			mStringBuilderMessages.append("(");
			mStringBuilderMessages.append(TABLE_ID );
			mStringBuilderMessages.append(" integer primary key AUTOINCREMENT,");
			mStringBuilderMessages.append(Message.DataBase.PHONENUMBER);
			mStringBuilderMessages.append(" text ,");
			mStringBuilderMessages.append(Message.DataBase.TIME);
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

			arg0.execSQL("drop table if exists" + PHONENUMBERS_TABLE, null);
			arg0.execSQL("drop table if exists" + MESSAGES_TABLE, null);
		}
	}
}
