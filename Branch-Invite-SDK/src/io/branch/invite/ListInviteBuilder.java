import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import io.branch.invite.BranchInviteStatusListener;
import io.branch.invite.TabbedInviteBuilder;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;


public  class ListInviteBuilder implements Branch.BranchLinkShareListener {
    /* Current actvity to show the dialog */
    final Activity activity_;
    /* Json parameters for link creation */
    final JSONObject params_;
    /* Message to send to the invitee */
    public String invitationMsg_;
    /* Subject of invitation */
    public String invitationSubject_;
    /* Default url specified for invitation*/
    public String defaultInvitationUrl_;

    /* User ID for the inviting person */
    public String userID_;
    /* Name of the inviting person */
    public String userFullName_;
    /* Url to the inviting users profile picture */
    public String userImageUrl_;
    /* Inviting persons last name */
    public String userShortName_;
    /* Callback to notify the invite process status */
    public BranchInviteStatusListener callback_;

    public ListInviteBuilder(Activity activity, String userID, String userFullName){
        activity_ = activity;
        params_ = new JSONObject();
        userID_ = userID;
        userFullName_ = userFullName;

        String appLabel = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
        invitationSubject_ = "Check out "+ appLabel+ "!";
        invitationMsg_ = "Check out this cool app named "+appLabel;
        defaultInvitationUrl_ = "https://play.google.com/store/apps/details?id="+ activity.getPackageName();
        userImageUrl_ = "";
        userShortName_ = "";
    }


    public ListInviteBuilder setInviterShortName(String inviterShortName){
        userShortName_ = inviterShortName;
        return this;
    }

    public ListInviteBuilder setInviterImageUrl(String imageUrl){
        userImageUrl_ = imageUrl;
        return this;
    }
    public ListInviteBuilder addCustomParams(String key ,String value){
        try {
            params_.put(key, value);
        }catch (JSONException ignore){

        }
        return this;
    }
    public ListInviteBuilder setInvitationStatusCallback(BranchInviteStatusListener callback){
        callback_ = callback;
        return this;
    }
    public ListInviteBuilder setInvitation(String subject, String message){
        invitationSubject_ = subject;
        invitationMsg_ = message;
        return this;
    }

    public void showInviteDialog(){
        try {
            params_.put("BRANCH_INVITE_USER_ID_KEY",userID_);
            params_.put("BRANCH_INVITE_USER_FULLNAME_KEY", userFullName_);
            params_.put("BRANCH_INVITE_USER_SHORT_NAME_KEY", userShortName_);
            params_.put("BRANCH_INVITE_USER_IMAGE_URL_KEY", userImageUrl_);

        } catch (JSONException ignore){

        }

         new Branch.ShareLinkBuilder(activity_,params_)
                 .setDefaultURL(defaultInvitationUrl_)
                 .setMessage(invitationMsg_)
                 .setSubject(invitationSubject_)
                 .setFeature(Branch.FEATURE_TAG_REFERRAL)
                 .setCallback(this)

                 .shareLink();
    }


    @Override
    public void onLinkShareResponse(String referralLink, String channel, BranchError branchError) {
        if(callback_ != null){
            callback_.onInviteFinished(referralLink, channel, null, branchError);
        }
    }

    @Override
    public void onChannelSelected(String s) {

    }
}