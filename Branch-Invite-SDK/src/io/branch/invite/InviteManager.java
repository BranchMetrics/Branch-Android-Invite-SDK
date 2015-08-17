package io.branch.invite;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;
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
 * <p>
 * Main application class for Branch invite sdk. Create and Manages the views for inviting contacts.
 * Class handles actions when user click on an action item.
 * </p>
 */
class InviteManager {

    /* The custom chooser dialog for selecting an application to share the link */
    Dialog inviteDialog_;
    /* {@link Context} for the invite manager */
    Context context_;
    /* Static instance  for the invite manager */
    private static InviteManager thisInstance_;
    private InviteTabbedBuilderParams inviteBuilderParams_;

    private InviteManager(){
        thisInstance_ = this;
    }

    /**
     * Get the singleton instance of {@link InviteManager}.
     * @return  {@link InviteManager} instance
     */
    public static InviteManager getInstance(){
        if(thisInstance_ == null){
            thisInstance_ = new InviteManager();
        }
        return thisInstance_;
    }

    public void inviteToApp(Context context ,InviteTabbedBuilderParams builderParams) {
        context_ = context;
        inviteBuilderParams_ = builderParams;
        createInviteDialog(builderParams);
    }

    /**
     * Create and show an invitation dialog with the given options.
     */
    private void createInviteDialog(InviteTabbedBuilderParams builderParams) {
        RelativeLayout tabbedViewCover = new RelativeLayout(context_);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabbedViewCover.addView(new InviteTabbedContentView(context_, contactTabViewCallback_, builderParams), params);
        tabbedViewCover.setBackgroundColor(Color.WHITE);
        inviteDialog_ = new Dialog(context_);
        setDialogWindowAttributes();
        inviteDialog_.setContentView(tabbedViewCover);
        
        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        slideUp.setDuration(500);
        slideUp.setInterpolator(new AccelerateInterpolator());
        ((ViewGroup) inviteDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideUp);

        inviteDialog_.show();
    }

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
        public void onPositiveButtonClicked(ArrayList<String> selectedContactName, InviteChannel selectedChannel, String targetPackage, InviteContactListView listView) {
            createUrlAndInvite(selectedContactName, selectedChannel, targetPackage, listView);
        }

        @Override
        public void onContactSelected(ContactListAdapter.MyContact contact) {

        }
    };

    private void createUrlAndInvite(final ArrayList<String> selectedContactNames, final InviteChannel selectedChannel, final String targetPackage, final InviteContactListView listView) {
        // Check if any contact selected.
        if (selectedChannel != InviteChannel.CUSTOM &&  selectedContactNames.size() < 1){
            animateDismiss();
        }
        else {
            BranchReferralUrlBuilder referralURIBuilder = new BranchReferralUrlBuilder(context_, selectedChannel.getName());

            //Add Custom parameters to the builder first
            Set<String> customKeys = inviteBuilderParams_.customDataMap_.keySet();
            for (String customKey : customKeys) {
                String customVal = inviteBuilderParams_.customDataMap_.get(customKey);
                referralURIBuilder.addParameters(customKey, customVal);
            }
            //Add inviter info
            referralURIBuilder.addParameters("BRANCH_INVITE_USER_ID_KEY", inviteBuilderParams_.userID_)
                    .addParameters("BRANCH_INVITE_USER_FULLNAME_KEY", inviteBuilderParams_.userFullName_)
                    .addParameters("BRANCH_INVITE_USER_SHORT_NAME_KEY", inviteBuilderParams_.userShortName_)
                    .addParameters("BRANCH_INVITE_USER_IMAGE_URL_KEY", inviteBuilderParams_.userImageUrl_);

            referralURIBuilder.generateReferralUrl(new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError branchError) {
                    //if selected channel is a custom channel notify the custom list view with the link. Custom list view can create the intent for sharing.
                    if (selectedChannel == InviteChannel.CUSTOM) {
                        if (listView != null) {
                            listView.onInvitationLinkCreated(url, branchError);
                        }
                    } else {
                        if (branchError == null) {
                            sendInvitation(selectedContactNames, selectedChannel, targetPackage, url, listView);
                            animateDismiss();
                        } else {
                            if (inviteBuilderParams_.defaultInvitationUrl_ != null) {
                                sendInvitation(selectedContactNames, selectedChannel, targetPackage, inviteBuilderParams_.defaultInvitationUrl_, listView);
                                animateDismiss();
                            } else {
                                if (inviteBuilderParams_.callback_ != null) {
                                    inviteBuilderParams_.callback_.onInviteFinished(url, selectedChannel.getName(), selectedContactNames, branchError);
                                }
                            }
                        }
                    }
                }
            });
        }


    }


    private void sendInvitation(ArrayList<String> selectedContactName, InviteChannel selectedChannel, String targetPackage ,String referralUrl, InviteContactListView listView){
        animateDismiss();
        //Handle a custom channel to share
        if (selectedChannel == InviteChannel.CUSTOM) {
            if(listView != null) {
                Intent customInviteIntent = listView.getSharingIntent();
                try {
                    context_.startActivity(customInviteIntent);
                } catch (ActivityNotFoundException ex) {
                    Log.i("BRANCH", "No activity found to complete the action.");
                }

            }
        } else {
            Intent inviteIntent = new Intent();

            String formattedContactList = "";
            for (String contactName : selectedContactName) {
                formattedContactList += contactName + ";";
            }
            if (formattedContactList.length() > 0) {
                formattedContactList = formattedContactList.substring(0, formattedContactList.length() - 1);
            }


            inviteIntent = getBaseInviteIntent(selectedContactName, formattedContactList, referralUrl);
            if (isPackageInstalled(selectedChannel.getTargetType())) {
                inviteIntent.setPackage(selectedChannel.getTargetType());
            }

            if (selectedChannel == InviteChannel.MESSAGE) {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    inviteIntent = new Intent(Intent.ACTION_SENDTO);
                    inviteIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    inviteIntent.setType(selectedChannel.getTargetType());
                    inviteIntent.setData(Uri.parse("sms:" + Uri.encode(formattedContactList)));
                    inviteIntent.putExtra("sms_body", inviteBuilderParams_.invitationMsg_ + "\n" + referralUrl);
                } else {
                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context_);
                    inviteIntent.setPackage(defaultSmsPackageName);
                }
            }
            try {
                context_.startActivity(inviteIntent);
            } catch (ActivityNotFoundException ex) {
                context_.startActivity(getBaseInviteIntent(selectedContactName, formattedContactList, referralUrl));
            }
        }

        if(inviteBuilderParams_.callback_  != null){
            inviteBuilderParams_.callback_.onInviteFinished(referralUrl, selectedChannel.getName(),selectedContactName,null);
        }
    }

    private Intent getBaseInviteIntent(ArrayList<String> selectedContactName ,String formattedContactList, String inviteUrl){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, inviteBuilderParams_.invitationSubject_);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, inviteBuilderParams_.invitationMsg_ + "\n\n" + inviteUrl);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, selectedContactName.toArray(new String[0]));
        intent.putExtra("address", formattedContactList);
        return intent;
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = context_.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void cancelInviteDialog(){
        animateDismiss();
    }

}
