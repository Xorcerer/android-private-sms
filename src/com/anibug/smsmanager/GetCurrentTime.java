package com.anibug.smsmanager;

import java.util.Calendar;

public class GetCurrentTime {
	
	public static String getFormateDate(){
		
		Calendar calendar = Calendar.getInstance();
		int month = (calendar.get(Calendar.MONTH)+1);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String systemTime = 
				(month<10?"0"+month:month)+"/"+(day<10?"0"+day:day)+"  "
				+(hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute)+":"+(second<10?"0"+second:second);
		return systemTime;
	}

	public static String getHourAndMinute(){
		
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return (hour<10?"0"+hour:hour)+":"+(minute<10?"0"+minute:minute);
	}
	
}
