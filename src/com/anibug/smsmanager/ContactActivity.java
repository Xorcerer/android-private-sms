package com.anibug.smsmanager;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.style.LineHeightSpan.WithDensity;
import android.view.Menu;
import android.view.MenuItem;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_CONTACT_RESULT) {
			if (resultCode == Activity.RESULT_OK) {

				Contact picked = contactManager.getContactFromPickResult(data.getData());
				Toast.makeText(this, "Select "+ picked.getName() + " with number: " + picked.getPhoneNumber(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void ShowPickContactDialog() {
		  Intent intent = new Intent(Intent.ACTION_PICK);
		  intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		  startActivityForResult(intent, PICK_CONTACT_RESULT);
		
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
