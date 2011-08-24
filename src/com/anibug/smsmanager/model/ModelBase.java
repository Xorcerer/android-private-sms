package com.anibug.smsmanager.model;

abstract public class ModelBase {

	protected long id = -1;
	
	public final long getId() {
		return id;
	}
	
	public final void setId(long id) {
		this.id = id;
	}
}