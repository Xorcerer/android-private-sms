package com.anibug.smsmanager;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;

public class SMSManagerActivity extends  ListActivity {
	ArrayList<Message> messages = new ArrayList<Message>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addTestData(10);

		getListView().setItemsCanFocus(false);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        Toast.makeText(getApplicationContext(), "Clicked NO." + position + " item", Toast.LENGTH_SHORT).show();
		    }
		});

		setListAdapter(new MessageArrayAdapter(getApplicationContext(), messages));

	}
	
	public void addTestData(int count) {
		for (int i = 0; i < count; ++i) {
			Message m = new Message();
			m.setBody("Test message " + String.valueOf(i));
			messages.add(m);
		}
	}
}