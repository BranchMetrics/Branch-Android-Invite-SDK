package io.branch.invite;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import io.branch.referral.BranchError;

/**
 * Created by sojanpr on 8/11/15.
 */
public class TabbedInviteBuilder {
    final InviteManager inviteManager_;
    final Context context_;
    final InviteTabbedBuilderParams inviteBuilderParams_;

    public TabbedInviteBuilder(Context context, String userID, String userFullName){
        context_ = context;
        inviteManager_ = InviteManager.getInstance();
        inviteBuilderParams_ = new InviteTabbedBuilderParams(context);
        inviteBuilderParams_.userID_ = userID;
        inviteBuilderParams_.userFullName_ = userFullName;
    }

    public TabbedInviteBuilder setTabStyle(Drawable selectedTabDrawable, Drawable nonSelectedTabDrawable){
        inviteBuilderParams_.tabSelectedBackground_ = selectedTabDrawable;
        inviteBuilderParams_.tabUnselectedBackground_ = nonSelectedTabDrawable;
        return this;
    }
    public TabbedInviteBuilder setPositiveButtonStyle(Drawable btnBackground, String btnText, int textColor){
        inviteBuilderParams_.positiveBtnBackground = btnBackground;
        inviteBuilderParams_.positiveButtonText_ = btnText;
        inviteBuilderParams_.positiveBtnTextColor = textColor;
        return this;
    }

    public TabbedInviteBuilder setNegativeButtonStyle(Drawable btnBackground, String btnText, int textColor){
        inviteBuilderParams_.negativeBtnBackground = btnBackground;
        inviteBuilderParams_.negativeButtonText_ = btnText;
        inviteBuilderParams_.negativeBtnTextColor = textColor;
        return this;
    }

    public TabbedInviteBuilder setEmailTabText(String emailTabText){
        inviteBuilderParams_.emailTabText_ = emailTabText;
        return  this;
    }

    public TabbedInviteBuilder setPhoneTabText(String phoneTabText){
        inviteBuilderParams_.textTabText_ = phoneTabText;
        return  this;
    }
    public TabbedInviteBuilder enableSingleSelect(){
        inviteBuilderParams_.isSingleSelect_ = true;
        return  this;
    }
    public TabbedInviteBuilder setBackground(Drawable backgroundDrawable){
        inviteBuilderParams_.backgroundDrawable_ = backgroundDrawable;
        return  this;
    }

    public TabbedInviteBuilder setContactListItemStyle(Drawable selectedIcon, Drawable nonSelectedIcons){
        inviteBuilderParams_.selectedIndicator_ = selectedIcon;
        inviteBuilderParams_.nonSelectedIndicator_ = nonSelectedIcons;
        return  this;
    }

    public TabbedInviteBuilder setInvitation(String subject, String message){
        inviteBuilderParams_.invitationSubject_ = subject;
        inviteBuilderParams_.invitationMsg_ = message;
        return this;
    }

    public TabbedInviteBuilder setInviterShortName(String inviterShortName){
        inviteBuilderParams_.userShortName_ = inviterShortName;
        return this;
    }

    public TabbedInviteBuilder setInviterImageUrl(String imageUrl){
        inviteBuilderParams_.userImageUrl_ = imageUrl;
        return this;
    }
    public TabbedInviteBuilder addCustomParams(String key ,String value){
        inviteBuilderParams_.customDataMap_.put(key, value);
        return this;
    }
    public TabbedInviteBuilder setInvitationStatusCallback(BranchInviteStatusListener callback){
        inviteBuilderParams_.callback_ = callback;
        return this;
    }

    public TabbedInviteBuilder addCustomTab(String channelName, InviteContactListView contactListView){
        inviteBuilderParams_.customTabMap_.put(channelName,contactListView);
        return this;
    }

    public void showInviteDialog(){
        inviteManager_.inviteToApp(context_, inviteBuilderParams_);
    }

    public void cancelInviteDialog(){
        if(inviteManager_ != null){
            inviteManager_.cancelInviteDialog();
        }
    }

}
