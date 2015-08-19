package io.branch.invite;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import java.security.PrivateKey;

/**
 * Created by sojanpr on 8/19/15.
 */
class WelcomeDialog extends Dialog {
    private static boolean isClosing_  = false;
    public WelcomeDialog(Context context) {
        super(context);
        init();
    }

    public WelcomeDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected WelcomeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    slideClose();
                }
                return true;
            }
        });
    }
    public void slideOpen(){
        TranslateAnimation slideUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        slideUp.setDuration(500);
        slideUp.setInterpolator(new AccelerateInterpolator());
        ((ViewGroup)getWindow().getDecorView()).getChildAt(0).startAnimation(slideUp);

        show();
    }

    public void slideClose(){
        if(!isClosing_) {
            TranslateAnimation slideDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1f);
            slideDown.setDuration(500);
            slideDown.setInterpolator(new DecelerateInterpolator());

            ((ViewGroup) getWindow().getDecorView()).getChildAt(0).startAnimation(slideDown);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isClosing_ = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isClosing_ = false;
                    dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }



}
