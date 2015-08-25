package io.branch.invite.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.View;

import java.util.ArrayList;

import io.branch.invite.TabBuilderParams;

/**
 * Adapter for listing the email contacts. Adds an alpha indexer for the list.
 */
class ContactListAdapterPhone extends ContactListAdapter {

    public ContactListAdapterPhone(Context context, Cursor c, InviteTabbedContentView.IContactTabViewEvents callback, TabBuilderParams builderParams) {

        super(context, c, callback, builderParams);
    }

    @Override
    String getIndexerColumnName() {
        return ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MyContact contact;
        String photoURI;

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        int contactType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            photoURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
        }
        else{
            photoURI = ContactsContract.Contacts.getLookupUri(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))).toString();
        }
        contact = new MyContact(id, displayName, phoneNum, contactType, photoURI);
        String ContactType = " (" + ContactsContract.CommonDataKinds.Phone.getTypeLabel(context_.getResources(), contact.getContactType(), "Other") + ") ";
        ((contactListItem) view).updateView(contact);

        view.setTag(contact);
        view.setOnClickListener(this);
    }

    public interface onContactItemSelectedCallback {
        public void onContactItemSelected(String ContactName, String contactInfo, int contactType);
    }

    @Override
    public Intent getInviteIntent(String referralUrl, ArrayList<String> selectedContacts, String subject, String message) {
        Intent inviteIntent;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            inviteIntent = new Intent(Intent.ACTION_SENDTO);
            inviteIntent.addCategory(Intent.CATEGORY_DEFAULT);
            inviteIntent.setType("vnd.android-dir/mms-sms");
            inviteIntent.setData(Uri.parse("sms:" + Uri.encode(BranchInviteUtil.formatListToCSV(selectedContacts))));
            inviteIntent.putExtra("sms_body", message + "\n" + referralUrl);
        } else {
            inviteIntent = new Intent(Intent.ACTION_SENDTO);
            inviteIntent.setType("text/plain");
            inviteIntent.putExtra("sms_body", message + "\n" + referralUrl);
            inviteIntent.setData(Uri.parse("smsto:" + Uri.encode(BranchInviteUtil.formatListToCSV(selectedContacts))));

            // In any old version of SMS app checking for subject and text params
            inviteIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            inviteIntent.putExtra(android.content.Intent.EXTRA_TEXT, message + "\n" + referralUrl);
            inviteIntent.putExtra("address", BranchInviteUtil.formatListToCSV(selectedContacts));

            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context_);
            inviteIntent.setPackage(defaultSmsPackageName);
        }
        return inviteIntent;
    }
}