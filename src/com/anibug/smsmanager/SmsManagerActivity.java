package com.anibug.smsmanager;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.anibug.smsmanager.database.DatabaseAdapter;
import com.anibug.smsmanager.model.MessageInfo;

public class SmsManagerActivity extends  ListActivity {
	ArrayList<Message> messages = new ArrayList<Message>();

	DatabaseAdapter mDatabaseAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] aStrings = {"123", "456", "789"};
		mDatabaseAdapter = new DatabaseAdapter(getApplicationContext());
		addTestDatabasePhoneNumber(aStrings);

		addTestData(10);

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	Intent intent = new Intent(view.getContext(), ConversationActivity.class);

		    	//need a place to out put
		    	outPutTestDatabasePrivateMessage();
		    	
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
	
	private void addTestDatabasePhoneNumber(String[] aStrings) {
		
		mDatabaseAdapter.open();
		
		for( String string : aStrings){
			
			Cursor mCursor = mDatabaseAdapter.getPhoneNumber(string);
			if(!mCursor.moveToFirst())
				mDatabaseAdapter.addPhoneNumber(string);
		}
		mDatabaseAdapter.close();

	}
	
	private void outPutTestDatabasePrivateMessage(){
		
		mDatabaseAdapter.open();

		Cursor mCursor = mDatabaseAdapter.getAllMessages();
		int count = mCursor.getCount();
		int i = 0;
		if(count >= 0){
			if(mCursor.moveToFirst()){
				do{
			        Log.d("Test", "result at index "+i +"is phoneNumber: " + mCursor.getString(mCursor.getColumnIndex(MessageInfo.DataBase.PHONENUMBER)) + "\n"
			        		+ "time: " + mCursor.getString(mCursor.getColumnIndex(MessageInfo.DataBase.TIME)) + "\n"
			        		+ "content: " + mCursor.getString(mCursor.getColumnIndex(MessageInfo.DataBase.CONTENT)) + "\n"
			        		+ "status: " + mCursor.getString(mCursor.getColumnIndex(MessageInfo.DataBase.STATUS)));
				}while(mCursor.moveToNext());
			}
		}
		mDatabaseAdapter.close();

	}

	public void addTestData(int count) {
		for (int i = 0; i < count; ++i) {
			Message m = new Message();
			m.setBody("Test message " + String.valueOf(i));
			messages.add(m);
		}
	}
}