package io.branch.invite;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.ArrayList;

import io.branch.referral.BranchError;

/**
 * Created by sojanpr on 8/13/15.
 */
public abstract class InviteContactListView extends ListView {
    public InviteContactListView(Context context) {
        super(context);
    }

    public InviteContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InviteContactListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InviteContactListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    public InviteChannel getInviteChannel(){
        return null;
    }
    public ArrayList<String> getSelectedContacts(){
        return new ArrayList<String>();
    }

    public abstract void onInvitationLinkCreated(String invitationUrl, BranchError error);
    public abstract Intent getSharingIntent();




}

