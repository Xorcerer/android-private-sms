package com.anibug.smsmanager.model;


abstract public class Model {

	protected long id = -1;

	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ("<" + getClass().getName() + ": " + String.valueOf(getId()) + ">");
	}
}