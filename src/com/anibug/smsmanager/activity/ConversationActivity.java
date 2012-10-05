package com.anibug.smsmanager.activity;

import java.util.Date;
import java.util.List;

import android.os.Bundle;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import com.anibug.smsmanager.R;
import com.anibug.smsmanager.Session;
import com.anibug.smsmanager.Utils;
import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class ConversationActivity extends ListActivityBase<Message> {
	private MessageManager messageManager;
	private String number;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

		messageManager = new MessageManager(this);
		number = getIntent().getStringExtra(Message.DataBase.PHONE_NUMBER);
        setTitle(number);

        updateListThenResetListener();
	}

	@Override
	protected void updateList() {
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

    public void sendMessage(View v) {
        EditText messageEdit = (EditText) findViewById(R.id.outgoing_message_content);
        String content = messageEdit.getText().toString();

        if (Session.isLocked())
        {
            if (!content.equals("password"))
                return;

            Session.unlock();
            finish();
            return;
        }
        SmsManager sm = SmsManager.getDefault();

        sm.sendTextMessage(number, null, content, null, null);

        Message sentMessage = new Message(number, new Date(), content, Message.STATUS_SENT);
        messageManager.save(sentMessage);

        updateListThenResetListener();
    }
}
