package com.anibug.smsmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Session {
    // Ugly? Ref: http://stackoverflow.com/questions/4391720/android-how-can-i-get-a-resources-object-from-a-static-context
    private static Context context;
    private static boolean locked = true;
    private static final String LOCKED_INTENT_KEY = "locked";

    public static void updateSessionFrom(Activity activity) {
        context = activity;
        locked = activity.getIntent().getBooleanExtra(LOCKED_INTENT_KEY, true);
    }

    public static void packSessionTo(Intent intent) {
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
}
