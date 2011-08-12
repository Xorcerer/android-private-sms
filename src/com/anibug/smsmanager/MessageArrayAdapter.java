package com.anibug.smsmanager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
	private static final int VIEW_ID = R.layout.list_item_multimedia;
	private LayoutInflater inflater;

	public MessageArrayAdapter(Context context, List<Message> objects) {
		super(context, VIEW_ID, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message post = getItem(position);

		RelativeLayout view = (RelativeLayout) inflater.inflate(VIEW_ID, null,
				false);

		// ImageView image = (ImageView) view.findViewById(R.id.post_image);

		TextView dateCreated = (TextView) view
				.findViewById(R.id.post_date_created);
		dateCreated.setText(post.getDateCreated().toLocaleString());

		TextView body = (TextView) view.findViewById(R.id.post_body);
		body.setText(post.getBody());

		return view;
	}

}
