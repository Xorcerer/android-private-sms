package com.anibug.smsmanager;

import android.app.NotificationManager;
import android.content.Context;

public final class Utils {
	public static void cancelNotification(Context content) {
		final NotificationManager manager =
				(NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(SmsReceiver.MSG_RECEIVED_NTF);
	}
}
