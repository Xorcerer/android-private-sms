package com.anibug.smsmanager;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
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
		ShowEditingDialog();
		return true;
	}
	
	private void ShowEditingDialog() {
		final EditText edit = new EditText(this);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(edit)
			   .setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String number = edit.getText().toString();
						Contact contact = new Contact();
						contact.setPhoneNumber(number);
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
