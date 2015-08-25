package io.branch.invite.welcome;

import android.app.Dialog;
import android.content.Context;

import java.util.ArrayList;

import io.branch.invite.util.Defines;
import io.branch.referral.BranchError;

/**
 * <p>
 * Builder for creating a welcome dialog when user open the app by clinking an invitation link.
 * By default Welcome dialog is created inviters name and image(if available).Builder provide options for
 * customising the default welcome dialog. In addition to customising the default welcome dialog, a custom view can also specified.
 * <p/>
 * <p>This will create a welcome dialog in the current context. Please make sure your activity is not finishing after launching this dialog.
 * Consider not calling this from your splash activity but from your home activity</p>
 * <p/>
 * </p>
 */
public class WelcomeBuilder {
    private final Context context_;
    private WelcomeViewStyle invitationStyle_;
    private WelcomeCallback callback_;
    private final ArrayList<String> inviteLookUpKeys_;

    private String fullNameKey_;
    private String shortNameKey_;
    private String imageUrlKey_;

    /**
     * <p> Create a builder for welcome dialog</p>
     *
     * @param context Context for showing the welcome dialog
     */
    public WelcomeBuilder(Context context) {
        context_ = context;
        invitationStyle_ = new WelcomeViewStyle(context);
        callback_ = null;
        inviteLookUpKeys_ = new ArrayList<String>();

        fullNameKey_ = Defines.INVITE_USER_FULLNAME.getKey();
        shortNameKey_ = Defines.INVITE_USER_SHORT_NAME.getKey();
        imageUrlKey_ = Defines.INVITE_USER_IMAGE_URL.getKey();
    }

    /**
     * <p>Provides options for customising default welcome screen.
     * The styling attributes are specified by {@link WelcomeViewStyle} instance </p>
     *
     * @param invitationStyle Instance of {@link WelcomeViewStyle } with desired styling parameters.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public WelcomeBuilder setWelcomeViewStyle(WelcomeViewStyle invitationStyle) {
        invitationStyle_ = invitationStyle;
        return this;
    }

    /**
     * <p> Set an instance of {@link WelcomeCallback} for notifying the welcome UI events and errors.
     * Should set this callback to provide a custom view for the welcome dialog.
     * WelcomeBuilder#getCustomInvitationView should be implemented to return a custom view to set your own view to the welcome dialog</p>
     *
     * @param callback Instance of {@link WelcomeCallback}
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public WelcomeBuilder setWelcomeViewCallback(WelcomeCallback callback) {
        callback_ = callback;
        return this;
    }

    /**
     * <p> Set additional look up keys to check whether teh link click corresponds to an invitation.
     * Branch will check the for any match with the link parameters for the given key and launch welcome dialog on finding a match.
     * Note :- This is not needed if the invitation link is created  by using Branch invite SDK.</p>
     *
     * @param lookUpKey A {@link String} to look up the link prams to launch the welcome dialog.
     */
    public WelcomeBuilder addCustomLookupKeys(String lookUpKey) {
        inviteLookUpKeys_.add(lookUpKey);
        return this;
    }

    /**
     * <p> Set a custom key to get the user full name from the link params.
     * Note :- This is not needed if the invitation link is created by using Branch invite SDK.
     * </p>
     *
     * @param key custom key used for adding the user full name while creating the invitation link.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public WelcomeBuilder setCustomFullNameKey(String key) {
        fullNameKey_ = key;
        return this;
    }

    /**
     * <p> Set a custom key to get the user short name from the link params.
     * Note :- This is not needed if the invitation link is created by using Branch invite SDK.
     * </p>
     *
     * @param key custom key used for adding the user short name while creating the invitation link.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public WelcomeBuilder setCustomShortNameKey(String key) {
        shortNameKey_ = key;
        return this;
    }

    /**
     * <p> Set a custom key to get the user image url from the link params.
     * Note :- This is not needed if the invitation link is created by using Branch invite SDK.
     * </p>
     *
     * @param key custom key used for adding the user image url while creating the invitation link.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public WelcomeBuilder setCustomImageUrlKey(String key) {
        imageUrlKey_ = key;
        return this;
    }

    /**
     * <p>Creates an WelcomeDialog with the arguments supplied to this builder.
     * Please make sure your Branch session is initialised before calling this method. {@link WelcomeCallback#onBranchError(BranchError)} will be
     * called back with {@link io.branch.referral.BranchError} in case branch is not initialised. </p>
     * <p/>
     * <p>This will create a welcome dialog in the current context. Please make sure your activity is not finishing after launching this dialog.
     * Consider not calling this from your splash activity but from your home activity</p>
     *
     * @return {@link Dialog} instance for the welcome dialog if invitation params are available. Null if there is no invitation parameters available.
     */
    public Dialog create() {
        return WelcomeHandler.HandleInvitations(context_, invitationStyle_, callback_, inviteLookUpKeys_, fullNameKey_, shortNameKey_, imageUrlKey_);
    }


}
