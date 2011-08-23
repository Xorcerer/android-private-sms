package com.anibug.smsmanager.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class ManagerBase<T> {

	protected SQLiteDatabase sqliteDatabase;

	private static final String TABLE_ID = "id";
	private static final String[] ALL = null;
	
	abstract public String getTableName();
	
	abstract public String getTableDefinitionSQL();
	
	public List<T> selectBy(String column, Object value) {
		final String where = column + "=?";
		String[] whereArgs = new String[] { String.valueOf(value)};
		
		ArrayList<T> result = new ArrayList<T>();
		
		Cursor cursor = sqliteDatabase.query(getTableName(), ALL, where, whereArgs, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				result.add(createObject(cursor));
			} while (cursor.moveToNext());
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}

		return result;
	}
	
	public boolean insert(T obj) {
		ContentValues record = createRecord(obj);
		sqliteDatabase.insert(getTableName(), null, record);
		return true;
	}
	
	public boolean delete(long id) {
		final String where = TABLE_ID + "=?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return sqliteDatabase.delete(getTableName(), where, whereArgs) > 0;
	}
	
	abstract public ContentValues createRecord(T obj);
	
	abstract public T createObject(Cursor cursor);
	
	public class SQLiteHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "db";
	    private static final int DATABASE_VERSION = 1;
	    
	    SQLiteHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        //db.execSQL(DICTIONARY_TABLE_CREATE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}
}
