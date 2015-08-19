package io.branch.invitedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import io.branch.invite.SimpleInviteBuilder;
import io.branch.invite.WelcomeBuilder;
import io.branch.invite.WelcomeCallback;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;


/**
 * Activity to demonstrate Branch Invite SDK. Branch Deep-Link SDK need to be added and initialised
 * in order to use the Branch Invite SDK.Make sure you are added Branch referral SDK as compile dependency.
 */
public class InviteDemoActivity extends Activity {
    Branch branch;

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

                new SimpleInviteBuilder(InviteDemoActivity.this,"123455","Sojan")
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
                    Log.i("BranchInviteTestBed", "Latest Referring params!" +branch.getLatestReferringParams());
                }

                // Handle Invitation with branch default style and flow
                new WelcomeBuilder(InviteDemoActivity.this).show();
//
//                // Add custom Style to invitation
//                WelcomeViewStyle invitationStyle = new WelcomeViewStyle(InviteDemoActivity.this)
//                        .setDefaultUserImage(getResources().getDrawable(R.drawable.contact_default))
//                        .setInvitationMessage("You are invited to this app by $FULL_NAME")
//                        .setWelcomeMessage("Welcome to this cool app. Have fun with your friend $SHORT_NAME")
//                        .setProceedToAppMessage("Click me to proceed");
//
//                BranchInvitationHandler.HandleInvitations(InviteDemoActivity.this, invitationStyle, invitationUIListener);
//
//                //Add custom view for invitation
//                BranchInvitationHandler.HandleInvitations(InviteDemoActivity.this, invitationUIListener);
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    WelcomeCallback invitationUIListener = new WelcomeCallback() {
        @Override
        public View getCustomInvitationView(String userID, String inviterFullName, String inviterShortName, String userImageUrl) {
            return null;
        }

        @Override
        public void onInvitationDialogLaunched() {
            Log.d("BranchInviteTestBed","onInvitationDialogLaunched()");
        }

        @Override
        public void onInvitationDialogDismissed() {
            Log.d("BranchInviteTestBed","onInvitationDialogDismissed()");
        }
    };

    //Invitation listener for custom view
    WelcomeCallback customInvitationUIListener = new WelcomeCallback() {
        @Override
        public View getCustomInvitationView(String userID, String inviterFullName, String inviterShortName, String userImageUrl) {
            return null;
        }

        @Override
        public void onInvitationDialogLaunched() {
            Log.d("BranchInviteTestBed","onInvitationDialogLaunched()");
        }

        @Override
        public void onInvitationDialogDismissed() {
            Log.d("BranchInviteTestBed","onInvitationDialogDismissed()");
        }
    };


}
