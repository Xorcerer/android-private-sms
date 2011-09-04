package com.anibug.smsmanager;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.anibug.smsmanager.adapter.ContactArrayAdapter;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactActivity extends ListActivity {
	ContactManager contactManager;
	
	private static final int PICK_CONTACT_RESULT = 1001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		case R.id.item_pick_contact:
			ShowPickContactDialog();
			break;
		default:
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == PICK_CONTACT_RESULT && resultCode == Activity.RESULT_OK) {
			Contact picked = contactManager.getContactFromPickResult(intent.getData());
			if (picked != null)
				Toast.makeText(this, "Select "+ picked.getName() + " with number: " + picked.getPhoneNumber(), Toast.LENGTH_SHORT).show();
		}
	}

	private void ShowPickContactDialog() {
		  Intent intent = new Intent(Intent.ACTION_PICK);
		  intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		  startActivityForResult(intent, PICK_CONTACT_RESULT);
		
	}

	private void ShowEditingDialog() {
		final View editView = getLayoutInflater().inflate(R.layout.edit_contact,
				(ViewGroup) findViewById(R.id.edit_contact));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("New Contact");
		builder.setView(editView)
			   .setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						EditText nameEdit = (EditText) editView.findViewById(R.id.edit_contact_name);
						EditText numberEdit = (EditText) editView.findViewById(R.id.edit_contact_phonenumber);
						String name = nameEdit.getText().toString();
						String number = numberEdit.getText().toString();
						Contact contact = new Contact(name, number);
						contactManager.save(contact);
						
						((BaseAdapter)getListAdapter()).notifyDataSetChanged();
					}
			   })
			   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
			   });
		builder.show();
	}
}
