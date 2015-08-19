package io.branch.invite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.AndroidCharacter;
import android.util.TypedValue;

import java.security.PublicKey;

/**
 * Created by sojanpr on 8/18/15.
 */
public class InvitationStyle {
    public int  inviteTextColor_ = Color.WHITE;
    public int  welcomeTextColor_ = Color.BLUE;
    public String invitationMessageText_;
    public String welcomeMessageText_;
    public String proceedToAppText_;
    public Drawable defaultContactImg_;

    public static final String FULL_NAME_SUB = "$FULL_NAME";
    public static final String SHORT_NAME_SUB = "$SHORT_NAME";

    public InvitationStyle(Context context){
        inviteTextColor_ = Color.WHITE;
        welcomeTextColor_ = Color.BLUE;
        String appLabel = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        invitationMessageText_ = FULL_NAME_SUB+" has invited you to use "+appLabel;
        welcomeMessageText_ = "Welcome to "+appLabel+ "! You've been invited to join "+appLabel+" by another user "+SHORT_NAME_SUB;
        proceedToAppText_ = "Press to join "+ SHORT_NAME_SUB ;
        defaultContactImg_  = context.getResources().getDrawable(android.R.drawable.gallery_thumb);
    }

    public InvitationStyle setColorTheme(int invitationTextColor, int welcomeTextColor){
        inviteTextColor_ = invitationTextColor;
        welcomeTextColor_ =  welcomeTextColor;
        return this;
    }

    public InvitationStyle setDefaultUserImage(Drawable userImage){
        defaultContactImg_ = userImage;
        return this;
    }

    public void setInvitationMessage(String invitationMessageText){
        invitationMessageText_ = invitationMessageText;
    }

    public void setWelcomeMessage(String welcomeMessageText){
        welcomeMessageText_ = welcomeMessageText;
    }

    public void proceedToAppMessage( String proceedToAppMessage){
        proceedToAppText_ = proceedToAppMessage;
    }

}
