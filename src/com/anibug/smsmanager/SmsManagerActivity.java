package com.anibug.smsmanager;

import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.anibug.smsmanager.database.DatabaseAdapter;
import com.anibug.smsmanager.model.Message;

public class SmsManagerActivity extends  ListActivity {
	ArrayList<Message> messages = new ArrayList<Message>();

	DatabaseAdapter mDatabaseAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDatabaseAdapter = new DatabaseAdapter(getApplicationContext());

		addTestData(10);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent intent = new Intent(view.getContext(), ConversationActivity.class);
		    	
		    	// FIXME: We should assign the contact id somewhere else, 
		    	// instead of using the text of view.
		    	TextView contact = (TextView) view
						.findViewById(R.id.message_contact);
		    	intent.putExtra("contact", contact.getText().toString());

		    	startActivityForResult(intent, -1);
		    }
		});

		setListAdapter(new MessageArrayAdapter(getApplicationContext(), messages));

	}

	public void addTestData(int count) {
		for (int i = 0; i < count; ++i) {
			String content = "Test message " + String.valueOf(i);
			Message m = new Message("", new Date(), content, Message.STATUS_RECEIVED);
			messages.add(m);
		}
	}
}