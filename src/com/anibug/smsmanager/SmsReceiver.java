package com.anibug.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	
	private String _phoneNumber;
	private String _content;

	public SmsReceiver() {
		_phoneNumber = "";
		_content = "";
	}
	
	@Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Reciever", "SMSReceiver, isOrderedBroadcast()="
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

        Log.d("Reciever", "receive message with phoneNumber: "+ _phoneNumber + "content: " + _content);
        
        // TODO: Save message here.
    }

	public final static String trimPhoneNumber(String prefix, String number) {
		String s = number;
		if (prefix.length() > 0 && number.startsWith(prefix)) {
			s = number.substring(prefix.length());
		}
		return s;
	}
}