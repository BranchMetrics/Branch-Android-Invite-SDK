# Branch Metrics Android Invite SDK

## [New documentation portal](https://dev.branch.io) and [support portal with user forums](http://support.branch.io)

This is the repository of our open source Android Invite SDK. There's a full demo app embedded in this repository.
The purpose of this SDK is to provide an out-of-the-box functional 'invite feature' for apps consuming the Branch SDK that want to utilize a standard invite feature in their app.

There will still be some configuration on the dashboard, but the goal is to provide the most extensible, yet simple to use, full invite feature SDK. To see the basics of setting up an app with Branch, check out [the Branch Android SDK readme](https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK).

# Dependency
You need to have [the Branch Android SDK ](https://github.com/BranchMetrics/Android-Deferred-Deep-Linking-SDK) as a compiler dependency before using this Invite SDK
``` xml
dependencies {
    compile 'io.branch.sdk.android:library:1.8.+'
}
```
# Invite Flow
In your app, there will be a trigger to open the Invite UI. This will show a list of contacts and allow the user to select friends they want to invite to join them.
