package com.anibug.smsmanager;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public final class Utils {

    // Ugly? Ref: http://stackoverflow.com/questions/4391720/android-how-can-i-get-a-resources-object-from-a-static-context
    private static Context context;
    private static boolean locked = true;
    private static final String LOCKED_INTENT_KEY = "locked";

    public static void setActivityContext(Activity activity) {
        context = activity;
        locked = activity.getIntent().getBooleanExtra(LOCKED_INTENT_KEY, true);
    }

    public static void packActivityContext(Intent intent) {
        intent.putExtra(LOCKED_INTENT_KEY, locked);
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isLocked() {
        return locked;
    }

    public static void lock() {
        locked = true;
    }

    public static void unlock() {
        locked = false;
    }

    public static void cancelNotification(Context content) {
        final NotificationManager manager =
                (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(SmsReceiver.MSG_RECEIVED_NTF);
    }
}
