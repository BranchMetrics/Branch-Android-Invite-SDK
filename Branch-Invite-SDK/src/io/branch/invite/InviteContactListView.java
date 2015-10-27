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
 * <p>Abstract class for invite contact list view. This class provides the view for tab when using a {@link TabbedInviteBuilder}.
 * All custom tabs added to the {@link TabbedInviteBuilder} should provide an implementation of this class. </p>
 */
public abstract class InviteContactListView extends ListView {
    public InviteContactListView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public InviteContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    public InviteContactListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InviteContactListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * <p>Name of the channel this list represents.</p>
     * This method provide option to to name the channel when you can create your custom invitee list to add to {@link TabbedInviteBuilder}.
     *
     * @return A {@link String } representing the channel name
     */
    public abstract String getInviteChannelName();

    /**
     * <p>Gets a list of contacts selected for inviting. This method is called when user press positive button to start sending invitation.</p>
     *
     * @return an {@link ArrayList<String>} of selected contacts
     */
    public abstract ArrayList<String> getSelectedContacts();

    /**
     * <p>Callback method to notify when an invitation link created.</p>
     *
     * @param invitationUrl The invitation URl created.
     * @param error         A {@link BranchError} instance if there is any error. Value will be null if there is no error.
     */
    public abstract void onInvitationLinkCreated(String invitationUrl, BranchError error);

    /**
     * <p>Get an intent to invoke the applications to send the invitation. List views added to the {@link TabbedInviteBuilder}
     * should provide an intent to invoke the sharing client applications.
     * </p>
     *
     * @param referralUrl   The invite url created
     * @param selectedUsers An {@link ArrayList<String>} of selected users.
     * @param subject       A {@link String} containing the subject for sharing invite link
     * @param message       A {@link String} containing the message for sharing invite link
     * @return An {@link Intent} targeted to share the link created with other application.
     */
    public abstract Intent getInviteIntent(String referralUrl, ArrayList<String> selectedUsers, String subject, String message);

}

