package com.anibug.smsmanager.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anibug.smsmanager.database.SQLiteHelper;

public abstract class ManagerBase<T extends ModelBase> {

	protected SQLiteDatabase sqliteDatabase;
	
	protected static final String[] ALL = null;
	
	abstract public String getTableName();
	
	abstract public String[] getTableDefinitionSQLs();
	
	public  ManagerBase() {
		for (String sql : getTableDefinitionSQLs()) {
			SQLiteHelper.addSQL(sql);
		}
	}
	
	public List<T> fetchBy(String column, Object value) {
		final String where = column + "=?";
		String[] whereArgs = new String[] { String.valueOf(value) };
		
		Cursor cursor = sqliteDatabase.query(getTableName(), ALL, where, whereArgs, null, null, "id DESC");

		return fetchList(cursor);
	}
	
	protected List<T> fetchList(Cursor cursor) {
		ArrayList<T> result = new ArrayList<T>();
		
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
	
	public boolean save(T obj) {
		if (obj.getId() >= 0) {
			return update(obj);
		}
		return insert(obj);
	}
	
	protected boolean update(T obj) {
		final String where = "id=?";
		String[] whereArgs = new String[] { String.valueOf(obj.getId()) };

		ContentValues record = createRecord(obj);
		return sqliteDatabase.update(getTableName(), record, where, whereArgs) == 1;
	}
	
	protected boolean insert(T obj) {
		ContentValues record = createRecord(obj);
		long id = sqliteDatabase.insert(getTableName(), null, record);
		if (id < 0)
			return false;
		obj.setId(id);
		return true;
	}
	
	public boolean delete(long id) {
		final String where = "id=?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return sqliteDatabase.delete(getTableName(), where, whereArgs) > 0;
	}
	
	abstract public ContentValues createRecord(T obj);
	
	abstract public T createObject(Cursor cursor);
}
