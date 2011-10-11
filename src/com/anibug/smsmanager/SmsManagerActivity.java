package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class SmsManagerActivity extends ListActivity {
	public static final String PREFS_NAME = "default";

	private SharedPreferences settings;

	private MessageManager messageManager;
	private ContactManager contactManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

		messageManager = new MessageManager(this);
		contactManager = new ContactManager(this);

		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(view.getContext(),
								ConversationActivity.class);

						// FIXME: We should assign the contact id somewhere
						// else,
						// instead of using the text of view.
						TextView contact = (TextView) view
								.findViewById(R.id.message_contact);
						intent.putExtra(Message.DataBase.PHONENUMBER, contact
								.getText().toString());

						startActivity(intent);
					}
				});

		update();
	}

	private void update() {
		List<Message> messages = messageManager
				.getLastOneMessageForEachNumber();
		setListAdapter(new ConversationListArrayAdapter(this, messages));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean blocking = settings.getBoolean(MessageManager.PREF_BLOCKING,
				true);

		if (blocking)
			menu.findItem(R.id.item_blocking).setTitle(
					getString(R.string.blocking));
		else
			menu.findItem(R.id.item_blocking).setTitle(
					getString(R.string.not_blocking));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_contact_list:
			Intent intent = new Intent(this, ContactActivity.class);
			startActivity(intent);
			return true;
		case R.id.item_blocking:
			boolean blocking = settings.getBoolean(
					MessageManager.PREF_BLOCKING, true);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(MessageManager.PREF_BLOCKING, !blocking);
			editor.commit();
			return true;
		case R.id.item_refresh:
			update();
			return true;
		default:
			assert false : "An unhandle item selecting triggered.";
			return super.onOptionsItemSelected(item);
		}
	}

	private ReceivedAction receivedAction;
	private IntentFilter intentFilter;

	@Override
	protected void onResume() {

		super.onResume();
		intentFilter = new IntentFilter();
		intentFilter.addAction(SmsReceiver.SMS_RECEIVED_ACTION);
		receivedAction = new ReceivedAction();
		this.registerReceiver(receivedAction, intentFilter);
		Log.d("Reciever", "registerReceiver");

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(receivedAction);
		Log.d("Reciever", "unregisterReceiver");
	}

	class ReceivedAction extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(SmsReceiver.SMS_RECEIVED_ACTION)) {
				update();
			}
		}

	}
}