package io.branch.invitedemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import io.branch.invite.SimpleInviteBuilder;
import io.branch.invite.WelcomeBuilder;
import io.branch.invite.WelcomeCallback;
import io.branch.invite.WelcomeViewStyle;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;


/**
 * Activity to demonstrate Branch Invite SDK. Branch Deep-Link SDK need to be added and initialised
 * in order to use the Branch Invite SDK.Make sure you are added Branch referral SDK as compile dependency.
 */
public class InviteDemoActivity extends Activity {
    Branch branch;
    Dialog welcomeDialog_ = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_main);

        findViewById(R.id.invite_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TabbedInviteBuilder(InviteDemoActivity.this, "My userID", "My Name")
//                        .setTabStyle(new ColorDrawable(Color.RED), new ColorDrawable(Color.GREEN))
//                        .setPositiveButtonStyle(new ColorDrawable(Color.GREEN),"Invite", Color.GRAY)
//                        .setNegativeButtonStyle(new ColorDrawable(Color.YELLOW),"Close", Color.MAGENTA)
//                        .addCustomParams("Custom_Param", "This is a custom param")
//                        .showInviteDialog();

                new SimpleInviteBuilder(InviteDemoActivity.this, "123455", "Sojan")
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.EMAIL)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.TWITTER)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.MESSAGE)
                        .showInviteDialog();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        branch = branch.getInstance();

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams,
                                       BranchError error) {
                if (error != null) {
                    Log.i("BranchInviteTestBed", "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.i("BranchInviteTestBed", "Latest Referring params!" + branch.getLatestReferringParams());
                }

                //----  Branch default welcome screen----------------//
                final Dialog dialog = new WelcomeBuilder(InviteDemoActivity.this).show();

                //----  Here is how to customise your Branch welcome screen---------------//

                /* new WelcomeBuilder(InviteDemoActivity.this)
                        .setInvitationStyle(new WelcomeViewStyle(InviteDemoActivity.this)
                                .setDefaultUserImage(getResources().getDrawable(R.drawable.contact_default))
                                .setInvitationMessage("You are invited to this app by $FULL_NAME")
                                .setWelcomeMessage("Welcome to this cool app. Have fun with your friend $SHORT_NAME")
                                .setProceedToAppMessage("Click me to proceed"))
                        .show();
                 */

                //----  Here is how to add a custom welcome view---------------------//

                /*   welcomeDialog_ = new WelcomeBuilder(InviteDemoActivity.this)
                        .setInvitationUICallback(new WelcomeCallback() {
                            @Override
                            public View getCustomInvitationView(String userID, String inviterFullName, String inviterShortName, String userImageUrl, JSONObject customParameters) {
                                return getCustomView(inviterFullName);
                            }

                            @Override
                            public void onInvitationDialogLaunched() {}

                            @Override
                            public void onInvitationDialogDismissed() {}

                            @Override
                            public void onBranchError(BranchError error) {}
                        })
                        .show();
                 */


            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    private View getCustomView(String inviterFullName) {
        View customView = getLayoutInflater().inflate(R.layout.welcome_layout, null);
        ((TextView) customView.findViewById(R.id.inviteText)).setText(inviterFullName + " Invited you to this application");
        customView.findViewById(R.id.proceedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeDialog_.dismiss();
            }
        });
        return customView;
    }
}
