package io.branch.invite.util;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Set;

import io.branch.invite.InviteContactListView;
import io.branch.invite.TabBuilderParams;
import io.branch.invite.TabbedInviteBuilder;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchReferralUrlBuilder;

/**
 * <p>Class for handling the tabbed invitation dialog. This class create and manages the invitation tabbed dialog.
 * Email / Text tabs are added by default to the tabbed view.</p>
 */
public class TabbedViewHandler implements DialogInterface.OnDismissListener {

    /* The custom chooser dialog for selecting an application to share the link */
    AnimatedDialog inviteDialog_;
    /* {@link Context} for the invite manager */
    Context context_;
    /* Static instance  for the invite manager */
    private static TabbedViewHandler thisInstance_;
    /* Builder parameters for the tabbed view.*/
    private TabBuilderParams inviteBuilderParams_;

    private InviteTabbedContentView inviteTabbedContentView_;

    /**
     * Singleton instance for this class
     */
    private TabbedViewHandler() {
        thisInstance_ = this;
    }

    /**
     * Get the singleton instance of {@link TabbedViewHandler}.
     *
     * @return {@link TabbedViewHandler} instance
     */
    public static TabbedViewHandler getInstance() {
        if (thisInstance_ == null) {
            thisInstance_ = new TabbedViewHandler();
        }
        return thisInstance_;
    }

    //-------------------- Public Methods ---------------------------//

    /**
     * Creates a new invitation dialog.
     *
     * @param context       Context for the dialog
     * @param builderParams {@link TabbedInviteBuilder} instance.
     */
    public Dialog createDialog(Context context, TabBuilderParams builderParams) {
        context_ = context;
        inviteBuilderParams_ = builderParams;
        createInviteDialog(builderParams);
        inviteDialog_.setOnDismissListener(this);
        return inviteDialog_;
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (inviteBuilderParams_.callback_ != null) {
            inviteBuilderParams_.callback_.onInviteDialogDismissed();
        }
        if(inviteTabbedContentView_ != null){
            inviteTabbedContentView_.onClose();
        }
    }

    //------------------------ Private methods -----------------------------//

    /**
     * Create and show an invitation dialog with the given options.
     */
    private void createInviteDialog(TabBuilderParams builderParams) {
        RelativeLayout tabbedViewCover = new RelativeLayout(context_);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        inviteTabbedContentView_ = new InviteTabbedContentView(context_, contactTabViewCallback_, builderParams);
        tabbedViewCover.addView(inviteTabbedContentView_, params);
        tabbedViewCover.setBackgroundColor(Color.WHITE);
        if (inviteDialog_ != null && inviteDialog_.isShowing()) {
            inviteDialog_.cancel();
        }
        inviteDialog_ = new AnimatedDialog(context_);
        inviteDialog_.setContentView(tabbedViewCover);
    }


    /**
     * Tab view events are handled here.
     */
    public InviteTabbedContentView.IContactTabViewEvents contactTabViewCallback_ = new InviteTabbedContentView.IContactTabViewEvents() {
        @Override
        public void onNegativeButtonClicked() {
            if (inviteDialog_ != null && inviteDialog_.isShowing()) {
                inviteDialog_.cancel();
            }
        }

        @Override
        public void onPositiveButtonClicked(ArrayList<String> selectedContactName, String selectedChannel, InviteContactListView listView) {
            createUrlAndInvite(selectedContactName, selectedChannel, listView);
        }

        @Override
        public void onContactSelected(ContactListAdapter.MyContact contact) {

        }
    };

    /**
     * Creates the referral url and send invitation to invitees.
     *
     * @param selectedContactNames An {@link ArrayList<String>} containing the list of contact selected.
     * @param selectedChannel      A {@link String} representing the channel name
     * @param listView             Instance of current tab content.Tab contents are always instance of {@link InviteContactListView}.
     */
    private void createUrlAndInvite(final ArrayList<String> selectedContactNames, final String selectedChannel, final InviteContactListView listView) {
        // Check if any contact selected.
        if (selectedContactNames.size() < 1) {
            if (inviteDialog_ != null && inviteDialog_.isShowing()) {
                inviteDialog_.cancel();
            }
        } else {
            BranchReferralUrlBuilder referralURIBuilder = new BranchReferralUrlBuilder(context_, selectedChannel);

            //Add Custom parameters to the builder first
            Set<String> customKeys = inviteBuilderParams_.customDataMap_.keySet();
            for (String customKey : customKeys) {
                String customVal = inviteBuilderParams_.customDataMap_.get(customKey);
                referralURIBuilder.addParameters(customKey, customVal);
            }
            //Add inviter info
            referralURIBuilder.addParameters(Defines.INVITE_USER_ID.getKey(), inviteBuilderParams_.userID_)
                    .addParameters(Defines.INVITE_USER_FULLNAME.getKey(), inviteBuilderParams_.userFullName_)
                    .addParameters(Defines.INVITE_USER_SHORT_NAME.getKey(), inviteBuilderParams_.userShortName_)
                    .addParameters(Defines.INVITE_USER_IMAGE_URL.getKey(), inviteBuilderParams_.userImageUrl_);

            referralURIBuilder.generateReferralUrl(new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError branchError) {
                    listView.onInvitationLinkCreated(url, branchError);
                    if (branchError != null) {
                        url = inviteBuilderParams_.defaultInvitationUrl_;
                    }
                    if (url != null && url.length() > 0) {
                        Intent invitationIntent = listView.getInviteIntent(url, selectedContactNames, inviteBuilderParams_.invitationSubject_, inviteBuilderParams_.invitationMsg_);
                        sendInvitation(invitationIntent, selectedContactNames, url);
                    }
                    if (inviteBuilderParams_.callback_ != null) {
                        inviteBuilderParams_.callback_.onInviteFinished(url, selectedChannel, selectedContactNames, branchError);
                    }

                    if (inviteDialog_ != null && inviteDialog_.isShowing()) {
                        inviteDialog_.cancel();
                    }
                }
            });
        }
    }

    /**
     * Invoke the applications targeted by the specific intent to share the invitation.
     *
     * @param invitationIntent    {@link Intent} to share the invitation.
     * @param selectedContactList An {@link ArrayList<String>} containing the list of contact selected.
     * @param inviteUrl           The invitation url created.
     */
    private void sendInvitation(Intent invitationIntent, ArrayList<String> selectedContactList, String inviteUrl) {
        try {
            context_.startActivity(invitationIntent);
        } catch (ActivityNotFoundException ex) {
            context_.startActivity(getDefaultIntent(selectedContactList, inviteUrl));
        }
    }

    /**
     * Creates a  fallback intent to show the sharing options
     *
     * @param selectedContacts List of selected contacts
     * @param inviteUrl        The invite url created
     * @return Intent with SEND Action to launch the sharing options
     */
    private Intent getDefaultIntent(ArrayList<String> selectedContacts, String inviteUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, inviteBuilderParams_.invitationSubject_);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, inviteBuilderParams_.invitationMsg_ + "\n" + inviteUrl);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, selectedContacts.toArray(new String[selectedContacts.size()]));
        intent.putExtra("address", BranchInviteUtil.formatListToCSV(selectedContacts));
        return intent;
    }

}
