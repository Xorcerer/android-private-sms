package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

// TODO: Extends ListActivityBase.
public class ConversationActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);

		final MessageManager messageManager = new MessageManager(getApplicationContext());
		final String number = getIntent().getStringExtra(Message.DataBase.PHONENUMBER);
		final List<Message> messages = messageManager.getMessages(number);

		setListAdapter(new ConversationListArrayAdapter(getApplicationContext(), messages));
	}

}
