package io.branch.invite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * <p>
 * Main application class for Branch invite sdk. Create and Manages the views for inviting contacts.
 * Class handles actions when user click on an action item.
 * </p>
 */
class InviteManager {

    /* The custom chooser dialog for selecting an application to share the link */
    Dialog inviteDialog_;
    /* {@link Context} for the invite manager */
    Context context_;
    /* Static instance  for the invite manager */
    private static InviteManager thisInstance_;

    private InviteManager(){
        thisInstance_ = this;
    }

    /**
     * Get the singleton instance of {@link InviteManager}.
     * @return  {@link InviteManager} instance
     */
    public static InviteManager getInstance(){
        if(thisInstance_ == null){
            thisInstance_ = new InviteManager();
        }
        return thisInstance_;
    }

    public void inviteToApp(Context context ,InviteBuilderParams builderParams) {
        context_ = context;
        createInviteDialog(builderParams);
    }

    /**
     * Create and show an invitation dialog with the given options.
     */
    private void createInviteDialog(InviteBuilderParams builderParams) {
        RelativeLayout tabbedViewCover = new RelativeLayout(context_);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabbedViewCover.addView(new InviteContentView(context_, contactTabViewCallback_, builderParams), params);
        tabbedViewCover.setBackgroundColor(Color.WHITE);
        inviteDialog_ = new Dialog(context_);
        setDialogWindowAttributes();
        inviteDialog_.setContentView(tabbedViewCover);
        
        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        slideUp.setDuration(500);
        slideUp.setInterpolator(new AccelerateInterpolator());
        ((ViewGroup) inviteDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideUp);

        inviteDialog_.show();
    }

    private void setDialogWindowAttributes() {
        inviteDialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
        inviteDialog_.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inviteDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        inviteDialog_.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(inviteDialog_.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager wm = (WindowManager) context_.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.8f;
        inviteDialog_.getWindow().setAttributes(lp);
        inviteDialog_.getWindow().setWindowAnimations(android.R.anim.slide_in_left);
        inviteDialog_.setCanceledOnTouchOutside(true);
    }

    /**
     * Animation to be played on dismissing the dialog.
     */
    private void animateDismiss() {
        TranslateAnimation slideDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1f);
        slideDown.setDuration(500);
        slideDown.setInterpolator(new DecelerateInterpolator());
        ((ViewGroup) inviteDialog_.getWindow().getDecorView()).getChildAt(0).startAnimation(slideDown);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (inviteDialog_ != null) {
                    inviteDialog_.dismiss();
                    inviteDialog_ = null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Tab view events are handled here.
     */
    InviteContentView.IContactTabViewEvents contactTabViewCallback_ = new InviteContentView.IContactTabViewEvents() {
        @Override
        public void onNegativeButtonClicked() {
            animateDismiss();
        }

        @Override
        public void onPositiveButtonClicked(ArrayList<String> selectedContactName, InviteChannel selectedChannel, String targetPackage) {

        }

        @Override
        public void onContactSelected(ContactListAdapter.MyContact contact) {

        }
    };

}
