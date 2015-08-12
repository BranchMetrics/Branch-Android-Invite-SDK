package io.branch.invite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Tabbed view to show the contact list items.
 * The view contains a tab view and its content which is a list of contact. Also add top action button
 * for positive and negative actions.
 */
class InviteContentView extends LinearLayout {
    /* Tab host for this tabbed view */
    TabHost host_;
    /* Context for creating the view */
    Context context_;
//    /* Selected background for the tab */
//    private Drawable tabSelectedBackground_;
//    /* Unselected background for the tab */
//    private Drawable tabUnselectedBackground_;
//    private int padding_;
//
//    /* Tab action bar positive button text */
//    String positiveButtonText_ = "Done";
//    /* Tab action bar negative button text */
//    String negativeButtonText_ = "Cancel";
//    /* Text color for negative button */
//    int negativeBtnTextColor = Color.BLUE;
//    /* Text color for positive button */
//    int positiveBtnTextColor = Color.BLUE;
//    /* Background drawable for positive button */
//    Drawable positiveBtnBackground = new ColorDrawable(Color.TRANSPARENT);
//    /* Drawable background for negative button */
//    Drawable negativeBtnBackground = new ColorDrawable(Color.TRANSPARENT);
//    /* Background drawable for the tab view.*/
//    Drawable backgroundDrawable_ = new ColorDrawable(Color.WHITE);
//
//    /* Name for email contact tab */
//    private String emailTabText = "Email";
//    /* Name for phone contact tab */
//    private String textTabText = "Text";

    private int padding_;

    /* Callback for tab events */
    IContactTabViewEvents contactTabViewEventsCallback_;
    InviteBuilderParams inviteBuilderParams_;

    /**
     * Creates a Invite content with action buttons and default tabs.
     *
     * @param context               A {@link Context} for the view
     * @param IContactTabViewEvents Instance of {@link io.branch.invite.InviteContentView.IContactTabViewEvents} to update invite view events
     */
    public InviteContentView(Context context, IContactTabViewEvents IContactTabViewEvents, InviteBuilderParams inviteBuilderParams) {
        super(context);
        context_ = context;
        setOrientation(VERTICAL);
        inviteBuilderParams_ = inviteBuilderParams;
        setBackgroundDrawable(this, inviteBuilderParams_.backgroundDrawable_);
        contactTabViewEventsCallback_ = IContactTabViewEvents;


        padding_ = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        initTabView();
    }

    /**
     * Initialise  the invite  view and setup the default tabs.
     */
    private void initTabView() {
        //Add action buttons
        LinearLayout controlCover = new LinearLayout(context_);
        controlCover.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        controlCover.setPadding(padding_ * 2, padding_ * 2, padding_ * 2, padding_ * 2);

        TextView negativeButton = new TextView(context_);
        negativeButton.setText(inviteBuilderParams_.negativeButtonText_);
        negativeButton.setBackgroundColor(Color.TRANSPARENT);
        negativeButton.setTextAppearance(context_, android.R.style.TextAppearance_Large);
        negativeButton.setTypeface(null, Typeface.BOLD);
        negativeButton.setGravity(Gravity.LEFT);
        negativeButton.setTextColor(inviteBuilderParams_.negativeBtnTextColor);
        setBackgroundDrawable(negativeButton, inviteBuilderParams_.negativeBtnBackground);

        negativeButton.setOnClickListener(negativeButtonClickListener_);


        TextView positiveButton = new TextView(context_);
        positiveButton.setText(inviteBuilderParams_.positiveButtonText_);
        positiveButton.setBackgroundColor(Color.TRANSPARENT);
        positiveButton.setTextAppearance(context_, android.R.style.TextAppearance_Large);
        positiveButton.setTypeface(null, Typeface.BOLD);
        positiveButton.setGravity(Gravity.RIGHT);
        positiveButton.setTextColor(inviteBuilderParams_.positiveBtnTextColor);
        setBackgroundDrawable(positiveButton, inviteBuilderParams_.positiveBtnBackground);

        positiveButton.setOnClickListener(positiveButtonClickListener_);

        controlCover.addView(negativeButton, params);
        controlCover.addView(positiveButton, params);
        this.addView(controlCover);


        //Add Tab view
        host_ = new TabHost(context_, null);
        host_.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        host_.setPadding(padding_, padding_, padding_, padding_);

        TabWidget widget = new TabWidget(context_);
        widget.setId(android.R.id.tabs);
        host_.addView(widget, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        widget.setBackgroundColor(Color.DKGRAY);

        host_.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                setTabBackGround();
            }
        });


        FrameLayout contentLayout = new FrameLayout(context_);
        contentLayout.setId(android.R.id.tabcontent);
        host_.addView(contentLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        host_.setup();

        addEmailTab();
        addTextTab();


        this.addView(host_);
        //Set content offset
        int contentOffset = host_.getTabWidget().getChildAt(host_.getTabWidget().getChildCount() - 1).getLayoutParams().height + padding_;
        contentLayout.setPadding(0, contentOffset, 0, 0);
        setTabBackGround();
    }

    /**
     * Adds the email contact list to the view. Query the Email Content URI for the available emails.
     */
    private void addEmailTab() {
        Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Email._ID,
                ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.PHOTO_THUMBNAIL_URI};

        Cursor queryCursor = context_.getContentResolver()
                .query(uri, projection, null, null, null);
        addTabForCursor(inviteBuilderParams_.emailTabText_, new EmailContactListAdapter(context_, queryCursor,
                                                            contactTabViewEventsCallback_,inviteBuilderParams_));
    }

    /**
     * Adds the phone contact list to the view. Query the Phone Content URI for the available phone numbers.
     */
    private void addTextTab() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI};
        Cursor queryCursor = context_.getContentResolver()
                .query(uri, projection, null, null, null);

        addTabForCursor(inviteBuilderParams_.textTabText_, new PhoneContactListAdapter(context_, queryCursor,
                                                            contactTabViewEventsCallback_,inviteBuilderParams_));
    }

    private void addTabForCursor(String tabName, final ListAdapter adapter) {
        TabHost.TabSpec textTab = host_.newTabSpec(tabName).setIndicator(tabName).setContent(new TabHost.TabContentFactory() {
            @SuppressLint("NewApi")
            @Override
            public View createTabContent(String tag) {
                ListView lv = new ListView(context_);
                lv.setAdapter(adapter);
                setBackgroundDrawable(lv, inviteBuilderParams_.backgroundDrawable_);
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    lv.setFastScrollAlwaysVisible(true);
                }
                lv.setFastScrollEnabled(true);
                return lv;
            }
        });
        host_.addTab(textTab);
    }


    private OnClickListener negativeButtonClickListener_ = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (contactTabViewEventsCallback_ != null) {
                contactTabViewEventsCallback_.onNegativeButtonClicked();
            }
        }
    };

    private OnClickListener positiveButtonClickListener_ = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (contactTabViewEventsCallback_ != null) {
                contactTabViewEventsCallback_.onPositiveButtonClicked();
            }
        }
    };

    public void setTabBackGround() {
        for (int i = 0; i < host_.getTabWidget().getChildCount(); i++) {
            //noinspection deprecation
            host_.getTabWidget().getChildAt(i).setBackgroundDrawable(inviteBuilderParams_.tabUnselectedBackground_); // Unselected Tabs
        }
        //noinspection deprecation Note:SetBackground is not working for tab widget
        host_.getTabWidget().getChildAt(host_.getCurrentTab()).setBackgroundDrawable(inviteBuilderParams_.tabSelectedBackground_);// selected tab
    }

    private void setBackgroundDrawable(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(inviteBuilderParams_.backgroundDrawable_);
        } else {
            view.setBackground(inviteBuilderParams_.backgroundDrawable_);
        }

    }

    interface IContactTabViewEvents {
        /* Called on user selecting the negative button */
        void onNegativeButtonClicked();

        /* Called on user selecting the positive button */
        void onPositiveButtonClicked();

        /* Called when user select a tab*/
        void onContactSelected(ContactListAdapter.MyContact contact);

    }

}
