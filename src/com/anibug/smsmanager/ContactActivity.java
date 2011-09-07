package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anibug.smsmanager.adapter.ContactArrayAdapter;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactActivity extends ListActivity {
	ContactManager contactManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Contacts");

		contactManager = new ContactManager(this);

		List<Contact> contacts = contactManager.fetchAll();
		setListAdapter(new ContactArrayAdapter(this, contacts));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_list_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_new_contact:
			ShowEditingDialog();
			break;
		default:
			assert false;
			break;
		}
		
		return true;
	}
	
	private void ShowEditingDialog() {
		  Intent intent = new Intent(this, ContactEditActivity.class);
		  startActivityForResult(intent, ContactEditActivity.NEW_CONTACT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ContactEditActivity.NEW_CONTACT:
			List<Contact> contacts = contactManager.fetchAll();
			setListAdapter(new ContactArrayAdapter(this, contacts));
			break;
		default:
			assert false;
			break;
		}
	}
}
