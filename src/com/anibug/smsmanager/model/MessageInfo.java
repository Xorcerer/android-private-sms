package com.anibug.smsmanager.model;

public class MessageInfo {
	
	private String _phoneNumber;
	private String _time;
	private String _content;
	private String _status;
	
	public String getPhoneNumber() {
		
		return _phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		
		_phoneNumber = phoneNumber;
	}
	
	public String getTime() {
		
		return _time;
	}
	public void setTime(String time) {
		
		_time = time;
	}

	public String getContent() {
		
		return _content;
	}
	public void setContent(String content) {
		
		_content = content;
	}
	
	public String getStatus() {
		
		return _status;
	}
	public void setStatus(String status) {
		
		_status = status;
	}
	
	public class DataBase
	{
		public static final String PHONENUMBER = "number";
		
		public static final String TIME = "time";
		
		public static final String CONTENT = "content";
		
		public static final String STATUS = "status";
	}
}
