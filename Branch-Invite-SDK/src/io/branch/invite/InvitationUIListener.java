package io.branch.invite;

import android.view.View;

/**
 * Created by sojanpr on 8/19/15.
 */
public interface InvitationUIListener {
    View getCustomInvitationView (String userID, String inviterFullName, String inviterShortName, String userImageUrl);

    void onInvitationDialogLaunched();
    void onInvitationDialogDismissed();

}
