package com.anibug.smsmanager.activity;

import java.util.Date;
import java.util.List;

import android.os.Bundle;

import android.telephony.SmsManager;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.anibug.smsmanager.R;
import com.anibug.smsmanager.utils.ReceivedActionHelper;
import com.anibug.smsmanager.adapter.ConversationListArrayAdapter;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.model.MessageManager;

public class ConversationActivity extends ListActivityBase<Message> {
    private final ReceivedActionHelper receivedActionHelper = new ReceivedActionHelper(this);

    private MessageManager messageManager;
	private String number;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

		messageManager = new MessageManager(this);
		number = getIntent().getStringExtra(Message.DataBase.PHONE_NUMBER);
        setTitle(number);

        updateList();
	}

	@Override
    public void updateList() {
		final List<Message> messages = messageManager.getMessages(number);

		setListAdapter(new ConversationListArrayAdapter(this, messages));
		receivedActionHelper.cancelNotification();
	}

	@Override
	protected int getContextMenuOptions() {
		return MENU_ITEM_REMOVE | MENU_ITEM_COPY_TEXT;
	}

	@Override
	protected String getContextMenuTitle(Message selected) {
		return selected.getShortContent();
	}

	@Override
	protected void onItemRemoved(Message selected) {
		messageManager.delete(selected);
	}

    @Override
    protected void onItemTextCopied(Message selected) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(selected.getContent());
    }

    public void sendMessage(View v) {
        EditText messageEdit = (EditText) findViewById(R.id.outgoing_message_content);
        String content = messageEdit.getText().toString();
        if (content.length() == 0) {
            Toast.makeText(this, R.string.cannot_send_empty_message, Toast.LENGTH_SHORT).show();
            return;
        }
        messageEdit.getEditableText().clear();

        SmsManager.getDefault().sendTextMessage(number, null, content, null, null);

        Message sentMessage = new Message(number, new Date(), content, Message.STATUS_SENT);
        messageManager.save(sentMessage);

        updateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        receivedActionHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        receivedActionHelper.onPause();
    }

}
