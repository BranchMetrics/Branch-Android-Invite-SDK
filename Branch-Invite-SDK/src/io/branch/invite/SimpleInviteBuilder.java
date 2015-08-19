package io.branch.invite;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;

/**
 * <p>
 * Builder for creating a Branch invitation dialog with preselected application.
 * This dialog will show a list of specified clients to send the invitation.
 * This class make use of {@link BranchInviteStatusListener } to notify the invitation status.
 * If you want a have pre-populated contact list for selected channel, then consider using {@link SimpleInviteBuilder}
 * </p>
 */
public class SimpleInviteBuilder implements Branch.BranchLinkShareListener {
    /* Current activity to show the dialog */
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
    /* Preferred option for sharing. */
    final private ArrayList<SharingHelper.SHARE_WITH> preferredOptions_;
    /* Branch share link instance to share teh link created. */
    final Branch.ShareLinkBuilder shareLinkBuilder_;

    /**
     * Creates a builder for an invitation dialog with list of applications to send the invitation with.
     *
     * @param activity     Current {@link Activity} instance.
     * @param userID       A {@link String} with value of user-id for the inviter.
     * @param userFullName A {@link String} with value of users full name.
     */
    public SimpleInviteBuilder(Activity activity, String userID, String userFullName) {
        activity_ = activity;
        params_ = new JSONObject();
        userID_ = userID;
        userFullName_ = userFullName;
        userImageUrl_ = "";
        userShortName_ = "";
        preferredOptions_ = new ArrayList<SharingHelper.SHARE_WITH>();

        // Set all default params first.
        String appLabel = activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
        shareLinkBuilder_ = new Branch.ShareLinkBuilder(activity_, params_);
        shareLinkBuilder_.setSubject("Check out " + appLabel + "!")
                .setMessage("Check out this cool app named " + appLabel)
                .setDefaultURL("https://play.google.com/store/apps/details?id=" + activity.getPackageName());

    }

    /**
     * Set as custom display name for the inviter.
     * @param inviterShortName A {@link String} with value of custom display name
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    public SimpleInviteBuilder setInviterShortName(String inviterShortName) {
        userShortName_ = inviterShortName;
        return this;
    }

    /**
     * Set the any image url associated with the inviter.
     *
     * @param imageUrl A url for the user image.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public SimpleInviteBuilder setInviterImageUrl(String imageUrl) {
        userImageUrl_ = imageUrl;
        return this;
    }

    /**
     * Add any custom parameters to the invitation.
     *
     * @param key   A {@link String} value with the key for the custom param.
     * @param value A {@link String} value with the value for the custom param.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public SimpleInviteBuilder addCustomParams(String key, String value) {
        try {
            params_.put(key, value);
        } catch (JSONException ignore) {

        }
        return this;
    }

    /**
     * Sets a callback to get notified on invitation status.
     *
     * @param callback an instance of {@link BranchInviteStatusListener } to notify the invite process status.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public SimpleInviteBuilder setInvitationStatusCallback(BranchInviteStatusListener callback) {
        callback_ = callback;
        return this;
    }

    /**
     * Set the title and body for the invitation to send.
     *
     * @param subject {@link String} with value for the invite message title.
     * @param message {@link String} with value for the invite message body.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public SimpleInviteBuilder setInvitation(String subject, String message) {
        shareLinkBuilder_.setSubject(subject);
        shareLinkBuilder_.setMessage(subject);
        return this;
    }

    /**
     * <p>Adds application to the preferred list of applications which are shown on invitation dialog.
     * Only these options will be visible when the invitation dialog launches. Other options can be
     * accessed by clicking "More"</p>
     *
     * @param preferredOption A list of applications to be added as preferred options on the app chooser.
     *                        Preferred applications are defined in {@link io.branch.referral.SharingHelper.SHARE_WITH}.
     * @return A {@link io.branch.referral.Branch.ShareLinkBuilder} instance.
     */
    public SimpleInviteBuilder addPreferredInviteChannel(SharingHelper.SHARE_WITH preferredOption) {
        shareLinkBuilder_.addPreferredSharingOption(preferredOption);
        return this;
    }

    /**
     * Set the fallback back url to send to the invitees in case Branch Invite URL generation fails.
     *
     * @param defaultUrl Fallback url to share.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    public SimpleInviteBuilder setDefaultUrl(String defaultUrl) {
        shareLinkBuilder_.setDefaultURL(defaultUrl);
        return this;
    }


    public void showInviteDialog() {
        try {
            params_.put(Defines.INVITE_USER_ID.getKey(), userID_);
            params_.put(Defines.INVITE_USER_FULLNAME.getKey(), userFullName_);
            params_.put(Defines.INVITE_USER_SHORT_NAME.getKey(), userShortName_);
            params_.put(Defines.INVITE_USER_IMAGE_URL.getKey(), userImageUrl_);

        } catch (JSONException ignore) {

        }

        shareLinkBuilder_.setFeature(Branch.FEATURE_TAG_REFERRAL)
                .setCallback(this)
                .shareLink();
    }

    /**
     * Cancel the current active invitation dialog
     */
    public void cancelInviteDialog() {
        Branch.getInstance().cancelShareLinkDialog();
    }


    //---- Link share events---------------------------//

    @Override
    public void onLinkShareResponse(String referralLink, String channel, BranchError branchError) {
        if (callback_ != null) {
            callback_.onInviteFinished(referralLink, channel, null, branchError);
        }
    }

    @Override
    public void onChannelSelected(String channelName) {
        if (callback_ != null) {
            callback_.onInviteChannelSelected(channelName);
        }
    }
}