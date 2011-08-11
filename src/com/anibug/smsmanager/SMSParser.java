/**
 * If these code works, it was written by kya.
 * If it doesn`t, I don`t know who wrote it
 */
package com.anibug.smsmanager;
import java.util.ArrayList;

import android.telephony.SmsMessage;

/**
 * @author kya
 *
 */
public class SMSParser {
	
	ArrayList<String>blacklist;
	
	private SMSParser(){
		blacklist = new ArrayList<String>();
		blacklist.add("123");
		blacklist.add("456");
	}
	private static SMSParser instance = new SMSParser();
	public static SMSParser getInstance() {
		return instance;
	}
	public boolean shouldAbortBoardCast(SmsMessage sms){
		if(blacklist.contains(sms.getDisplayOriginatingAddress())){
			return true;
		}
		else {
			return false;
		}
	}
}
