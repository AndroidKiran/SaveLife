package com.donate.savelife.navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.donate.savelife.CentralAppServiceIml;
import com.donate.savelife.R;
import com.donate.savelife.chats.ChatActivity;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.country.CountryActivity;
import com.donate.savelife.home.HomeActivity;
import com.donate.savelife.intro.IntroActivity;
import com.donate.savelife.launcher.LauncherActivity;
import com.donate.savelife.requirements.MyNeedsActivity;
import com.donate.savelife.requirements.NeedActivity;
import com.donate.savelife.user.CompleteProfileActivity;
import com.donate.savelife.user.HonorHeroesActivity;
import com.donate.savelife.user.ProfileActivity;
import com.donate.savelife.welcome.WelcomeActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;


public class AndroidNavigator implements Navigator {

    private final Activity activity;

    public static final int FIRST_FLOW_REQUEST_CODE = 001;
    public static final int FIRST_FLOW_RESPONSE_CODE = 002;

    public AndroidNavigator(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void toHome() {
        activity.startActivity(HomeActivity.createIntentFor(activity));
        activity.onBackPressed();
    }


    @Override
    public void toIntro() {
        Intent introIntent = new Intent(activity, IntroActivity.class);
        activity.startActivityForResult(introIntent, FIRST_FLOW_REQUEST_CODE);
    }

    @Override
    public void toParent() {
        activity.onBackPressed();
    }

    public void toChat(String needId) {
        activity.startActivity(ChatActivity.createIntentFor(activity, needId));
    }


    @Override
    public void toCompleteProfile() {
        Intent intent = new Intent(activity, CompleteProfileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void toNeed() {
        activity.startActivityForResult(NeedActivity.createIntentFor(activity), SEEK_NEED);
    }

    @Override
    public void toMain() {
        activity.startActivity(LauncherActivity.createIntentFor(activity));
        activity.onBackPressed();
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
    public void toProfile(Message message) {
        activity.startActivity(ProfileActivity.createIntentFor(activity, message));
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
    public void toMarketPlace() {
        Intent intentMarket = new Intent(Intent.ACTION_VIEW);
        PackageManager myAppPackage = activity.getPackageManager();
        intentMarket
                .setData(Uri
                        .parse("https://play.google.com/store/apps/details?id=com.donate.savelife"));

        if (intentMarket.resolveActivity(myAppPackage) != null) {
            activity.startActivity(intentMarket);
        }
    }

    @Override
    public void toMyNeeds(boolean finishActivity) {
        activity.startActivity(MyNeedsActivity.createIntentFor(activity));
        if (finishActivity){
            activity.onBackPressed();
        }
    }

    @Override
    public void toWelcome() {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
        activity.onBackPressed();
    }

    @Override
    public void toMapPicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            activity.startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toHonor(String needId) {
        activity.startActivity(HonorHeroesActivity.createIntentFor(activity, needId));
    }

    @Override
    public void toAddCity() {
        Intent intent = new Intent(activity, CountryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void startAppCentralService(Bundle bundle, String action) {
        CentralAppServiceIml.startActionSend(activity, bundle, action);
    }


    @Override
    public Activity getActivity() {
        return activity;
    }


}
