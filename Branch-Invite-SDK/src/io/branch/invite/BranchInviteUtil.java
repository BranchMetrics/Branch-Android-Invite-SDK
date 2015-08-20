package io.branch.invite;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Utility class for Branch invite sdk</p>
 */
class BranchInviteUtil {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * </p>Generate a value suitable for use in {@link View#setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.</p>x
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * <p>Check if the specified  package is installed on the device</p>
     *
     * @param context     Current context
     * @param packageName The complete package name to be checked for installation status
     * @return A {@link Boolean} whose value is true if the package is installed on the device
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * <p>Converts the given list to a comma separated string to be used with email address field</p>
     *
     * @param stringList a list of strings to be converted to CSV format
     * @return a comma separated string representation of the given list
     */
    public static String formatListToCSV(ArrayList<String> stringList) {
        String formattedContactList = "";
        for (String contactName : stringList) {
            formattedContactList += contactName + ";";
        }
        if (formattedContactList.length() > 0) {
            formattedContactList = formattedContactList.substring(0, formattedContactList.length() - 1);
        }
        return formattedContactList;
    }

    /**
     * <p>Set the view background with the given drawable. Calls appropriate methods depending on the API level.</p>
     *
     * @param view     View whose background need to be set
     * @param drawable Drawable to set as background
     */
    public static void setViewBackground(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }


}
