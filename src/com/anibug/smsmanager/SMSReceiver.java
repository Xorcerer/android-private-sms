/**
 * If these code works, it was written by kya.
 * If it doesn`t, I don`t know who wrote it
 */
package com.anibug.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author kya
 *
 */
public class SMSReceiver extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SMSReceiver, isOrderedBroadcast()="
                + isOrderedBroadcast());
        
        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        SmsMessage smsMessage[] = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            
            if(SMSParser.getInstance().shouldAbortBoardCast(smsMessage[n])){
                System.out.println("abort broadcast message with sender:" + smsMessage[n].getOriginatingAddress()+" | " +
                		"with body: "+smsMessage[n].getMessageBody());
                this.abortBroadcast();
            }
        }
    }

}