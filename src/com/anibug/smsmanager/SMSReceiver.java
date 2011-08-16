package com.anibug.smsmanager;

import com.anibug.smsmanager.database.DatabaseAdapter;
import com.anibug.smsmanager.model.MessageInfo;

import android.R.bool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	
	private String _phoneNumber;
	private String _content;

	private DatabaseAdapter mDatabaseAdapter;
	
	public SMSReceiver() {
		
		_phoneNumber = new String();
		_content = new String();
	}
	
	@Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SMSReceiver, isOrderedBroadcast()="
                + isOrderedBroadcast());
        
        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        
        StringBuilder phoneNumberBuilder = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        
        SmsMessage smsMessage[] = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
        	
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        }
        
        for (SmsMessage currentMessage : smsMessage){
        	
        	phoneNumberBuilder.append(currentMessage.getDisplayOriginatingAddress());
        	contentBuilder.append(currentMessage.getDisplayMessageBody());
        }

        //ToDo, for other country support
        _phoneNumber = trimPhoneNumber("+86", phoneNumberBuilder.toString());
        _content = contentBuilder.toString();

        mDatabaseAdapter = new DatabaseAdapter(context);
        mDatabaseAdapter.open();
        
        System.out.println("receive message with phoneNumber: "+ _phoneNumber + "content: " + _content);
        
        Cursor mCursor = mDatabaseAdapter.getPhoneNumber(_phoneNumber);
        if(mCursor.moveToFirst()){
        	abortBroadcast();
            System.out.println("--abortBroadcast message with phoneNumber: "+ _phoneNumber + "content: " + _content);
        	
        	MessageInfo mInfo = new MessageInfo();
        	mInfo.setPhoneNumber(_phoneNumber);
        	mInfo.setContent(_content);
        	mInfo.setTime(GetCurrentTime.getFormateDate());
        	mInfo.setStatus("1");
        	
        	mDatabaseAdapter.addMessage(mInfo);
        }
        
        mDatabaseAdapter.close();
    }

	public final static String trimPhoneNumber(String prefix, String number) {
		String s = number;
		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}
		return s;
	}
}