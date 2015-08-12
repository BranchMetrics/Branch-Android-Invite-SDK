package io.branch.invitedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import io.branch.invite.InviteBuilder;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;


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
                new InviteBuilder(InviteDemoActivity.this).
                        setTabStyle(new ColorDrawable(Color.RED), new ColorDrawable(Color.GREEN))
                        .show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        branch = Branch.getInstance();
        branch.setDebug();
        //branch.disableTouchDebugging();

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams,
                                       BranchError error) {
                if (error != null) {
                    Log.i("BranchTestBed", "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.i("BranchTestBed", "branch init complete!");
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

}
