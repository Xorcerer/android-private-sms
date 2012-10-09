package com.anibug.smsmanager.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.anibug.smsmanager.R;
import com.anibug.smsmanager.SmsReceiver;
import com.anibug.smsmanager.Utils;
import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class ConversationListActivity extends ListActivityBase<Message> {

    private SharedPreferences settings;

	private MessageManager messageManager;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        settings = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);

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

                        Message message = (Message) view.getTag();
                        intent.putExtra(Message.DataBase.PHONE_NUMBER, message.getPhoneNumber());

						startActivity(intent);
					}
				});

		updateList();
	}

	@Override
	protected void updateList() {
		List<Message> messages;
        messages = messageManager.getLastOneMessageForEachNumber();
		setListAdapter(new ConversationListArrayAdapter(this, messages));
		Utils.cancelNotification(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
        int stringId = settings.getBoolean(MessageManager.PREF_BLOCKING, true) ?
                R.string.blocking : R.string.not_blocking;
        menu.findItem(R.id.item_blocking).setTitle(getString(stringId));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_contact_list:
			startActivity(new Intent(this, ContactListActivity.class));
			return true;
		case R.id.item_blocking:
			final boolean blocking = settings.getBoolean(
					MessageManager.PREF_BLOCKING, true);
			final SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(MessageManager.PREF_BLOCKING, !blocking);
			editor.commit();
			return true;
        case R.id.item_preferences:
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
		default:
			assert false : "An unhandled item selecting triggered.";
			return super.onOptionsItemSelected(item);
		}
	}

	private ReceivedAction receivedAction;

    @Override
	protected void onResume() {
		super.onResume();

        IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SmsReceiver.SMS_RECEIVED_ACTION);
		receivedAction = new ReceivedAction();
		registerReceiver(receivedAction, intentFilter);

		updateList();
	}

	@Override
	protected void onPause() {
		super.onPause();

        unregisterReceiver(receivedAction);
	}

	class ReceivedAction extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(SmsReceiver.SMS_RECEIVED_ACTION)) {
				updateList();
			}
		}
	}

	@Override
	public void onItemRemoved(Message selected) {
		messageManager.deleteAllByPhoneNumber(selected.getPhoneNumber());
	}

	@Override
	protected int getContextMenuOptions() {
		return MENU_ITEM_REMOVE;
	}

	@Override
	protected String getContextMenuTitle(Message selected) {
		// TODO: Show contact name instead of phone number.
		return "All messages of \"" + selected.getPhoneNumber() + "\"";
	}
}