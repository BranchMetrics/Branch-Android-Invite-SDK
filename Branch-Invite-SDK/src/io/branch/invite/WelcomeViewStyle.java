package io.branch.invite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by sojanpr on 8/18/15.
 */
public class WelcomeViewStyle {
    private int  inviteTextColor_ = Color.WHITE;
    private int  welcomeTextColor_ = Color.BLUE;
    private String invitationMessageText_;
    private String welcomeMessageText_;
    private String proceedToAppText_;
    private Drawable defaultContactImg_;


    public WelcomeViewStyle(Context context){
        inviteTextColor_ = Color.WHITE;
        welcomeTextColor_ = Color.BLUE;
        String appLabel = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        invitationMessageText_ = Defines.FULL_NAME_SUB.getKey()+" has invited you to use "+appLabel;
        welcomeMessageText_ = "Welcome to "+appLabel+ "! You've been invited to join "+appLabel+" by another user "+Defines.SHORT_NAME_SUB.getKey();
        proceedToAppText_ = "Press to join "+ Defines.SHORT_NAME_SUB.getKey() ;
        defaultContactImg_  = context.getResources().getDrawable(android.R.drawable.gallery_thumb);
    }

    public WelcomeViewStyle setColorTheme(int invitationTextColor, int welcomeTextColor){
        inviteTextColor_ = invitationTextColor;
        welcomeTextColor_ =  welcomeTextColor;
        return this;
    }

    public WelcomeViewStyle setDefaultUserImage(Drawable userImage){
        defaultContactImg_ = userImage;
        return this;
    }

    public WelcomeViewStyle setInvitationMessage(String invitationMessageText){
        invitationMessageText_ = invitationMessageText;
        return this;
    }

    public WelcomeViewStyle setWelcomeMessage(String welcomeMessageText){
        welcomeMessageText_ = welcomeMessageText;
        return this;
    }

    public WelcomeViewStyle setProceedToAppMessage( String proceedToAppMessage){
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
