package io.branch.invite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.HashMap;

/**
 * <p>
 * Class for representing parameters for invite  builder.
 * This class represent all the features that user can set for a tabbed invite dialog.
 * </p>
 */
public class TabBuilderParams {
    /* Selected background for the tab */
    public Drawable tabSelectedBackground_;
    /* Unselected background for the tab */
    public Drawable tabUnselectedBackground_;

    /* Tab action bar positive button text */
    public String positiveButtonText_;
    /* Tab action bar negative button text */
    public String negativeButtonText_;
    /* Text color for negative button */
    public int negativeBtnTextColor;
    /* Text color for positive button */
    public int positiveBtnTextColor;
    /* Background drawable for positive button */
    public Drawable positiveBtnBackground;
    /* Drawable background for negative button */
    public Drawable negativeBtnBackground;
    /* Background drawable for the tab view.*/
    public Drawable backgroundDrawable_;

    /* Name for email contact tab */
    public String emailTabText_;
    /* Name for phone contact tab */
    public String textTabText_;

    /* Drawable to show when there is no profile picture */
    public Drawable defaultContactPic_;
    /* Denote whether single selection enabled or not */
    public boolean isSingleSelect_;
    /* Message to send to the invitee */
    public String invitationMsg_;
    /* Subject of invitation */
    public String invitationSubject_;
    /* Default url specified for invitation*/
    public String defaultInvitationUrl_;

    /* User ID for the inviting person */
    public String userID_;
    /* Name of the inviting person */
    public String userFullName_;
    /* Url to the inviting users profile picture */
    public String userImageUrl_;
    /* Inviting persons last name */
    public String userShortName_;
    /* Map to hold the custom parameters added  by user */
    public final HashMap<String, String> customDataMap_;
    /* Callback to notify the invite process status */
    public InviteCallback callback_;
    /* Map to hold the custom Tabs */
    public final HashMap<String, InviteContactListView> customTabMap_;
    /* Message to show when there is no contact available */
    public String noContactAvailableMsg_;
    /* Text view for setting a title for the sharing dialog */
    public TextView titleTxtVew_;
    /* Background color for selected items in the list view */
    public int selectedItemBackGroundColor_;




    public TabBuilderParams(Context context) {
        tabSelectedBackground_ = new ColorDrawable(Color.parseColor("#FF000088")); //Default selected color for the tabs
        tabUnselectedBackground_ = new ColorDrawable(Color.parseColor("#FF0000DD"));//Default non-selected color for the tabs

        positiveButtonText_ = "Done";
        negativeButtonText_ = "Cancel";
        negativeBtnTextColor = Color.BLUE;
        positiveBtnTextColor = Color.BLUE;
        positiveBtnBackground = new ColorDrawable(Color.TRANSPARENT);
        negativeBtnBackground = new ColorDrawable(Color.TRANSPARENT);
        backgroundDrawable_ = new ColorDrawable(Color.WHITE);
        selectedItemBackGroundColor_ = Color.parseColor("#FF99FFCC");

        emailTabText_ = "Email";
        textTabText_ = "Text";

        defaultContactPic_ = context.getResources().getDrawable(android.R.drawable.gallery_thumb);
        isSingleSelect_ = false;
        String appLabel = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        invitationSubject_ = "Check out " + appLabel + "!";
        invitationMsg_ = "Check out this cool app named " + appLabel;
        defaultInvitationUrl_ = "https://play.google.com/store/apps/details?id=" + context.getPackageName();

        userID_ = "";
        userFullName_ = "";
        userImageUrl_ = "";
        userShortName_ = "";

        customDataMap_ = new HashMap<>();
        customTabMap_ = new HashMap<>();

        noContactAvailableMsg_ = "No Contacts available";

        titleTxtVew_ = null;

        callback_ = null;
    }

}
