# Branch Metrics Android Invite SDK

This is the repository of our open source Android Invite SDK. There's a full demo app embedded in this repository.
The purpose of this SDK is to provide an out-of-the-box functional 'invite and welcome feature' for apps consuming the Branch SDK that want to utilize a standard invite feature in their app.
This SDK provide simple and flexible option for inviting users to application and welcoming new user to the application. Existing user can invite new users and when new users click on the
invitation link it will take them to the welcome screen.

There will still be some configuration on the dashboard, but the goal is to provide the most extensible, yet simple to use, full invite feature SDK. To see the basics of setting up an app with Branch, check out [the Branch Android SDK readme](https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK).

# Invite Flow
In your app, there will be a trigger to open the Invite UI. This will show a list of contacts and allow the user to select friends they want to invite to join them.When the the shared link clicked it will open the welcome screen

![Invite](https://s3-us-west-1.amazonaws.com/branchhost/invite_android.png)<br><br>![Welcome](https://s3-us-west-1.amazonaws.com/branchhost/welcome_android.png)




# Dependency
You need to have [the Branch Android SDK ](https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK) as a compiler dependency before using this Invite SDK
``` xml
dependencies {
    compile 'io.branch.sdk.android:library:1.8.+'
}
```
For more information please see [Installing Branch Android SDK ](https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK/blob/master/README.md#install-library-project)

# Installing the SDK
Following section describes the easy few steps to get started with Branch Invite SDK to your project.

## Add SDK to your project
if you are using the gradle build system simply add compile `compile 'io.branch.invite.sdk.android:library:0.1.+'` to the dependencies section of your build.gradle file.
``` xml
dependencies {
    compile 'io.branch.sdk.android:library:1.8.+'
    compile 'io.branch.invite.sdk.android:library:0.1.+'
}
```

Or you can also find the .jar file for the sdk from https://s3-us-west-1.amazonaws.com/branchhost/Branch-Android-Invite-SDK.zip if you want to use the .jar file
Also you can download the testbed project here The testbed project: https://s3-us-west-1.amazonaws.com/branchhost/Branch-Android-Invite-TestBed.zip

## Add permissions
Add Internet permission in your application manifest
``` xml
<uses-permission android:name="android.permission.INTERNET" />
```
If you are using Simple Invite Builder this is the only permission you need. If you want to Tabbed Invite Builder you need  to set read contact permission also.
``` xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

## Enable Branch
In your project's manifest file, make sure you have registered your main activity is configured to receive URI registered with Branch Link. if your app is already configured for Branch
deep linking you will have this set up already. Otherwise modify your manifest to add an additional intent filter as  shown below.

```xml
<activity
    android:name="com.yourapp.MainActivity"
    android:label="@string/app_name">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>

    <!-- Add this intent filter below, and change yourapp to your app name -->
    <intent-filter>
        <data android:scheme="yourapp" android:host="open" />
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>
</activity>
```

## Add Branch key
Make sure you have added valid Branch keys in your manifest.
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="io.branch.sample"
    android:versionCode="1"
    android:versionName="1.0" >

    <application>
        <!-- Other existing entries -->

        <!-- Add this meta-data below, and change "key_live_xxxxxxx" to your actual live Branch key -->
        <meta-data android:name="io.branch.sdk.BranchKey" android:value="key_live_xxxxxxx" />

        <!-- For your test app, if you have one; Again, use your actual test Branch key -->
        <meta-data android:name="io.branch.sdk.BranchKey.test" android:value="key_test_yyyyyyy" />
    </application>
</manifest>
```
That's all. Now you are ready to use Branch Invite SDK.
Note: Please make sure Branch session is initialised before using any of invite feature. If your app is not Branch Deep linking  enabled
please follow the simple steps in the link to enable Branch deep linking
https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK/blob/master/README.md#initialization

# Show Invite Screen
Once you passed the previous steps, showing an invitation screen is as simple as below

```java
new SimpleInviteBuilder(Activitycontext, "Inviting userID", "Inviting user Name").showInviteDialog();
```
This will create a chooser dialog for the users to select an application and invite them via that app.

if you would like to have Tabbed invite view with pre-populated user names you can use the Tabbed Invite Builder as below.
 ```java
 new TabbedInviteBuilder(context, "Inviting userID","Inviting user Name").create().show();
 ```


# Show Welcome Screen
Welcome screen is shown upon a new user install and open the app by clicking an invitation link. Add the following code to show a welcome dialog in any activity you prefer.
```java
 welcomeDialog_ = new WelcomeBuilder(activity).create();
                if(welcomeDialog_ != null) {
                    welcomeDialog_.show();
                }
```
This will show a welcome dialog with default style. If you wish to customise the view please refer to customise welcome screen.

# Advanced: Customize Welcome Screen
`WelcomeBuilder` Class flexible options to customise the look and feel or the messages for the default welcome screen. Additionally you can provide your own custom view also to show on welcome screen.
Here is a sample to demo welcome dialog customisation by using a `WelcomeViewStyle`

```java
welcomeDialog_ = new WelcomeBuilder(activity)
             .setWelcomeViewStyle(new WelcomeViewStyle(activity)
                     .setDefaultUserImage(getResources().getDrawable(R.drawable.contact_default))
                     .setInvitationMessage("You are invited to this app by $FULL_NAME")
                     .setWelcomeMessage("Welcome to this cool app. Have fun with your friend $SHORT_NAME")
                     .setProceedToAppMessage("Click me to proceed"))
                     .create();
               if(welcomeDialog_ != null) {
                   welcomeDialog_.show();
               };
```

If you really wish to provide a complete view for welcome screen, it is as simple as following
```java
    welcomeDialog_ = new WelcomeBuilder(activity)
                    .setWelcomeViewCallback(new WelcomeCallback() {
                    @Override
                    public View getCustomInvitationView(String userID, String inviterFullName, String inviterShortName, String userImageUrl, JSONObject customParameters) {
                        return myCustomInviteView; // return your custom view here. This view will be used for the welcome screen
                    }
                    @Override
                    public void onWelcomeDialogLaunched() {
                    }
                    @Override
                    public void onWelcomeDialogDismissed() {
                    }
                    @Override
                    public void onBranchError(BranchError error) {
                    }
                })
                        .create();
                if(welcomeDialog_ != null) {
                    welcomeDialog_.show();
                };
```
# Advanced: Customize Welcome Screen
In addition to flexible customization options both Simple Invite Builder and Tabbed Invite Builder provides options for adding custom views .
## Customise Simple Invite Screen
Simple invite screens displays a list of applications to send invitation. As user selects the application Branch will create invitation link and update the invitation sending application interface.
You can pre-configure the applications to be listed and customise the mesage to the invitee as follows.

```java
SimpleInviteBuilder simpleInviteBuilder = new SimpleInviteBuilder(activity, "Inviting userID", "Inviting user Name")
             .addPreferredInviteChannel(SharingHelper.SHARE_WITH.EMAIL)
             .addPreferredInviteChannel(SharingHelper.SHARE_WITH.FACEBOOK)
             .addPreferredInviteChannel(SharingHelper.SHARE_WITH.TWITTER)
             .addPreferredInviteChannel(SharingHelper.SHARE_WITH.MESSAGE)
             .setInviterImageUrl("https://s3-us-west-1.amazonaws.com/branchhost/mosaic_og.png")
             .addCustomParams("Custom_Param1", "Custom_Param1_value");
             .setInvitation("Invitation Title", "Invitation Message");
simpleInviteBuilder.show();
```

## Customise Tabbed Invite Screen
Tabbed invite screen preselected sharing application with the contacts populated. The default Tabbed invite view shows Email and Text Tabs. In addition to customise the style and view for the
Tabs you can add  custom tabs of your own.

```java
new TabbedInviteBuilder(activity, "Inviting userID", "Inviting user Name")
            .setTabStyle(getDrawable(R.drawable.tab_on), getDrawable(R.drawable.tab_off))
            .setPositiveButtonStyle(new ColorDrawable(Color.TRANSPARENT),"Invite", Color.BLUE)
            .setNegativeButtonStyle(new ColorDrawable(Color.TRANSPARENT),"Close", Color.MAGENTA)
            .setInviterImageUrl("https://s3-us-west-1.amazonaws.com/branchhost/mosaic_og.png")
            .setInvitationText("Invitation Title", "Invitation Message")
            .setPhoneTabText("Message")
            .setEmailTabText("E-mail")
            .setTitle("Invite a friend")
            .setSelectedItemColor(Color.parseColor("##FF0000FF"))
            .addCustomParams("Custom_Param", "This is a custom param")
            .create().show();
```

#Advanced: Adding a custom contacts provider
 You can add custom tab to the tabbed view by providing an instance of `InviteContactListView`. `InviteContactListView` is an abstract class which subclasses standard ListView.
 This class has abstracted methods need to be implemented by child classes for providing the contact list or the intent to share with the client application.

 ```java
 new TabbedInviteBuilder(activity, "Inviting userID", "Inviting user Name")
            .setEmailTabText("E-mail")
            .setTitle("Invite a friend")
            .addCustomTab("Facebook",FBInviteContactListView)  // Your custom tab with custom contact list and share intent as abstracted by InviteContactListView
            .create().show();
```