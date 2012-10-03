package com.anibug.smsmanager;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.anibug.smsmanager.adapter.ContactArrayAdapter;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactListActivity extends ListActivity {
	ContactManager contactManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Contacts");

		contactManager = new ContactManager(this);

		updateList();
	}

	private void updateList() {
		final List<Contact> contacts = contactManager.fetchAll();
		setListAdapter(new ContactArrayAdapter(this, contacts));
		getListView().setOnCreateContextMenuListener(this);
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
			showEditingDialog(null);
			break;
		default:
			assert false;
			break;
		}

		return true;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ContactEditActivity.NEW_CONTACT:
		case ContactEditActivity.EDIT_CONTACT:
			updateList();
			break;
		default:
			assert false;
			break;
		}
	}


	private int positionClicked = -1;

	private final int MENU_ITEM_REMOVE = 1;
	private final int MENU_ITEM_EDIT = 2;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// TODO: Put the name in title.
		menu.setHeaderTitle("Contact");

		try {
			// Save the position and recall it when item clicked.
			AdapterView.AdapterContextMenuInfo info;
		    info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		    positionClicked = info.position;
		} catch (final ClassCastException e) {
		    Log.e(getClass().getName(), "bad menuInfo", e);
		    return;
		}

		menu.add(Menu.NONE, MENU_ITEM_REMOVE, Menu.NONE, "Remove");
		menu.add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "Edit");
	}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	final Contact selected = (Contact) getListAdapter().getItem(positionClicked);
		switch (item.getItemId()) {
		case MENU_ITEM_REMOVE:
			contactManager.delete(selected);
			updateList();
			return true;
		case MENU_ITEM_EDIT:
			showEditingDialog(selected);
			return true;
		default:
			assert false;
			return true;
		}
    }
}
