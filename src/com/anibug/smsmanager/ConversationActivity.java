package com.anibug.smsmanager;

import java.util.List;

import android.os.Bundle;

import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class ConversationActivity extends ListActivityBase<Message> {
	private MessageManager messageManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		messageManager = new MessageManager(this);

		updateListThenResetListener();
	}

	@Override
	protected void updateList() {
		final String number = getIntent().getStringExtra(Message.DataBase.PHONENUMBER);
		final List<Message> messages = messageManager.getMessages(number);

		setListAdapter(new ConversationListArrayAdapter(getApplicationContext(), messages));
		Utils.cancelNotification(this);
	}

	@Override
	protected int getContextMenuOptions() {
		return MENU_ITEM_REMOVE;
	}

	@Override
	protected String getContextMenuTitle(Message selected) {
		return selected.getShortContent();
	}

	@Override
	protected void onItemRemoved(Message selected) {
		messageManager.delete(selected);
	}
}
