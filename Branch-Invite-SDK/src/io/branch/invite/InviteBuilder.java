package io.branch.invite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by sojanpr on 8/11/15.
 */
public class InviteBuilder {
    final InviteManager inviteManager_;
    final Context context_;
    final InviteBuilderParams inviteBuilderParams_;

    public InviteBuilder(Context context){
        context_ = context;
        inviteManager_ = InviteManager.getInstance();
        inviteBuilderParams_ = new InviteBuilderParams(context);
    }

    public void show(){
        inviteManager_.inviteToApp(context_, inviteBuilderParams_);
    }

    public InviteBuilder setTabStyle(Drawable selectedTabDrawable, Drawable nonSelectedTabDrawable){
        inviteBuilderParams_.tabSelectedBackground_ = selectedTabDrawable;
        inviteBuilderParams_.tabUnselectedBackground_ = nonSelectedTabDrawable;
        return this;
    }
    public InviteBuilder setPositiveButtonStyle(Drawable btnBackground, String btnText, int textColor){
        inviteBuilderParams_.positiveBtnBackground = btnBackground;
        inviteBuilderParams_.positiveButtonText_ = btnText;
        inviteBuilderParams_.positiveBtnTextColor = textColor;
        return this;
    }

    public InviteBuilder setNegativeButtonStyle(Drawable btnBackground, String btnText, int textColor){
        inviteBuilderParams_.negativeBtnBackground = btnBackground;
        inviteBuilderParams_.negativeButtonText_ = btnText;
        inviteBuilderParams_.negativeBtnTextColor = textColor;
        return this;
    }

    public InviteBuilder setEmailTabText(String emailTabText){
        inviteBuilderParams_.emailTabText_ = emailTabText;
        return  this;
    }

    public InviteBuilder setPhoneTabText(String phoneTabText){
        inviteBuilderParams_.textTabText_ = phoneTabText;
        return  this;
    }
    public InviteBuilder enableSingleSelect(){
        inviteBuilderParams_.isSingleSelect_ = true;
        return  this;
    }
    public InviteBuilder setBackground(Drawable backgroundDrawable){
        inviteBuilderParams_.backgroundDrawable_ = backgroundDrawable;
        return  this;
    }

    public InviteBuilder setContactListItemStyle(Drawable selectedIcon, Drawable nonSelectedIcons){
        inviteBuilderParams_.selectedIndicator_ = selectedIcon;
        inviteBuilderParams_.nonSelectedIndicator_ = nonSelectedIcons;
        return  this;
    }

    public InviteBuilder setInvitation(String subject, String message){
        inviteBuilderParams_.invitationSubject_ = subject;
        inviteBuilderParams_.invitationMsg_ = message;
        return this;
    }




}
