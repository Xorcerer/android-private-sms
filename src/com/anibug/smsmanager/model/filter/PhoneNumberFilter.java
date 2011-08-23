package com.anibug.smsmanager.model.filter;

import com.anibug.smsmanager.model.Message;

public class PhoneNumberFilter implements Filter {
	private final String phoneNumber;
	
	public PhoneNumberFilter(String number) {
		phoneNumber = number;
	}
	
	public boolean match(Message message) {
		return message.getPhoneNumber().indexOf(phoneNumber) >= 0;
	}
	
	public class DataBase
	{
		public static final String TABLE_NAME = "phone_numbers";

		public static final String PHONENUMBER = "phone_number";
		
		public static final String DATE_CREATED = "date_created";
		
		public static final String ENABLED = "enabled";
	}

}
