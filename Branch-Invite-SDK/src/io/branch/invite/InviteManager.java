package io.branch.invite;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Set;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchReferralUrlBuilder;

/**
 * <p>Class for handling the tabbed invitation dialog. This class create and manages the invitation tabbed dialog.
 * Email / Text tabs are added by default to the tabbed view.</p>
 */
class InviteManager implements DialogInterface.OnDismissListener {

    /* The custom chooser dialog for selecting an application to share the link */
    Dialog inviteDialog_;
    /* {@link Context} for the invite manager */
    Context context_;
    /* Static instance  for the invite manager */
    private static InviteManager thisInstance_;
    /* Builder parameters for the tabbed view.*/
    private InviteTabbedBuilderParams inviteBuilderParams_;

    private InviteManager() {
        thisInstance_ = this;
    }

    /**
     * Get the singleton instance of {@link InviteManager}.
     *
     * @return {@link InviteManager} instance
     */
    public static InviteManager getInstance() {
        if (thisInstance_ == null) {
            thisInstance_ = new InviteManager();
        }
        return thisInstance_;
    }

    /**
     * Create and opens a new invitation dialog.
     *
     * @param context       Context for the dialog
     * @param builderParams {@link TabbedInviteBuilder} instance.
     */
    public void showDialog(Context context, InviteTabbedBuilderParams builderParams) {
        context_ = context;
        inviteBuilderParams_ = builderParams;
        createInviteDialog(builderParams);
        inviteDialog_.setOnDismissListener(this);
    }

    /**
     * Create and show an invitation dialog with the given options.
     */
    private void createInviteDialog(InviteTabbedBuilderParams builderParams) {
        RelativeLayout tabbedViewCover = new RelativeLayout(context_);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabbedViewCover.addView(new InviteTabbedContentView(context_, contactTabViewCallback_, builderParams), params);
        tabbedViewCover.setBackgroundColor(Color.WHITE);
        if (inviteDialog_ != null && inviteDialog_.isShowing()) {
            inviteDialog_.dismiss();
        }
        inviteDialog_ = new Dialog(context_);
        setDialogWindowAttributes();
        inviteDialog_.setContentView(tabbedViewCover);
        
        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        slideUp.setDuration(500);
        slideUp.setInterpolator(new AccelerateInterpolator());
        ((ViewGroup) inviteDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideUp);

        inviteDialog_.show();
    }

    /**
     * Set the window attributes for the invite dialog.
     */
    private void setDialogWindowAttributes() {
        inviteDialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
        inviteDialog_.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inviteDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        inviteDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(inviteDialog_.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager wm = (WindowManager) context_.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.8f;
        inviteDialog_.getWindow().setAttributes(lp);
        inviteDialog_.getWindow().setWindowAnimations(android.R.anim.slide_in_left);
        inviteDialog_.setCanceledOnTouchOutside(true);
    }

    /**
     * Animation to be played on dismissing the dialog.
     */
    private void animateDismiss() {
        TranslateAnimation slideDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1f);
        slideDown.setDuration(500);
        slideDown.setInterpolator(new DecelerateInterpolator());
        ((ViewGroup) inviteDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideDown);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (inviteDialog_ != null) {
                    inviteDialog_.dismiss();
                    inviteDialog_ = null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Tab view events are handled here.
     */
    InviteTabbedContentView.IContactTabViewEvents contactTabViewCallback_ = new InviteTabbedContentView.IContactTabViewEvents() {
        @Override
        public void onNegativeButtonClicked() {
            animateDismiss();
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
            animateDismiss();
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
                    if(branchError != null){
                        url = inviteBuilderParams_.defaultInvitationUrl_;
                    }
                    if(url != null && url.length() > 0) {
                        Intent invitationIntent = listView.getInviteIntent(url, selectedContactNames, inviteBuilderParams_.invitationSubject_, inviteBuilderParams_.invitationMsg_);
                        sendInvitation(invitationIntent, selectedContactNames, url);
                    }
                    if (inviteBuilderParams_.callback_ != null) {
                        inviteBuilderParams_.callback_.onInviteFinished(url, selectedChannel, selectedContactNames, branchError);
                    }

                    animateDismiss();
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

    private Intent getDefaultIntent(ArrayList<String> selectedContactName, String inviteUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, inviteBuilderParams_.invitationSubject_);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, inviteBuilderParams_.invitationMsg_ + "\n" + inviteUrl);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, selectedContactName.toArray(new String[0]));
        intent.putExtra("address", BranchInviteUtil.formatListToCSV(selectedContactName));
        return intent;
    }


    public void cancelInviteDialog() {
        animateDismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (inviteBuilderParams_.callback_ != null) {
            inviteBuilderParams_.callback_.onInviteDialogDismissed();
        }
    }
}
