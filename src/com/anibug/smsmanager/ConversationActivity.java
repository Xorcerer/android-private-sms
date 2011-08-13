package com.anibug.smsmanager;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

public class ConversationActivity extends ListActivity {
	ArrayList<Message> messages = new ArrayList<Message>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		setListAdapter(new MessageArrayAdapter(getApplicationContext(), messages));
	}

}
