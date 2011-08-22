package com.anibug.smsmanager.model;

public class ContactManager extends Managerbase {
	public static final int MIN_PHONENUMBER_LENGTH_IN_CHINA = 11;
	public boolean hasPhoneNumber(String number) {
		// TODO: Implement it.
		return false;
	}
	
	public String normalizePhoneNumber(String number) {
		if (number.length() <= MIN_PHONENUMBER_LENGTH_IN_CHINA)
			return number;
		return number.substring(number.length() - MIN_PHONENUMBER_LENGTH_IN_CHINA);
	}
}
