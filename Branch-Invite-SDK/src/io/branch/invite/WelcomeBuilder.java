package io.branch.invite;

import android.app.Dialog;
import android.content.Context;

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

    /**
     * <p> Create a builder for welcome dialog</p>
     *
     * @param context Context for showing the welcome dialog
     */
    public WelcomeBuilder(Context context) {
        context_ = context;
        invitationStyle_ = new WelcomeViewStyle(context);
        callback_ = null;
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
     * <p>Creates an WelcomeDialog with the arguments supplied to this builder and immediately displays the dialog.
     * Please make sure your Branch session is initialised before calling this method. {@link WelcomeCallback#onBranchError(BranchError)} will be
     * called back with {@link io.branch.referral.BranchError} in case branch is not initialised. </p>
     * <p/>
     * <p>This will create a welcome dialog in the current context. Please make sure your activity is not finishing after launching this dialog.
     * Consider not calling this from your splash activity but from your home activity</p>
     *
     * @return {@link Dialog} instance for the welcome dialog if invitation params are available. Null if there is no invitation parameters available.
     */
    public Dialog show() {
        return WelcomeHandler.HandleInvitations(context_, invitationStyle_, callback_);
    }


}
