/**
 *  The interface for class to filter out messages. 
 */
package com.anibug.smsmanager.model.filter;

import com.anibug.smsmanager.model.Message;

public interface Filter {
	public boolean match(Message message);
}
