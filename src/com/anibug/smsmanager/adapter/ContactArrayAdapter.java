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
import com.anibug.smsmanager.utils.TextMasker;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {
	private static final int VIEW_ID = R.layout.contact_list_item;
    private final TextMasker textMasker;
    private final LayoutInflater inflater;

	public ContactArrayAdapter(Context context, List<Contact> objects, TextMasker textMasker) {
		super(context, VIEW_ID, objects);
        this.textMasker = textMasker;
        inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact = getItem(position);

        View view = convertView != null ?
                convertView : (RelativeLayout) inflater.inflate(VIEW_ID, null, false);

		view.setId(position);

		TextView dateCreated = (TextView) view.findViewById(R.id.contact_name);
		dateCreated.setText(textMasker.maskText(contact.getName()));

		TextView contactNumber = (TextView) view.findViewById(R.id.contact_number);
		contactNumber.setText(textMasker.maskNumber(contact.getPhoneNumber()));

        view.setTag(contact);
		return view;
	}

}
