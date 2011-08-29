package com.anibug.smsmanager.model;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public interface AbstractManager<T extends ModelBase> {

	public abstract List<T> fetchAll();

	public abstract List<T> fetch(String column, Object value);

	public abstract boolean save(T obj);

	public abstract boolean delete(long id);

	abstract public ContentValues createRecord(T obj);

	abstract public T createObject(Cursor cursor);

}