package io.branch.invite.welcome;

import android.view.View;

import org.json.JSONObject;

import io.branch.referral.BranchError;

/**
 * <p>Callback for getting notified for Welcome view events and errors. Should implemented if you wish to provide a custom view for Welcome Dialog</p>
 */
public interface WelcomeCallback {

    /**
     * <p>Returns a custom view for the welcome Dialog. This will override any other styles specified for teh welcome Dialog content.
     * </p>
     *
     * @param inviterID  UserID specified for the inviter in the referral link
     * @param inviterFullName Full Name specified for the inviter in the referral link
     * @param inviterShortName Optional short Name specified for the inviter in the referral link
     * @param inviterImageUrl Optional image url specified for the inviter in the referral link
     * @param customParameters A {JSONObject} containing the additional parameters specified for the link creation
     * @return A custom view for the welcome Dialog content view
     */
    View getCustomInvitationView(String inviterID, String inviterFullName, String inviterShortName, String inviterImageUrl, JSONObject customParameters);

    /**
     * <p>Callback method to notify welcome Dialog launch</p>
     */
    void onWelcomeDialogLaunched();

    /**
     * <p>Callback to notify welcome Dialog dismissed event</p>
     */
    void onWelcomeDialogDismissed();

    /**
     * <p>Notify any error while creating the welcome Dialog</p>
     * @param error A {@link BranchError} instance with the details of error occurred
     */
    void onBranchError(BranchError error);

}
