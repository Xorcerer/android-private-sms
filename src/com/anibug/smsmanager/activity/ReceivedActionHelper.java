package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.anibug.smsmanager.SmsReceiver;

public class ReceivedActionHelper extends BroadcastReceiver {
    private final ListActivityBase<?> activity;

    public ReceivedActionHelper(ListActivityBase<?> activity) {
        this.activity = activity;
    }

    protected void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsReceiver.SMS_RECEIVED_ACTION);
        activity.registerReceiver(this, intentFilter);

        activity.updateList();
    }

    protected void onPause() {
        activity.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SmsReceiver.SMS_RECEIVED_ACTION)) {
            activity.updateList();
        }
    }
}