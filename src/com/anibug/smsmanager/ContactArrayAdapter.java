package com.anibug.smsmanager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anibug.smsmanager.model.Contact;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {
	private static final int VIEW_ID = R.layout.contact_list_item;
	private LayoutInflater inflater;

	public ContactArrayAdapter(Context context, List<Contact> objects) {
		super(context, VIEW_ID, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact message = getItem(position);

		RelativeLayout view = (RelativeLayout) inflater.inflate(VIEW_ID, null,
				false);

		TextView dateCreated = (TextView) view.findViewById(R.id.contact_name);
		dateCreated.setText(message.getName());

		TextView contact = (TextView) view.findViewById(R.id.contact_number);
		contact.setText(message.getPhoneNumber());

		return view;
	}

}
