package com.anibug.smsmanager;

import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.anibug.smsmanager.adapter.MessageArrayAdapter;
import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;
import com.anibug.smsmanager.model.WebClient;

public class SmsManagerActivity extends  ListActivity {
	MessageManager messageManager;
	ContactManager contactManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		messageManager = new MessageManager(getApplicationContext());
		contactManager = new ContactManager(getApplicationContext());

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent intent = new Intent(view.getContext(), ConversationActivity.class);

		    	// FIXME: We should assign the contact id somewhere else, 
		    	// instead of using the text of view.
		    	TextView contact = (TextView) view
						.findViewById(R.id.message_contact);
		    	intent.putExtra(Message.DataBase.PHONENUMBER, contact.getText().toString());

		    	startActivity(intent);
		    }
		});

		List<Message> messages = messageManager.getLastOneMessageForEachNumber();
		setListAdapter(new MessageArrayAdapter(getApplicationContext(), messages));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_contact_list:
			Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}