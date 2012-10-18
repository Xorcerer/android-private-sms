package com.anibug.smsmanager.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.anibug.smsmanager.SmsReceiver;
import com.anibug.smsmanager.activity.ListActivityBase;

public class ReceivedActionHelper extends BroadcastReceiver {
    private final ListActivityBase<?> activity;

    public ReceivedActionHelper(ListActivityBase<?> activity) {
        this.activity = activity;
    }

    public static void cancelNotification(Context content) {
        final NotificationManager manager =
                (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(SmsReceiver.MSG_RECEIVED_NTF);
    }

    public void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsReceiver.SMS_RECEIVED_ACTION);
        activity.registerReceiver(this, intentFilter);

        activity.updateList();
    }

    public void onPause() {
        activity.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SmsReceiver.SMS_RECEIVED_ACTION)) {
            activity.updateList();
        }
    }
}