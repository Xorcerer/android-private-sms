package com.anibug.smsmanager.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class ModelBase {

	protected long id = -1;
	
	public final long getId() {
		return id;
	}
	
	public final void setId(long id) {
		this.id = id;
	}
	
	public boolean save() {
		
		try {
			Method method = this.getClass().getMethod("getManager", null);
			
			ManagerBase manager = (ManagerBase)method.invoke(this.getClass(), null);
			return manager.save(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return false;
	}
}