package io.branch.invite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import io.branch.invite.util.TabbedViewHandler;

/**
 * Builder for create a Tabbed invite dialog. This dialog has pre-populated contact list for
 * each channel. Each channel is added a as a tab on the dialog. There are customisable positive and negative buttons
 * for capturing user actions. New tabs with custom contact list can be added to the tabbed view.
 * This class make use of {@link InviteCallback } to notify the invitation status.
 * If you want a start with a list of applications rather than a pre-populated contact list then consider using {@link SimpleInviteBuilder}
 * {@see addCustomTab() method to see how to add a custom tab to the tabbed view}
 */
public class TabbedInviteBuilder {
    final TabbedViewHandler inviteManager_;
    final Context context_;
    final TabBuilderParams inviteBuilderParams_;

    /**
     * <p>Create a builder for Tabbed invite dialog. This dialog has pre-populated contact list for
     * each channel. </p>
     *
     * @param context      Context for the invite tabbed dialog.
     * @param userID       A {@link String} with value of user-id for the inviter.
     * @param userFullName A {@link String} with value of users full name.
     */
    public TabbedInviteBuilder(Context context, String userID, String userFullName) {
        context_ = context;
        inviteManager_ = TabbedViewHandler.getInstance();
        inviteBuilderParams_ = new TabBuilderParams(context);
        inviteBuilderParams_.userID_ = userID;
        inviteBuilderParams_.userFullName_ = userFullName;
    }

    /**
     * <p>Set the selected and non selected drawables for the tab. This will be shown as the tab background depending on the selected state.</p>
     *
     * @param selectedTabDrawable    Drawable background for a selected tab.
     * @param nonSelectedTabDrawable Drawable background for a non selected tab.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setTabStyle(Drawable selectedTabDrawable, Drawable nonSelectedTabDrawable) {
        inviteBuilderParams_.tabSelectedBackground_ = selectedTabDrawable;
        inviteBuilderParams_.tabUnselectedBackground_ = nonSelectedTabDrawable;
        return this;
    }

    /**
     * <p>Set the selected and non selected drawables for the tab. This will be shown as the tab background depending on the selected state.</p>
     *
     * @param selectedTabDrawableResId    Resource ID for drawable background for a selected tab.
     * @param nonSelectedTabDrawableResId Resource ID for drawable background for a non selected tab.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setTabStyle(int selectedTabDrawableResId, int nonSelectedTabDrawableResId) {
        inviteBuilderParams_.tabSelectedBackground_ = context_.getResources().getDrawable(selectedTabDrawableResId);
        inviteBuilderParams_.tabUnselectedBackground_ = context_.getResources().getDrawable(nonSelectedTabDrawableResId);
        return this;
    }

    /**
     * <p>Set the positive action button (Done button by default) background and text.</p>
     *
     * @param btnBackground Drawable background for the positive action button.
     * @param btnText       Text for the positive action button.
     * @param textColor     An  {@link Integer} representing the ARGB color value for positive button text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setPositiveButtonStyle(Drawable btnBackground, String btnText, int textColor) {
        inviteBuilderParams_.positiveBtnBackground = btnBackground;
        inviteBuilderParams_.positiveButtonText_ = btnText;
        inviteBuilderParams_.positiveBtnTextColor = textColor;
        return this;
    }

    /**
     * <p>Set the positive action button (Done button by default) background and text.</p>
     *
     * @param btnBackgroundResId Resource ID for drawable background for the positive action button.
     * @param btnTextResId       Resource ID for positive action button text.
     * @param textColorResId     Resource Id for positive button text color.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setPositiveButtonStyle(int btnBackgroundResId, int btnTextResId, int textColorResId) {
        inviteBuilderParams_.positiveBtnBackground = context_.getResources().getDrawable(btnBackgroundResId);
        inviteBuilderParams_.positiveButtonText_ = context_.getResources().getString(btnTextResId);
        inviteBuilderParams_.positiveBtnTextColor = context_.getResources().getColor(textColorResId);
        return this;
    }

    /**
     * <p>Set the negative action button (Cancel button by default) background and text.</p>
     *
     * @param btnBackground Drawable background for the negative action button.
     * @param btnText       Text for the negative action button.
     * @param textColor     An  {@link Integer} representing the ARGB color value for negative button text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setNegativeButtonStyle(Drawable btnBackground, String btnText, int textColor) {
        inviteBuilderParams_.negativeBtnBackground = btnBackground;
        inviteBuilderParams_.negativeButtonText_ = btnText;
        inviteBuilderParams_.negativeBtnTextColor = textColor;
        return this;
    }

    /**
     * <p>Set the negative action button (Cancel button by default) background and text.</p>
     *
     * @param btnBackgroundResId Resource ID for the drawable background for the negative action button.
     * @param btnTextResId       Resource id for negative action button text.
     * @param textColorResId     Resource ID for negative button text color.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setNegativeButtonStyle(int btnBackgroundResId, int btnTextResId, int textColorResId) {
        inviteBuilderParams_.negativeBtnBackground = context_.getResources().getDrawable(btnBackgroundResId);
        inviteBuilderParams_.negativeButtonText_ = context_.getResources().getString(btnTextResId);
        inviteBuilderParams_.negativeBtnTextColor = context_.getResources().getColor(textColorResId);
        return this;
    }

    /**
     * <p>Set a custom text for Email contact tab. Default text will be  "Email"</p>
     *
     * @param emailTabText A {@link String} value for Email tab text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setEmailTabText(String emailTabText) {
        inviteBuilderParams_.emailTabText_ = emailTabText;
        return this;
    }

    /**
     * <p>Set a custom text for Email contact tab. Default text will be  "Email"</p>
     *
     * @param emailTabTextResId Resource id for Email tab text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setEmailTabText(int emailTabTextResId) {
        inviteBuilderParams_.emailTabText_ = context_.getResources().getString(emailTabTextResId);
        return this;
    }

    /**
     * <p>Set a custom text for Phone contact tab. Default text will be  "Text"</p>
     *
     * @param phoneTabText A {@link String} value for Phone tab text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setPhoneTabText(String phoneTabText) {
        inviteBuilderParams_.textTabText_ = phoneTabText;
        return this;
    }

    /**
     * <p>Set a custom text for Phone contact tab. Default text will be  "Text"</p>
     *
     * @param phoneTabTextResId Resource ID for phone tab text.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setPhoneTabText(int phoneTabTextResId) {
        inviteBuilderParams_.textTabText_ = context_.getResources().getString(phoneTabTextResId);
        return this;
    }

    /**
     * <p>Specifies whether contact selection model is  single select or multi select.</p>
     * Calling this method will set the contact selection to single select.
     *
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder enableSingleSelect() {
        inviteBuilderParams_.isSingleSelect_ = true;
        return this;
    }

    /**
     * <p>Set the background drawable for the tabbed contact view.</p>
     *
     * @param backgroundDrawable Drawable background for the tabbed contact view
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setBackground(Drawable backgroundDrawable) {
        inviteBuilderParams_.backgroundDrawable_ = backgroundDrawable;
        return this;
    }

    /**
     * <p>Set the background drawable for the tabbed contact view.</p>
     *
     * @param backgroundDrawableResId Resource id for the drawable background for the tabbed contact view
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setBackground(int backgroundDrawableResId) {
        inviteBuilderParams_.backgroundDrawable_ = context_.getResources().getDrawable(backgroundDrawableResId);
        return this;
    }

    /**
     * <p>Set highlighting color for the selected contacts in the contact list.</p>
     *
     * @param selectedItemColor Highlight color for the selected item in the contact list. A single color value in the form 0xAARRGGBB.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setSelectedItemColor(int selectedItemColor) {
        inviteBuilderParams_.selectedItemBackGroundColor_ = selectedItemColor;
        return this;
    }

    /**
     * <p>Set highlighting color for the selected contacts in the contact list.</p>
     *
     * @param selectedItemColorResID Color resource id for highlighting  selected item in the contact list.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setSelectedItemColorResourceId(int selectedItemColorResID) {
        inviteBuilderParams_.selectedItemBackGroundColor_ = context_.getResources().getColor(selectedItemColorResID);
        return this;
    }

    /**
     * <p>Set body and title to for the invitation message.</p>
     *
     * @param subject Title or subject for the invitation message
     * @param message Invitation message body
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setInvitationText(String subject, String message) {
        inviteBuilderParams_.invitationSubject_ = subject;
        inviteBuilderParams_.invitationMsg_ = message;
        return this;
    }

    /**
     * <p>Set body and title to for the invitation message.</p>
     *
     * @param subjectResId String resource id for Title or subject for the invitation message
     * @param messageResId String resource id for Invitation message body
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setInvitationText(int subjectResId, int messageResId) {
        inviteBuilderParams_.invitationSubject_ = context_.getResources().getString(subjectResId);
        inviteBuilderParams_.invitationMsg_ = context_.getResources().getString(messageResId);
        return this;
    }

    /**
     * Set as custom display name for the inviter.
     *
     * @param inviterShortName A {@link String} with value of custom display name
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setInviterShortName(String inviterShortName) {
        inviteBuilderParams_.userShortName_ = inviterShortName;
        return this;
    }

    /**
     * Set the any image url associated with the inviter.
     *
     * @param imageUrl A url for the user image.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setInviterImageUrl(String imageUrl) {
        inviteBuilderParams_.userImageUrl_ = imageUrl;
        return this;
    }

    /**
     * Add any custom parameters to the invitation.
     *
     * @param key   A {@link String} value with the key for the custom param.
     * @param value A {@link String} value with the value for the custom param.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder addCustomParams(String key, String value) {
        inviteBuilderParams_.customDataMap_.put(key, value);
        return this;
    }

    /**
     * Sets a callback to get notified on invitation status.
     *
     * @param callback an instance of {@link InviteCallback } to notify the invite process status.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setInvitationStatusCallback(InviteCallback callback) {
        inviteBuilderParams_.callback_ = callback;
        return this;
    }

    /**
     * Adds a custom tab to the list of contact tabs. All custom tabs should provide a {@link InviteContactListView} instance
     * which is set as the content view of the tab. The content view should handle all the contact selection and and sharing functions.
     *
     * @param channelName     Name of this custom contact list channel.
     * @param contactListView A {@link InviteContactListView} instance which will be set as the content view for the tab.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder addCustomTab(String channelName, InviteContactListView contactListView) {
        inviteBuilderParams_.customTabMap_.put(channelName, contactListView);
        return this;
    }

    /**
     * Adds a custom tab to the list of contact tabs. All custom tabs should provide a {@link InviteContactListView} instance
     * which is set as the content view of the tab. The content view should handle all the contact selection and and sharing functions.
     *
     * @param channelNameResID String resource identifier for channel name.
     * @param contactListView  A {@link InviteContactListView} instance which will be set as the content view for the tab.
     * @return This Builder object to allow for chaining of calls to set methods
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder addCustomTab(int channelNameResID, InviteContactListView contactListView) {
        inviteBuilderParams_.customTabMap_.put(context_.getResources().getString(channelNameResID), contactListView);
        return this;
    }

    /**
     * Set the fallback back url to send to the invitees in case Branch Invite URL generation fails.
     *
     * @param defaultUrl Fallback url to share.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setDefaultUrl(String defaultUrl) {
        inviteBuilderParams_.defaultInvitationUrl_ = defaultUrl;
        return this;
    }

    /**
     * Set a message to be displayed when there is no contact available. Default message is "No Contacts available".
     *
     * @param noContactMsg Message to be displayed when there is no contact available to list.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setNoContactAvailableMessage(String noContactMsg) {
        inviteBuilderParams_.noContactAvailableMsg_ = noContactMsg;
        return this;
    }

    /**
     * Set a message to be displayed when there is no contact available. Default message is "No Contacts available".
     *
     * @param noContactMsgResId A String resource id for message to be displayed when there is no contact available to list.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setNoContactAvailableMessage(int noContactMsgResId) {
        inviteBuilderParams_.noContactAvailableMsg_ = context_.getResources().getString(noContactMsgResId);
        return this;
    }

    /**
     * Sets a title for the invite dialog with the given Text View.
     *
     * @param titleView Text view for the title of the invite dialog.
     * @return This Builder object to allow for chaining of calls to set methods.
     */
    @SuppressWarnings("unused")
    public TabbedInviteBuilder setTitle(TextView titleView) {
        inviteBuilderParams_.titleTxtVew_ = titleView;
        return this;
    }


    /**
     * Creates the invitation dialog with the arguments supplied in the builder.
     */
    public Dialog create() {
        return inviteManager_.createDialog(context_, inviteBuilderParams_);
    }
}
