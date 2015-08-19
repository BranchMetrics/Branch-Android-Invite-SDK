package io.branch.invite;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by sojanpr on 8/19/15.
 */
public class WelcomeBuilder {
    private final Context context_;
    private WelcomeViewStyle invitationStyle_;
    private WelcomeCallback callback_;

    public WelcomeBuilder(Context context){
        context_ = context;
        invitationStyle_ = new WelcomeViewStyle(context);
        callback_ = null;
    }

    public WelcomeBuilder setInvitationStyle(WelcomeViewStyle invitationStyle){
        invitationStyle_ = invitationStyle;
        return this;
    }

    public WelcomeBuilder setInvitationUICallback(WelcomeCallback callback){
        callback_ = callback;
        return this;
    }

    public Dialog show(){
        return WelcomeHandler.HandleInvitations(context_, invitationStyle_, callback_);
    }


}
