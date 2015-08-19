package io.branch.invite;

import java.security.Key;

/**
 * Created by sojanpr on 8/18/15.
 */
enum Defines {

    Feature("feature"),
    INVITE_USER_ID("BRANCH_INVITE_USER_ID_KEY"),
    INVITE_USER_FULLNAME("BRANCH_INVITE_USER_FULLNAME_KEY"),
    INVITE_USER_SHORT_NAME("BRANCH_INVITE_USER_SHORT_NAME_KEY"),
    INVITE_USER_IMAGE_URL("BRANCH_INVITE_USER_IMAGE_URL_KEY"),
    FULL_NAME_SUB("$FULL_NAME"),
    SHORT_NAME_SUB("$SHORT_NAME");

    private String key = "";
    private Defines(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }

}
