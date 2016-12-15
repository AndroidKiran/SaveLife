package com.donate.savelife.link;

import android.net.Uri;

import com.donate.savelife.core.link.LinkFactory;
import com.donate.savelife.core.user.data.model.User;

import java.net.URI;

public class FirebaseDynamicLinkFactory implements LinkFactory {

    public static final String SENDER = "sender";
    private final String dynamicLinkDomain;
    private final String deepLinkBaseUrl;
    private final String androidPackageName;

    public FirebaseDynamicLinkFactory(String dynamicLinkDomain, String deepLinkBaseUrl, String androidPackageName) {
        this.dynamicLinkDomain = dynamicLinkDomain;
        this.deepLinkBaseUrl = deepLinkBaseUrl;
        this.androidPackageName = androidPackageName;
    }

    @Override
    public URI inviteLinkFrom(User user) {
        Uri uri = Uri.parse(dynamicLinkDomain)
                .buildUpon()
                .appendQueryParameter("link", welcomeDeepLinkFromUser(user).toString())
                .appendQueryParameter("apn", androidPackageName)
                .build();
        return URI.create(uri.toString());
    }

    private Uri welcomeDeepLinkFromUser(User user) {
        return Uri.parse(deepLinkBaseUrl)
                .buildUpon()
                .appendPath("welcome")
                .appendQueryParameter(SENDER, user.getId())
                .build();
    }

}
