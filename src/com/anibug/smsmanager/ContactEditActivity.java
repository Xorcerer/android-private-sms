package com.anibug.smsmanager;

import com.anibug.smsmanager.model.Contact;
import com.anibug.smsmanager.model.ContactManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactEditActivity extends Activity {
	private static final int PICK_CONTACT_RESULT = 1001;
	
	public static final int NEW_CONTACT = 1;
	
	//TODO: Use the activity for editing contacts.
	public static final int EDIT_CONTACT = 2;

	ContactManager contactManager;
	EditText nameEdit;
	EditText numberEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
		setTitle("New Contact");

		contactManager = new ContactManager(this);
		nameEdit = (EditText) findViewById(R.id.edit_contact_name);
		numberEdit = (EditText) findViewById(R.id.edit_contact_phonenumber);


		final Button pickerButton = (Button) findViewById(R.id.contact_picker_button);
		pickerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ShowPickContactDialog();
			}
		});
	}

	public void ok(View v) {
		Contact contact = new Contact(nameEdit.getText().toString(), numberEdit.getText().toString());
		contactManager.save(contact);
		setResult(RESULT_OK);
	}

	public void cancel(View v) {
		setResult(RESULT_CANCELED);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == PICK_CONTACT_RESULT && resultCode == RESULT_OK) {
			Contact picked = contactManager.getContactFromPickResult(intent.getData());
			if (picked != null) {
				nameEdit.setText(picked.getName());
				numberEdit.setText(picked.getPhoneNumber());
			}
		}
	}

	private void ShowPickContactDialog() {
		  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		  intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		  startActivityForResult(intent, PICK_CONTACT_RESULT);
	}

}
