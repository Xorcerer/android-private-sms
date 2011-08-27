package com.anibug.smsmanager;

import java.util.Date;

import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	@Override
    public void onReceive(Context context, Intent intent) {
		MessageManager messageManager = new MessageManager(context);
		ContactManager contactManager = new ContactManager(context);

		Log.d("Reciever", "SMSReceiver, isOrderedBroadcast()="
				+ isOrderedBroadcast());

        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");

        // TODO: Filter out saved messages from broadcast.
        for (int n = 0; n < messages.length; n++) {
        	SmsMessage sms = SmsMessage.createFromPdu((byte[]) messages[n]);

        	Log.d(getClass().getName(), "receive message with phoneNumber: "+ sms.getDisplayOriginatingAddress() +
        			"content: " + sms.getDisplayOriginatingAddress());

        	Message message = new Message(sms.getDisplayOriginatingAddress(), new Date(),
        			sms.getDisplayMessageBody(), Message.STATUS_RECEIVED);

        	if (contactManager.match(message))
        		messageManager.save(message);
        }
    }
}