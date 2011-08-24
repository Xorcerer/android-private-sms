package com.anibug.smsmanager.database;

import java.util.HashSet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 1;
    
    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // FIXME: It is not guaranteed we have all the table definitions
    // before onCreate invoked;
    private static HashSet<String> definitions = new HashSet<String>();
    public static void addSQL(String sql) {
    	definitions.add(sql);
	}
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : definitions) {
        	Log.d("Executing", sql);
			db.execSQL(sql);
		}
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing here in version 1.
	}
}
