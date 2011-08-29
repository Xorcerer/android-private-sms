package com.anibug.smsmanager;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anibug.smsmanager.adapter.ContactArrayAdapter;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactActivity extends ListActivity {
	ContactManager contactManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contactManager = new ContactManager(
				getApplicationContext());
		List<Contact> contacts = contactManager.fetchAll();

		setListAdapter(new ContactArrayAdapter(getApplicationContext(),
				contacts));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_list_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CreateEditingDialog().show();
		return true;
	}
	
	private Dialog CreateEditingDialog() {
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

		// FIXME: The following line broken.
		View view = layoutInflater.inflate(R.layout.edit_contact_dialog,
				(ViewGroup) findViewById(R.id.edit_contact_layout));

		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setView(view)
			   .setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Dialog d = (Dialog)dialog;
						EditText edit = (EditText)d.findViewById(R.id.edit_phone_number);
						// TODO: Get the phone number and add a contact.
					}
			   })
			   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
			   });

		return builder.create();
	}
}
