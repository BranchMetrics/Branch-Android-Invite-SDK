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


}
