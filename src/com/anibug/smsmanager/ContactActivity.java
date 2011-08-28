package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ContactManager contactManager = new ContactManager(getApplicationContext());
		List<Contact> contacts = contactManager.fetchAll();

		setListAdapter(new ContactArrayAdapter(getApplicationContext(), contacts));
	}

}
