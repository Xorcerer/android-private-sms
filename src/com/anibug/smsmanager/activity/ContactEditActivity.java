package com.anibug.smsmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anibug.smsmanager.R;
import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

public class ContactEditActivity extends Activity {
	private static final int PICK_CONTACT_RESULT = 1001;

	public static final int NEW_CONTACT = 1;
	public static final int EDIT_CONTACT = 2;

	private ContactManager contactManager;
	private EditText nameEdit;
	private EditText numberEdit;

	private Contact contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit);

		contactManager = new ContactManager(this);
		nameEdit = (EditText) findViewById(R.id.edit_contact_name);
		numberEdit = (EditText) findViewById(R.id.edit_contact_phonenumber);

		final long contactId = getIntent().getLongExtra("contactId", -1);
		if (contactId == -1) {
			setTitle("New Contact");
			contact = new Contact();
		} else {
			setTitle("Edit Contact");
			contact = contactManager.get(contactId);
			nameEdit.setText(contact.getName());
			numberEdit.setText(contact.getPhoneNumber());
		}

		final Button pickerButton = (Button) findViewById(R.id.contact_picker_button);
		pickerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ShowPickContactDialog();
			}
		});
	}

	public void save(View v) {
		contact.setName(nameEdit.getText().toString());
		contact.setPhoneNumber(numberEdit.getText().toString());
		contactManager.save(contact);
		setResult(RESULT_OK);
		finish();
	}

	public void cancel(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == PICK_CONTACT_RESULT && resultCode == RESULT_OK) {
			final Contact picked = contactManager.getContactFromPickResult(intent.getData());
			if (picked != null) {
				nameEdit.setText(picked.getName());
				numberEdit.setText(picked.getPhoneNumber());
			}
		}
	}

	private void ShowPickContactDialog() {
		  final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		  intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		  startActivityForResult(intent, PICK_CONTACT_RESULT);
	}

}
