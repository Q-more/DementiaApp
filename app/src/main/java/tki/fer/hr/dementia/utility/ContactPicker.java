package tki.fer.hr.dementia.utility;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Created by lucija on 02.03.18..
 */

public class ContactPicker {
    public static final int RESULT_PICK_CONTACT = 8550;

    @Data
    @AllArgsConstructor
    public static class ContactDetails {
        private String name;
        private String phoneNumber;
        private String photo;
    }

    public static Intent pickContactIntent() {
        return new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    }

    @SuppressLint("Recycle")
    public static ContactDetails contactPicked(Intent data, ContentResolver resolver) {
        Cursor cursor;
        if (data == null) return null;
        try {
            Uri uri = data.getData();
            assert uri != null;
            cursor = resolver.query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);


            return new ContactDetails(cursor.getString(nameIndex), cursor.getString(phoneIndex), cursor.getString(photoIdIdx));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
