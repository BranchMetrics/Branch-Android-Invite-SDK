package io.branch.invitedemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.FormatFlagsConversionMismatchException;

import io.branch.invite.SimpleInviteBuilder;
import io.branch.invite.TabbedInviteBuilder;
import io.branch.invite.welcome.WelcomeBuilder;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;


/**
 * Activity to demonstrate Branch Invite SDK. Branch Deep-Link SDK need to be added and initialised
 * in order to use the Branch Invite SDK. Make sure you are added Branch referral SDK as compile dependency.
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

                //-----------  Simple invite View -------------------------------//
                new SimpleInviteBuilder(InviteDemoActivity.this, "123456", "Dalu james").showInviteDialog();

                //-- Here is how to Customise simple invite view ----------------------------------------//

               /* SimpleInviteBuilder simpleInviteBuilder = new SimpleInviteBuilder(InviteDemoActivity.this, "123456", "Dalu James")
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.EMAIL)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.TWITTER)
                        .addPreferredInviteChannel(SharingHelper.SHARE_WITH.MESSAGE)
                        .setInviterImageUrl("https://s3-us-west-1.amazonaws.com/branchhost/mosaic_og.png")
                        .addCustomParams("Custom_Param1", "Custom_Param1_value")
                        .setInvitation("Invitation Title", "Invitation Message");

                simpleInviteBuilder.showInviteDialog(); */


                //----------------- Tabbed invite view ---------------------------------//
               //new TabbedInviteBuilder(InviteDemoActivity.this, "My userID", "My Name").create().show();



                //-- Here is how to customise tabbed invite view------------------------------------//

                /*new TabbedInviteBuilder(InviteDemoActivity.this, "My userID", "My Name")
                        .setTabStyle(getDrawable(R.drawable.tab_on), getDrawable(R.drawable.tab_off))
                        .setPositiveButtonStyle(new ColorDrawable(Color.TRANSPARENT),"Invite", Color.BLUE)
                        .setNegativeButtonStyle(new ColorDrawable(Color.TRANSPARENT),"Close", Color.MAGENTA)
                        .setInviterImageUrl("https://s3-us-west-1.amazonaws.com/branchhost/mosaic_og.png")
                        .setInvitationText("Invitation Title", "Invitation Message")
                        .setPhoneTabText("Message")
                        .setEmailTabText("E-mail")
                        .setTitle("Invite a friend")
                        .setSelectedItemColor(Color.parseColor("##FF0000FF"))
                        .addCustomParams("Custom_Param", "This is a custom param")
                        .addCustomTab("Facebook",inv)
                        .create().show();
                  */

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
                welcomeDialog_ = new WelcomeBuilder(InviteDemoActivity.this).create();
                if(welcomeDialog_ != null) {
                    welcomeDialog_.show();
                }

                //----  Here is how to customise your Branch welcome screen---------------//

                /*welcomeDialog_ = new WelcomeBuilder(InviteDemoActivity.this)
                                .setWelcomeViewStyle(new WelcomeViewStyle(InviteDemoActivity.this)
                                .setDefaultUserImage(getResources().getDrawable(R.drawable.contact_default))
                                .setInvitationMessage("You are invited to this app by $FULL_NAME")
                                .setWelcomeMessage("Welcome to this cool app. Have fun with your friend $SHORT_NAME")
                                .setProceedToAppMessage("Click me to proceed"))
                        .create();
                if(welcomeDialog_ != null) {
                    welcomeDialog_.show();
                };*/


                //----  Here is how to add a custom welcome view---------------------//

                /*welcomeDialog_ = new WelcomeBuilder(InviteDemoActivity.this)
                        .setWelcomeViewCallback(new WelcomeCallback() {
                            @Override
                            public View getCustomInvitationView(String userID, String inviterFullName, String inviterShortName, String userImageUrl, JSONObject customParameters) {
                                return getCustomView(inviterFullName);
                            }

                            @Override
                            public void onWelcomeDialogLaunched() {
                            }

                            @Override
                            public void onWelcomeDialogDismissed() {
                            }

                            @Override
                            public void onBranchError(BranchError error) {
                            }
                        })
                        .create();
                if(welcomeDialog_ != null) {
                    welcomeDialog_.show();
                };*/



            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    /**
     * Creates a custom view for welcome dialog
     */
   /* private View getCustomView(String inviterFullName) {
        View customView = getLayoutInflater().inflate(R.layout.welcome_layout, null);
        ((TextView) customView.findViewById(R.id.inviteText)).setText(inviterFullName + " Invited you to this application");
        customView.findViewById(R.id.proceedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeDialog_.cancel();
            }
        });
        return customView;
    }*/
}
