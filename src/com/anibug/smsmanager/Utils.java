package com.anibug.smsmanager;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public final class Utils {

    public static final String PREF_NAME = "default";
    public static final String PREF_PASSWORD_REQUIRED = "password_required";
    public static final String PREF_PASSWORD = "password";
    public static final String DEFAULT_PASSWORD = "password";

    public static void cancelNotification(Context content) {
        final NotificationManager manager =
                (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(SmsReceiver.MSG_RECEIVED_NTF);
    }
}
