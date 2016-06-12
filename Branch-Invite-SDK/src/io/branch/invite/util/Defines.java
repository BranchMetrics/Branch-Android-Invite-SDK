package io.branch.invite.util;

/**
 * <p>
 * Defines all Json keys associated with branch invite parameters.
 * </p>
 */
public enum Defines {

    INVITE_USER_ID("INVITING_USER_ID"),
    INVITE_USER_FULLNAME("INVITING_USER_FULLNAME"),
    INVITE_USER_SHORT_NAME("INVITING_USER_SHORT_NAME"),
    INVITE_USER_IMAGE_URL("INVITING_USER_IMAGE_URL"),
    FULL_NAME_SUB("$FULL_NAME"),
    SHORT_NAME_SUB("$SHORT_NAME");

    private String key = "";

    Defines(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
