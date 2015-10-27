package io.branch.invite.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import io.branch.invite.TabBuilderParams;

/**
 * Abstract class for contact list adapter. Handles the contact listing and selecting form the list.
 * Adds a SectionIndexer for alphabetic indexing. This class creates the view item for the child classes.
 */
abstract class ContactListAdapter extends CursorAdapter implements View.OnClickListener, SectionIndexer {

    /* Context for creating the adapter. */
    protected Context context_;
    /* Callback for notifying when user select an item in the list. */
    protected InviteTabbedContentView.IContactTabViewEvents contactItemSelectedCallBack_;
    /* Drawable to show when there is no profile picture. */
    Drawable defaultContactPic_;
    /* List of contact selected. */
    private HashMap<String, MyContact> selectedContactMap_;

    /* Alphabet indexer for the list view */
    AlphabetIndexer mAlphabetIndexer;
    /* Color for the selected item in contact list */
    final int selectedItemColor_;
    /* Selection mode */
    private final boolean isSingleSelect_;

    /**
     * Create an instance and initialises the list params
     *
     * @param context a {@link Context} for creating the list
     * @param c       A {@link Cursor} for creating contact list
     */
    public ContactListAdapter(Context context, Cursor c, InviteTabbedContentView.IContactTabViewEvents contactItemSelected, TabBuilderParams inviteBuilderParams) {
        super(context, c, false);
        context_ = context;
        defaultContactPic_ = inviteBuilderParams.defaultContactPic_;
        isSingleSelect_ = inviteBuilderParams.isSingleSelect_;
        contactItemSelectedCallBack_ = contactItemSelected;
        selectedContactMap_ = new HashMap<>();
        selectedItemColor_ = inviteBuilderParams.selectedItemBackGroundColor_;

        //Initialise indexer
        mAlphabetIndexer = new AlphabetIndexer(c, c.getColumnIndex(getIndexerColumnName()),
                " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        mAlphabetIndexer.setCursor(c);//Sets a new cursor as the data set and resets the cache of indices.
    }

    public ArrayList<String> getSelectedContacts() {
        ArrayList<String> selectedContactArray = new ArrayList<>();
        Set<String> keys = selectedContactMap_.keySet();
        for (String key : keys) {
            selectedContactArray.add(selectedContactMap_.get(key).getContactInfo());
        }
        return selectedContactArray;
    }

    /**
     * Return the indexer column id for setting the alphabet indexer.
     *
     * @return columnID for the indexer column in the cursor
     */
    abstract String getIndexerColumnName();

    /**
     * Get the intent to share the invitation url.
     *
     * @param referralUrl      The invitation url
     * @param selectedContacts A {@link ArrayList<String>} with selected contacts.
     * @param subject          Subject of the message to be shared.
     * @param message          Message to be shared to the invitee
     * @return An {@link Intent} containing data to be shared with the selected applications.This intent will be used to invoke application for sending the invite.
     */
    public abstract Intent getInviteIntent(String referralUrl, ArrayList<String> selectedContacts, String subject, String message);

    /**
     * Class for representing a contact item.
     */
    public class MyContact {
        /* Primary display name for the contact. */
        private String contactName = "";
        /* Primary contact info like email or phone number. */
        private String contactInfo = "";
        /* A unique ID for this contact. */
        private String contactID;
        /* Type of this contact. Email,Phone etc. */
        private int contactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        /* URI for the profile picture. */
        String photoURI_;

        public MyContact(String id, String name, String info, int type, String photoURI) {
            contactName = name;
            contactInfo = info;
            contactID = id;
            contactType = type;
            photoURI_ = photoURI;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public String getContactName() {
            return contactName;
        }

        public int getContactType() {
            return contactType;
        }

        public String getPhotoURI() {
            return photoURI_;

        }
    }

    class contactListItem extends LinearLayout {
        /* Text view for  contact primary display name */
        TextView displayNameTxt_;
        /* ImageView for contact profile pic */
        CircularImageView contactImg_;
        /* Image to highlight selected item */
        ImageView selectedImage_;
        LinearLayout coverLayout_;

        /**
         * Creates a view for the contact list item.
         *
         * @param context context for creating list view item.
         */
        public contactListItem(Context context) {
            super(context);
            coverLayout_ = new LinearLayout(context_);
            coverLayout_.setOrientation(HORIZONTAL);
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

            contactImg_ = new CircularImageView(context);
            contactImg_.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int contactPicSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(contactPicSize, contactPicSize);
            coverLayout_.addView(contactImg_, layoutParams);

            displayNameTxt_ = new TextView(context);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            displayNameTxt_.setPadding(padding * 3, padding, padding, padding);
            displayNameTxt_.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            coverLayout_.addView(displayNameTxt_, layoutParams);

            selectedImage_ = new ImageView(context);
            selectedImage_.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int selectedPicSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            layoutParams = new LinearLayout.LayoutParams(selectedPicSize, selectedPicSize);
            layoutParams.setMargins(0, 0, padding * 3, 0);
            coverLayout_.addView(selectedImage_, layoutParams);

            int alphabetIndexerSpacing = padding * 5;
            this.setPadding(0, 0, padding + alphabetIndexerSpacing, 0);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            coverLayout_.setPadding(0, padding, 0, padding);
            this.addView(coverLayout_, params);

        }

        public void updateView(MyContact contact) {
            SpannableString contactName = new SpannableString(contact.getContactName() + "\n" + contact.getContactInfo());

            contactName.setSpan(new RelativeSizeSpan(1.2f), 0, contact.getContactName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactName.setSpan(new ForegroundColorSpan(Color.BLACK), 0, contact.getContactName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            contactName.setSpan(new RelativeSizeSpan(0.8f), contact.getContactName().length(), contactName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactName.setSpan(new ForegroundColorSpan(Color.DKGRAY), contact.getContactName().length(), contactName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            displayNameTxt_.setText(contactName);
            displayNameTxt_.setTextColor(Color.BLACK);
            contactImg_.setBackgroundColor(Color.WHITE);
            if (contact.getPhotoURI() != null) {
                try {
                    contactImg_.setCircularBitmap(MediaStore.Images.Media.getBitmap(context_.getContentResolver(), Uri.parse(contact.getPhotoURI())));
                } catch (IOException ex) {
                    contactImg_.setCircularDrawable(defaultContactPic_);
                }
            } else {
                contactImg_.setCircularDrawable(defaultContactPic_);
            }

            if (selectedContactMap_.containsKey(contact.contactID)) {
                //this.setBackgroundColor(selectedItemColor_);
                coverLayout_.setBackgroundColor(selectedItemColor_);
                contactImg_.setBackgroundColor(selectedItemColor_);
                //displayNameTxt_.setCompoundDrawablesWithIntrinsicBounds(null, null, selectedIndicator_, null);
            } else {
                coverLayout_.setBackgroundColor(Color.TRANSPARENT);
                contactImg_.setBackgroundColor(Color.TRANSPARENT);
                //displayNameTxt_.setCompoundDrawablesWithIntrinsicBounds(null, null, nonSelectedIndicator_, null);
            }
        }
    }

    /**
     * Performs a binary search or cache lookup to find the first row that matches a given section's starting letter.
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        return mAlphabetIndexer.getPositionForSection(sectionIndex);
    }

    /**
     * Returns the section index for a given position in the list by querying the item and comparing it with all items
     * in the section array.
     */
    @Override
    public int getSectionForPosition(int position) {
        return mAlphabetIndexer.getSectionForPosition(position);
    }

    /**
     * Returns the section array constructed from the alphabet provided in the constructor.
     */
    @Override
    public Object[] getSections() {
        return mAlphabetIndexer.getSections();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new contactListItem(context);
    }


    @Override
    public void onClick(View v) {
        MyContact selectedContact = ((MyContact) v.getTag());
        if (contactItemSelectedCallBack_ != null) {
            contactItemSelectedCallBack_.onContactSelected(selectedContact);
        }
        if (selectedContactMap_.containsKey(selectedContact.contactID)) {
            selectedContactMap_.remove(selectedContact.contactID);
        } else {
            if(isSingleSelect_){
                selectedContactMap_.clear();
            }
            selectedContactMap_.put(selectedContact.contactID, selectedContact);
        }
        //Refresh the selected items.
        notifyDataSetChanged();


    }


}
