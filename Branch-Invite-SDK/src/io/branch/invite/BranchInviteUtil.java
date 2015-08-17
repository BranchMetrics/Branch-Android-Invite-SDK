package io.branch.invite;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * <p>Utility class for Branch invite sdk</p>
 */
class BranchInviteUtil {

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
}
