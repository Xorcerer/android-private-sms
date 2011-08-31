package com.anibug.smsmanager.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anibug.smsmanager.database.SQLiteHelper;

public abstract class Manager<T extends Model> {

	private static SQLiteHelper sqliteHelper;
	private static SQLiteDatabase sqliteDatabase;
	
	protected static final String[] ALL = null;
	protected static final String ID_DESC = "id DESC";
	
	abstract public String getTableName();
	
	abstract public String[] getTableDefinitionSQLs();
	
	public Manager(Context context) {
		if (sqliteHelper == null)
			sqliteHelper = new SQLiteHelper(context);

		// FIXME: We need a better way to added SQLs just once.
		String[] sqls = getTableDefinitionSQLs();
		for (String sql : sqls) {
			if (sqliteHelper.addSQL(sql))
				Log.d(getClass().getName(), "New definition added -- " + sql);
		}
	}
	
	protected synchronized SQLiteDatabase getSqliteDatabase() {
		if (sqliteDatabase == null)
			sqliteDatabase = sqliteHelper.getWritableDatabase();
		return sqliteDatabase;
	}


	public List<T> fetchAll() {
		Cursor cursor = getSqliteDatabase().query(getTableName(), ALL, null, null, null, null, ID_DESC);

		return fetchList(cursor);
	}
	
	public List<T> fetch(String column, Object value) {
		final String where = column + "=?";
		String[] whereArgs = new String[] { String.valueOf(value) };
		
		Cursor cursor = getSqliteDatabase().query(getTableName(), ALL, where, whereArgs, null, null, ID_DESC);

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
		if (obj.getLocalId() >= 0) {
			return update(obj);
		}
		return insert(obj);
	}
	
	protected boolean update(T obj) {
		final String where = "id=?";
		String[] whereArgs = new String[] { String.valueOf(obj.getLocalId()) };

		ContentValues record = createRecord(obj);
		return getSqliteDatabase().update(getTableName(), record, where, whereArgs) == 1;
	}
	
	protected boolean insert(T obj) {
		ContentValues record = createRecord(obj);
		long id = getSqliteDatabase().insert(getTableName(), null, record);
		if (id < 0)
			return false;
		obj.setLocalId(id);
		return true;
	}
	
	public boolean delete(long id) {
		final String where = "id=?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return getSqliteDatabase().delete(getTableName(), where, whereArgs) > 0;
	}
	
	abstract public ContentValues createRecord(T obj);
	
	abstract public T createObject(Cursor cursor);
}
