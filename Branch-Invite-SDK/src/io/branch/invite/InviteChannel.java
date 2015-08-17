package io.branch.invite;

/**
 * Created by sojanpr on 8/13/15.
 */
public enum InviteChannel {
    EMAIL("Email","com.google.android.gm"),
    FACEBOOK("Facebook","com.facebook.katana"),
    TWITTER("Twitter","com.twitter.android"),
    MESSAGE("Message","vnd.android-dir/mms-sms"),
    FLICKR("Flickr","com.yahoo.mobile.client.android.flickr"),
    GOOGLE_DOC("Google Doc","com.google.android.apps.docs"),
    WHATS_APP("Whats App","com.whatsapp"),
    CUSTOM("","");

    private String name = "";
    private String type = "";

    private InviteChannel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getTargetType() {
        return type;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setChannelName(String channelName){
        name = channelName;
    }

}
