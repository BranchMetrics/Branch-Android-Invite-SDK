package io.branch.invite;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.callback.Callback;

import io.branch.referral.Branch;

/**
 * Created by sojanpr on 8/17/15.
 */
public class BranchInvitationHandler {
    private static BranchInvitationHandler thisInstance_;

    /* The custom chooser dialog for selecting an application to share the link */
    Dialog invitationHandlerDialog_;
    /* {@link Context} for the invite manager */
    Context context_;
    InvitationUIListener callback_;
    View invitationView_;
    InvitationStyle invitationStyle_;
    LoadBitmapFromUrlTask imageLoadTask_;

    private BranchInvitationHandler() {
        thisInstance_ = this;
    }

    public static boolean HandleInvitations(Context context, InvitationStyle invitationStyle, InvitationUIListener callback){
        if(thisInstance_ == null){
            thisInstance_ = new BranchInvitationHandler();
        }
        else{
            thisInstance_.cancelDialog();
        }
        return thisInstance_.checkAndHandleInvitations(context, invitationStyle, callback);
    }

    public static void cancelInvitationDialog(){
        if(thisInstance_ != null){
            thisInstance_.cancelDialog();
        }
    }

    private boolean checkAndHandleInvitations(Context context, InvitationStyle invitationStyle, InvitationUIListener callback){
        context_ = context;
        invitationStyle_ = invitationStyle;
        callback_ = callback;

        boolean isInvitationHandled = false;
        if (Branch.getInstance() != null) {
            JSONObject latestReferringParams = Branch.getInstance().getLatestReferringParams();
            // Check if the link has inviter info.
            if (latestReferringParams.has(Defines.INVITE_USER_ID.getKey())
                    && latestReferringParams.has(Defines.INVITE_USER_FULLNAME.getKey())) {
                try {
                    // The link is referral type.Then get the inviter info
                    String userID = latestReferringParams.getString(Defines.INVITE_USER_ID.getKey());
                    String userFullName = latestReferringParams.getString(Defines.INVITE_USER_FULLNAME.getKey());

                    String userShortName = "";
                    if (latestReferringParams.has(Defines.INVITE_USER_ID.getKey())) {
                        userShortName = latestReferringParams.getString(Defines.INVITE_USER_SHORT_NAME.getKey());
                    }
                    String userImageUrl = "";
                    if (latestReferringParams.has(Defines.INVITE_USER_IMAGE_URL.getKey())) {
                        userImageUrl = latestReferringParams.getString(Defines.INVITE_USER_IMAGE_URL.getKey());
                    }
                    // Check if a custom view is desired for invitation.
                    if (callback != null) {
                        invitationView_ = callback.getCustomInvitationView(userID, userFullName, userShortName, userImageUrl);
                    }
                    //If user has not provided a custom view create a view with style specified.
                    if (invitationView_ == null) {
                        invitationView_ = new InvitationShowView(context_);
                        ((InvitationShowView) invitationView_).updateView(userFullName, userShortName, userImageUrl);
                    }
                    createInvitationHandlerDialog();
                    isInvitationHandled = true;

                } catch (JSONException ignore) {

                }
            }

        }
        return isInvitationHandled;
    }



    /**
     * Create and show an invitation dialog with the given options.
     */
    private void createInvitationHandlerDialog() {
        RelativeLayout invitationCoverlayout = new RelativeLayout(context_);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        invitationCoverlayout.addView(invitationView_, params);
        invitationCoverlayout.setBackgroundColor(Color.WHITE);
        if (invitationHandlerDialog_ != null && invitationHandlerDialog_.isShowing()) {
            invitationHandlerDialog_.dismiss();
        }
        invitationHandlerDialog_ = new Dialog(context_);
        setDialogWindowAttributes();
        invitationHandlerDialog_.setContentView(invitationCoverlayout);

        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        slideUp.setDuration(500);
        slideUp.setInterpolator(new AccelerateInterpolator());
        ((ViewGroup) invitationHandlerDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideUp);

        invitationHandlerDialog_.show();
        if(callback_ != null){
            callback_.onInvitationDialogLaunched();
        }

        invitationHandlerDialog_.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(imageLoadTask_ != null){
                    imageLoadTask_.cancel(true);
                }

                if(callback_ != null){
                    callback_.onInvitationDialogDismissed();
                }
            }
        });
    }

    /**
     * Set the window attributes for the invite dialog.
     */
    private void setDialogWindowAttributes() {
        invitationHandlerDialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
        invitationHandlerDialog_.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        invitationHandlerDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        invitationHandlerDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(invitationHandlerDialog_.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager wm = (WindowManager) context_.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.8f;
        invitationHandlerDialog_.getWindow().setAttributes(lp);
        invitationHandlerDialog_.getWindow().setWindowAnimations(android.R.anim.slide_in_left);
        invitationHandlerDialog_.setCanceledOnTouchOutside(true);
    }


    private void cancelDialog(){
        if(invitationHandlerDialog_ != null && invitationHandlerDialog_.isShowing()){
            invitationHandlerDialog_.dismiss();
        }
    }


    private class InvitationShowView extends LinearLayout {

        int  inviterInfoBackground_  = Color.WHITE;
        int inviteMsgBackground_  = Color.BLUE;
        Drawable defaultContactImg_  = new ColorDrawable(Color.GRAY);
        int contactPicSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        String doneBtnTxt = "Press to join %s";

        ImageView contactImg_;
        TextView inviterInfoText_;
        TextView welcomeMsgText_;
        TextView proceedToAppText;

        public InvitationShowView(Context context) {
            super(context);
            this.setOrientation(VERTICAL);
            LinearLayout.LayoutParams paramsInfo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, .6f);
            LinearLayout.LayoutParams paramsMsg = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, .4f);

            RelativeLayout inviterInfoLayout = new RelativeLayout(context);
            inviterInfoLayout.setBackgroundColor(inviterInfoBackground_);
            inviterInfoLayout.setPadding(padding, padding, padding, padding);

            contactImg_ = new ImageView(context);
            contactImg_.setId(BranchInviteUtil.generateViewId());
            contactImg_.setScaleType(ImageView.ScaleType.FIT_CENTER);
            contactImg_.setImageDrawable(defaultContactImg_);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(contactPicSize, contactPicSize);
            layoutParams.topMargin = 3*padding;
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            inviterInfoLayout.addView(contactImg_, layoutParams);

            inviterInfoText_ = new TextView(context_);
            inviterInfoText_.setBackgroundColor(inviterInfoBackground_);
            inviterInfoText_.setGravity(Gravity.CENTER);
            inviterInfoText_.setTextAppearance(context_, android.R.style.TextAppearance_Large);
            inviterInfoText_.setTextColor(inviteMsgBackground_);
            inviterInfoText_.setMaxLines(3);
            inviterInfoText_.setEllipsize(TextUtils.TruncateAt.END);
            inviterInfoText_.setText("Info testt    vdcvsvdgfdsgsgsdaggsg dsfsdgfsgfgfdgsdgsdgsgsdggdsgsadgsdgggggggggsav fhbad fjbdasbfjnbsdnfbdsknkdsfkdskfsdkfksdbkgfnsdknbgksdngkfjs");

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = padding;
            layoutParams.addRule(RelativeLayout.BELOW, contactImg_.getId());
            inviterInfoLayout.addView(inviterInfoText_, layoutParams);



            RelativeLayout inviteMsgLayout = new RelativeLayout(context);
            inviteMsgLayout.setBackgroundColor(inviteMsgBackground_);
            inviteMsgLayout.setPadding(padding, padding, padding, padding);

            welcomeMsgText_ = new TextView(context_);
            welcomeMsgText_.setBackgroundColor(inviteMsgBackground_);
            welcomeMsgText_.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            welcomeMsgText_.setTextColor(inviterInfoBackground_);
            welcomeMsgText_.setTextAppearance(context_, android.R.style.TextAppearance_Medium);
            welcomeMsgText_.setText("fdwhfbdbfdsfd nf d  ");
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            inviteMsgLayout.addView(welcomeMsgText_, layoutParams);

            proceedToAppText = new TextView(context_);
            proceedToAppText.setBackgroundColor(inviteMsgBackground_);
            proceedToAppText.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            proceedToAppText.setText(doneBtnTxt);
            proceedToAppText.setTextAppearance(context_, android.R.style.TextAppearance_Small);
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);welcomeMsgText_.setTextColor(inviterInfoBackground_);
            layoutParams.bottomMargin = padding;
            inviteMsgLayout.addView(proceedToAppText, layoutParams);

            this.addView(inviterInfoLayout, paramsInfo);
            this.addView(inviteMsgLayout, paramsMsg);

        }


       private void updateView(String userFullName, String userShortName, String imageUrl){
           //Set invitation message
           inviterInfoText_.setText(formatWithName(invitationStyle_.invitationMessageText_, userFullName, userShortName));

           //Set Welcome message
           welcomeMsgText_.setText(formatWithName(invitationStyle_.welcomeMessageText_, userFullName, userShortName));

           //Set proceed to app text
           proceedToAppText.setText(formatWithName(invitationStyle_.proceedToAppText_, userFullName, userShortName));

           //Load user image
           imageLoadTask_ =  new LoadBitmapFromUrlTask(contactImg_,imageUrl, invitationStyle_.defaultContactImg_);
           imageLoadTask_.execute();
       }

        private String formatWithName(String rawString, String userFullName,  String userShortName){
            if(rawString.contains(Defines.FULL_NAME_SUB.getKey())){
                rawString = rawString.replace(Defines.FULL_NAME_SUB.getKey(), userFullName);
            }
            if(rawString.contains(Defines.SHORT_NAME_SUB.getKey())){
                //ShortName is optional. So fall back to full name in case short name not available
                if(userShortName == null || userShortName.trim().length() < 1){
                    userShortName = userFullName;
                }
                rawString = rawString.replace(Defines.SHORT_NAME_SUB.getKey(), userShortName);
            }
            return  rawString;
        }

    }

    private class LoadBitmapFromUrlTask extends AsyncTask< Void, Void, Bitmap> {
        final ImageView imageView_;
        final String url_;

        LoadBitmapFromUrlTask(ImageView imgView, String url, Drawable defaultImage){
            imageView_ = imgView;
            url_ = url;
            imageView_.setImageDrawable(defaultImage);
        }
        @Override
        protected Bitmap doInBackground( Void... voids ) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(url_).openConnection().getInputStream());
            } catch ( MalformedURLException ignore ) {
            } catch ( IOException ignore ) {
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute( Bitmap image ) {
            super.onPostExecute(image);
            if(image != null) {
                imageView_.setImageBitmap(image);
            }
        }
    }


}
