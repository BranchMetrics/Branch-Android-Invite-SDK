package io.branch.invite;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;

/**
 * Adapter for listing the email contacts. Adds an alpha indexer for the list.
 */
class PhoneContactListAdapter extends ContactListAdapter {

    public PhoneContactListAdapter(Context context, Cursor c, InviteContentView.IContactTabViewEvents callback, InviteBuilderParams builderParams) {
        super(context, c, callback, builderParams);
    }

    @Override
    String getIndexerColumnName() {
        return ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MyContact contact = null;
        String photoURI = null;

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        int contactType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        photoURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

        contact = new MyContact(id, displayName, phoneNum, contactType, photoURI);
        String ContactType = " (" + ContactsContract.CommonDataKinds.Phone.getTypeLabel(context_.getResources(), contact.getContactType(), "Other") + ") ";
        ((contactListItem) view).updateView(contact);

        view.setTag(contact);
        view.setOnClickListener(this);
    }

    public interface onContactItemSelectedCallabck {
        public void onContactItemSelected(String ContactName, String contactInfo, int contactType);
    }
}