package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class SmsManagerActivity extends  ListActivity {
	List<Message> messages;
	MessageManager messageManager;
	ContactManager contactManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		messageManager = new MessageManager(getApplicationContext());
		contactManager = new ContactManager(getApplicationContext());
		messages = messageManager.getLastOneMessageForEachNumber();
		
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
}