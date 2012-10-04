package com.anibug.smsmanager;

import android.app.NotificationManager;
import android.content.Context;

public final class Utils {

    // Ugly? Ref: http://stackoverflow.com/questions/4391720/android-how-can-i-get-a-resources-object-from-a-static-context
    private static Context context;
    public static boolean Locked = true;

    public static void setContext(Context c) {
        context = c;
    }

    public static Context getContext() {
        return context;
    }

    public static void cancelNotification(Context content) {
        final NotificationManager manager =
                (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(SmsReceiver.MSG_RECEIVED_NTF);
    }
}
