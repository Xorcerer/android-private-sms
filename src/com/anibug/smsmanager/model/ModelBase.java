package com.anibug.smsmanager.model;

abstract public class ModelBase {
	protected long id = -1;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
