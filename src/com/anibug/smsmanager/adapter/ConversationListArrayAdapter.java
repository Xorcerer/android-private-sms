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
import com.anibug.smsmanager.model.Message;

public class ConversationListArrayAdapter extends ArrayAdapter<Message> {
	private static final int VIEW_ID = R.layout.message_list_item;
	private LayoutInflater inflater;

	public ConversationListArrayAdapter(Context context, List<Message> objects) {
		super(context, VIEW_ID, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = getItem(position);

		RelativeLayout view = (RelativeLayout) inflater.inflate(VIEW_ID, null,
				false);

		// ImageView image = (ImageView) view.findViewById(R.id.post_image);

		TextView dateCreated = (TextView) view
				.findViewById(R.id.message_date_created);
		dateCreated.setText(message.getDateCreated().toLocaleString());

		TextView contact = (TextView) view
				.findViewById(R.id.message_contact);
		contact.setText(message.getPhoneNumber());

		TextView body = (TextView) view.findViewById(R.id.message_body);
		body.setText(message.getContent());

		return view;
	}

}
