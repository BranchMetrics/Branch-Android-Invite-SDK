package io.branch.invite;

/**
 * Created by sojanpr on 8/13/15.
 */
public enum InviteChannel {
    EMAIL("com.google.android.email"),
    FACEBOOK("com.facebook.katana"),
    TWITTER("com.twitter.android"),
    MESSAGE(".mms"),
    FLICKR("com.yahoo.mobile.client.android.flickr"),
    GOOGLE_DOC("com.google.android.apps.docs"),
    WHATS_APP("com.whatsapp"),
    CUSTOM("need to get package name");

    private String name = "";

    private InviteChannel(String key) {
        this.name = key;
    }

    public String getChannelName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
