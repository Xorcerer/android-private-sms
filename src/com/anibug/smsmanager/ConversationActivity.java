package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class ConversationActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);

		MessageManager messageManager = new MessageManager(getApplicationContext());
		String number = getIntent().getStringExtra(Message.DataBase.PHONENUMBER);
		List<Message> messages = messageManager.getMessages(number);
		
		setListAdapter(new MessageArrayAdapter(getApplicationContext(), messages));
	}

}
