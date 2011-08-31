package com.anibug.smsmanager.model;


abstract public class Model {

	protected long localId = -1;
	
	public final long getLocalId() {
		return localId;
	}
	
	public final void setLocalId(long id) {
		this.localId = id;
	}
}