package com.anibug.smsmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anibug.smsmanager.R;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;
import com.anibug.smsmanager.model.Message;
import com.anibug.smsmanager.utils.TextFilter;

public class ConversationListArrayAdapter extends ArrayAdapter<Message> {
	private static final int VIEW_ID = R.layout.conversation_list_item;
    private final TextFilter textFilter;
    private final LayoutInflater inflater;
    private final ContactManager contactManager;

	public ConversationListArrayAdapter(Context context, List<Message> objects, TextFilter textFilter) {
		super(context, VIEW_ID, objects);
        this.textFilter = textFilter;
        inflater = LayoutInflater.from(context);
        contactManager = new ContactManager(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = getItem(position);

		RelativeLayout view = (RelativeLayout) inflater.inflate(VIEW_ID, null, false);

		// ImageView image = (ImageView) view.findViewById(R.id.post_image);

		TextView dateCreatedView = (TextView) view.findViewById(R.id.message_date_created);
		dateCreatedView.setText(message.getDateCreated().toLocaleString());

		TextView contactView = (TextView) view.findViewById(R.id.message_contact);
        if (message.getStatus() == Message.STATUS_RECEIVED) {
            Contact contact = contactManager.getByPhoneNumber(message.getPhoneNumber());
		    contactView.setText(contact == null ? message.getPhoneNumber() : contact.getName());
        } else {
            contactView.setText(getContext().getString(R.string.me));
        }

		TextView bodyView = (TextView) view.findViewById(R.id.message_body);
		bodyView.setText(textFilter.filterText(message.getContent()));

        view.setTag(message);
		return view;
	}

}
