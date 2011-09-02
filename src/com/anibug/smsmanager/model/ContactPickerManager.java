package com.anibug.smsmanager.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactPickerManager  {
	
	public static Contact getContactFromResult(Context ctx, Uri data){
		
		try {
            ContentResolver contect_resolver = ctx.getContentResolver();
			Cursor c = contect_resolver.query(data, null, null, null, null);

			if (c.moveToFirst()) {
				String name = null;
				String number = null;

	            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
	                
                Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

                if (phoneCur.moveToFirst()) {
                    name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    number = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

				name = c.getString(c
						.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				
				Contact selected = new Contact(number, -1);
				selected.setName(name);
				
				if(!c.isClosed())
					c.close();
				
				return selected;
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("IllegalArgumentException :: ", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error :: ", e.toString());
		}
		
		return null;
	}

}
