package io.branch.invite.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;

import java.util.ArrayList;

import io.branch.invite.TabBuilderParams;

/**
 * Adapter for listing the email contacts. Adds an alpha indexer for the list.
 */
class ContactListAdapterEmail extends ContactListAdapter {

    final Context context_;

    public ContactListAdapterEmail(Context context, Cursor c, InviteTabbedContentView.IContactTabViewEvents callback, TabBuilderParams builderParams) {
        super(context, c, callback, builderParams);
        context_ = context;
    }

    @Override
    String getIndexerColumnName() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return ContactsContract.CommonDataKinds.Email.ADDRESS;
        }else{
            return ContactsContract.CommonDataKinds.Email.DATA;
        }
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyContact contact;
        String photoURI;
        String emailAddress;

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
        int contactType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            photoURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.PHOTO_THUMBNAIL_URI));
            emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        } else {
            photoURI = ContactsContract.Contacts.getLookupUri(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))).toString();
            emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }

        if (displayName == null || displayName.trim().length() < 1)
            displayName = emailAddress;

        contact = new MyContact(id, displayName, emailAddress, contactType, photoURI);

        view.setTag(contact);
        ((contactListItem) view).updateView(contact);
        view.setOnClickListener(this);

    }

    @Override
    public Intent getInviteIntent(String referralUrl, ArrayList<String> selectedContacts, String subject, String message) {
        Intent inviteIntent = new Intent();
        inviteIntent = new Intent(Intent.ACTION_SEND);
        inviteIntent.setType("text/plain");
        inviteIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        inviteIntent.putExtra(android.content.Intent.EXTRA_TEXT, message + "\n\n" + referralUrl);
        inviteIntent.putExtra(android.content.Intent.EXTRA_EMAIL, selectedContacts.toArray(new String[selectedContacts.size()]));

        if (BranchInviteUtil.isPackageInstalled(context_, "com.google.android.gm")) {
            inviteIntent.setPackage("com.google.android.gm");
        }
        return inviteIntent;
    }
}
