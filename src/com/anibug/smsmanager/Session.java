package com.anibug.smsmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Session {
    // Ugly? Ref: http://stackoverflow.com/questions/4391720/android-how-can-i-get-a-resources-object-from-a-static-context
    private static Context context;

    public static void updateSessionFrom(Activity activity) {
        context = activity;
    }

    public static void packSessionTo(Intent intent) {
    }

    public static Context getContext() {
        return context;
    }
}
