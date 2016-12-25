package com.donate.savelife.navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.donate.savelife.R;
import com.donate.savelife.chats.ChatActivity;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.home.HomeActivity;
import com.donate.savelife.intro.IntroActivity;
import com.donate.savelife.requirements.NeedActivity;
import com.donate.savelife.user.CompleteProfileActivity;
import com.donate.savelife.user.ProfileActivity;
import com.donate.savelife.welcome.WelcomeActivity;


public class AndroidNavigator implements Navigator {

    private final Activity activity;
    public static  final int FIRST_FLOW_REQUEST_CODE = 001;
    public static  final int FIRST_FLOW_RESPONSE_CODE = 002;

    public AndroidNavigator(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void toHome() {
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.finish();
    }


    @Override
    public void toIntro() {
        Intent introIntent = new Intent(activity, IntroActivity.class);
        activity.startActivityForResult(introIntent, FIRST_FLOW_REQUEST_CODE);
    }

    @Override
    public void toParent() {
        activity.finish();
    }

    @Override
    public void toChat(Need need) {
        activity.startActivity(ChatActivity.createIntentFor(activity, need));
    }


    @Override
    public void toCompleteProfile() {
        Intent intent = new Intent(activity, CompleteProfileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void toNeed() {
        Intent intent = new Intent(activity, NeedActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void toMain() {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void toShareInvite(String sharingLink) {
        String sharingMessage = activity.getString(R.string.str_send_invite);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.str_check_out_app, sharingLink));
        activity.startActivity(Intent.createChooser(sharingIntent, sharingMessage));
    }

    @Override
    public void toProfile(String needID, String userID) {
        activity.startActivity(ProfileActivity.createIntentFor(activity, needID, userID));
    }

    @Override
    public void toDialNumber(String dialPhoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + dialPhoneNumber));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    @Override
    public void toMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    @Override
    public void toRateUs() {
        Intent intentMarket = new Intent(Intent.ACTION_VIEW);
        PackageManager myAppPackage = activity.getPackageManager();
        intentMarket
                .setData(Uri
                        .parse("https://play.google.com/store/apps/details?id=com.donate.savelife"));

        if (intentMarket.resolveActivity(myAppPackage) != null) {
            activity.startActivity(intentMarket);
        }
    }
}
