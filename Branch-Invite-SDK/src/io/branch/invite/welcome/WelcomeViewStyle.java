package io.branch.invite.welcome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import io.branch.invite.util.Defines;

/**
 * Builder class providing custom style for the welcome View
 */
public class WelcomeViewStyle {
    private int inviteTextColor_ = Color.WHITE;
    private int welcomeTextColor_ = Color.BLUE;
    private String invitationMessageText_;
    private String welcomeMessageText_;
    private String proceedToAppText_;
    private Drawable defaultContactImg_;


    /**
     * <p>Create an instance of the builder to set Welcome view styling parameters.</p>
     *
     * @param context Context for showing the welcome dialog
     */
    public WelcomeViewStyle(Context context) {
        /*Setting default values for welcome dialog style */
        inviteTextColor_ = Color.WHITE;
        welcomeTextColor_ = Color.BLUE;
        String appLabel = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        invitationMessageText_ = Defines.FULL_NAME_SUB.getKey() + " has invited you to use " + appLabel;
        welcomeMessageText_ = "Welcome to " + appLabel + "! You've been invited to join " + appLabel + " by another user " + Defines.SHORT_NAME_SUB.getKey();
        proceedToAppText_ = "Press to join " + Defines.SHORT_NAME_SUB.getKey();
        defaultContactImg_ = new ColorDrawable(Color.parseColor("#FFEFEFEF"));
    }

    /**
     * <p>Set the color theme for the welcome Dialog</p>
     *
     * @param invitationTextColor Text color for the inviter info
     * @param welcomeTextColor    Text color for the welcome message
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public WelcomeViewStyle setColorTheme(int invitationTextColor, int welcomeTextColor) {
        inviteTextColor_ = invitationTextColor;
        welcomeTextColor_ = welcomeTextColor;
        return this;
    }

    /**
     * <p>Set an default image to be displayed for the inviter if there is no image url specified for the inviter</p>
     *
     * @param defaultImage Default image for the inviter
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public WelcomeViewStyle setDefaultUserImage(Drawable defaultImage) {
        defaultContactImg_ = defaultImage;
        return this;
    }

    /**
     * <p> Set the invitation message to be displayed.
     * Any occurrence of '$FULL_NAME' will be replaced by user full name provided with the link
     * Any occurrence of '$SHORT_NAME' will be replaced by user short name provided with the link
     * </p>
     *
     * @param invitationMessage A {@link String } for invitation message with place holders for names [$FULL_NAME,$SHORT_NAME]
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public WelcomeViewStyle setInvitationMessage(String invitationMessage) {
        invitationMessageText_ = invitationMessage;
        return this;
    }

    /**
     * <p> Set the welcome message to be displayed.
     * Any occurrence of '$FULL_NAME' will be replaced by user full name provided with the link
     * Any occurrence of '$SHORT_NAME' will be replaced by user short name provided with the link
     * </p>
     *
     * @param welcomeMessage A {@link String } for welcome message with place holders for names [$FULL_NAME,$SHORT_NAME]
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public WelcomeViewStyle setWelcomeMessage(String welcomeMessage) {
        welcomeMessageText_ = welcomeMessage;
        return this;
    }

    /**
     * <p> Set the text for proceed to app TextView.
     * Any occurrence of '$FULL_NAME' will be replaced by user full name provided with the link
     * Any occurrence of '$SHORT_NAME' will be replaced by user short name provided with the link
     * </p>
     *
     * @param proceedToAppMessage A {@link String } for displaying with the text field for closing the welcome dialog with place holders for names[$FULL_NAME,$SHORT_NAME]
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public WelcomeViewStyle setProceedToAppMessage(String proceedToAppMessage) {
        proceedToAppText_ = proceedToAppMessage;
        return this;
    }

    public Drawable getDefaultContactImg() {
        return defaultContactImg_;
    }

    public int getInviteTextColor() {
        return inviteTextColor_;
    }

    public int getWelcomeTextColor() {
        return welcomeTextColor_;
    }

    public String getWelcomeMessageText() {
        return welcomeMessageText_;
    }

    public String getInvitationMessageText() {
        return invitationMessageText_;
    }

    public String getProceedToAppText() {
        return proceedToAppText_;
    }

}
