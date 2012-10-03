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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class SmsManagerActivity extends ListActivity {
	public static final String PREFS_NAME = "default";

	private SharedPreferences settings;

	private MessageManager messageManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

		messageManager = new MessageManager(this);
		// FIXME: For loading SQL definitions of ContactManager.
		// Bad design.
		new ContactManager(this);

		getListView().setOnItemClickListener(
				new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						final Intent intent = new Intent(view.getContext(),
								ConversationActivity.class);

						// FIXME: We should assign the contact id somewhere
						// else, instead of using the text of view.
						final TextView contact = (TextView) view
								.findViewById(R.id.message_contact);
						intent.putExtra(Message.DataBase.PHONENUMBER, contact
								.getText().toString());

						startActivity(intent);
					}
				});

		updateMessageList();
	}

	private void updateMessageList() {
		final List<Message> messages = messageManager
				.getLastOneMessageForEachNumber();
		setListAdapter(new ConversationListArrayAdapter(this, messages));
		getListView().setOnCreateContextMenuListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final boolean blocking = settings.getBoolean(MessageManager.PREF_BLOCKING,
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
			final Intent intent = new Intent(this, ContactListActivity.class);
			startActivity(intent);
			return true;
		case R.id.item_blocking:
			final boolean blocking = settings.getBoolean(
					MessageManager.PREF_BLOCKING, true);
			final SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(MessageManager.PREF_BLOCKING, !blocking);
			editor.commit();
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

		updateMessageList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(receivedAction);
	}

	class ReceivedAction extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(SmsReceiver.SMS_RECEIVED_ACTION)) {
				updateMessageList();
			}
		}
	}

	private int positionClicked = -1;

	private final int MENU_ITEM_REMOVE = 1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// TODO: Put the name in title.
		menu.setHeaderTitle("Conversation");

		try {
			// Save the position and recall it when item clicked.
			AdapterView.AdapterContextMenuInfo info;
		    info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		    positionClicked = info.position;
		} catch (final ClassCastException e) {
		    Log.e(getClass().getName(), "bad menuInfo", e);
		    return;
		}

		menu.add(Menu.NONE, MENU_ITEM_REMOVE, Menu.NONE, "Remove");
	}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	final Message selected = (Message) getListAdapter().getItem(positionClicked);
		switch (item.getItemId()) {
		case MENU_ITEM_REMOVE:
			messageManager.deleteAllByPhoneNumber(selected.getPhoneNumber());
			updateMessageList();
			return true;
		default:
			assert false;
			return true;
		}
    }
}