package com.anibug.smsmanager;


import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anibug.smsmanager.adapter.ContactArrayAdapter;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactListActivity extends ListActivityBase<Contact> {
	ContactManager contactManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Contacts");

		contactManager = new ContactManager(this);

		updateListThenResetListener();
	}

	@Override
	protected int getContextMenuOptions() {
		return MENU_ITEM_EDIT | MENU_ITEM_REMOVE;
	}

	@Override
	protected String getContextMenuTitle(Contact selected) {
		return "Contact \"" + selected.getName() + "\"";
	}

	@Override
	protected void updateList() {
		final List<Contact> contacts = contactManager.fetchAll();
		setListAdapter(new ContactArrayAdapter(this, contacts));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_list_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void showEditingDialog(Contact contact) {
		final Intent intent = new Intent(this, ContactEditActivity.class);
		if (contact == null) {
			startActivityForResult(intent, ContactEditActivity.NEW_CONTACT);
		} else {
			intent.putExtra("contactId", contact.getId());
			startActivityForResult(intent, ContactEditActivity.EDIT_CONTACT);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_new_contact:
			showEditingDialog(null);
			break;
		default:
			assert false;
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ContactEditActivity.NEW_CONTACT:
		case ContactEditActivity.EDIT_CONTACT:
			updateListThenResetListener();
			break;
		default:
			assert false;
			break;
		}
	}

	@Override
	protected void onItemRemoved(Contact selected) {
		contactManager.delete(selected);
	}

	@Override
	protected void onItemUpdated(Contact selected) {
		showEditingDialog(selected);
	}
}
