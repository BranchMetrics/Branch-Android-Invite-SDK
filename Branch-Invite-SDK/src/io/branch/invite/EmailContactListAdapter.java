package io.branch.invite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.View;

/**
 * Adapter for listing the email contacts. Adds an alpha indexer for the list.
 */
class EmailContactListAdapter extends ContactListAdapter {

    public EmailContactListAdapter(Context context, Cursor c, InviteContentView.IContactTabViewEvents callback ,InviteBuilderParams builderParams) {
        super(context, c, callback, builderParams);
    }

    @Override
    String getIndexerColumnName() {
        return ContactsContract.CommonDataKinds.Email.ADDRESS;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyContact contact = null;
        String photoURI = null;

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
        String emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        int contactType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
        photoURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.PHOTO_THUMBNAIL_URI));

        if (displayName == null || displayName.trim().length() < 1)
            displayName = emailAddress;

        contact = new MyContact(id, displayName, emailAddress, contactType, photoURI);

        view.setBackgroundColor(Color.WHITE);
        view.setTag(contact);
        ((contactListItem) view).updateView(contact);
        view.setOnClickListener(this);
    }
}
