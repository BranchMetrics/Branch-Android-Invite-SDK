package io.branch.invite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;

import java.util.ArrayList;

/**
 * Adapter for listing the email contacts. Adds an alpha indexer for the list.
 */
class ContactListAdapterEmail extends ContactListAdapter {

    final Context context_;

    public ContactListAdapterEmail(Context context, Cursor c, InviteTabbedContentView.IContactTabViewEvents callback, InviteTabbedBuilderParams builderParams) {
        super(context, c, callback, builderParams);
        context_ = context;
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            photoURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.PHOTO_THUMBNAIL_URI));
        } else {
            photoURI = ContactsContract.Contacts.getLookupUri(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))).toString();
        }

        if (displayName == null || displayName.trim().length() < 1)
            displayName = emailAddress;

        contact = new MyContact(id, displayName, emailAddress, contactType, photoURI);

        view.setBackgroundColor(Color.WHITE);
        view.setTag(contact);
        ((contactListItem) view).updateView(contact);
        view.setOnClickListener(this);

    }

    @Override
    Intent getInviteIntent(String referralUrl, ArrayList<String> selectedContacts, String subject, String message) {
        Intent inviteIntent = new Intent();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message + "\n\n" + referralUrl);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, selectedContacts.toArray(new String[0]));

        if (BranchInviteUtil.isPackageInstalled(context_, "com.google.android.gm")) {
            inviteIntent.setPackage("com.google.android.gm");
        }
        return inviteIntent;
    }
}
