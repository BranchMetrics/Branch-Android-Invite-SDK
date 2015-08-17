package io.branch.invite;

import java.util.ArrayList;

import io.branch.referral.BranchError;

/**
 * <p>An Interface class that is implemented by all classes that make use of
 * {@link TabbedInviteBuilder]}, defining methods to listen for invitation status.</p>
 */
public interface BranchInviteStatusListener {
    /**
     * <p> Callback method to update the sharing status. Called on sharing completed or on error.</p>
     *
     * @param inviteLink    The link created to invite.
     * @param inviteChannel Channel selected for inviting. Email/Message etc.
     * @param inviteeList   A list of contacts invited.
     * @param error         A {@link BranchError} to update errors, if there is any.
     */
    void onInviteFinished(String inviteLink, String inviteChannel, ArrayList<String> inviteeList, BranchError error);

    /**
     * <p>Called when user select a channel for inviting some one.
     * Branch will create a deep link for the selected channel and share with it after calling this
     * method. On sharing complete, status is updated by onInviteFinished() callback. Consider
     * having a sharing in progress UI if you wish to prevent user activity in the window between selecting a channel
     * and inviting complete.</p>
     *
     * @param channelName Name of the selected application to invite some one.
     */
    void inviteChannelSelected(String channelName);

}
