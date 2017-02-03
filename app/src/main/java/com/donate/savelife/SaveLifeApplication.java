package com.donate.savelife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.donate.savelife.apputils.ConnectivityReceiver;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.notifications.services.FCMNotificationService;

import java.util.Locale;

/**
 * Created by ravi on 28/06/16.
 */
public class SaveLifeApplication extends MultiDexApplication {

    public static int APP_VERSION_CODE;
    public static String APP_VERSION;
    private static SaveLifeApplication sInstance;
    private static SharedPreferences mSharedPreference;
    private Locale current;
    private Intent indexIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Dependencies.INSTANCE.init(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        try {
            final PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            APP_VERSION = info.versionName;
            APP_VERSION_CODE = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            APP_VERSION = "Unknown";
            APP_VERSION_CODE = 0;
        }
    }

    public static synchronized SaveLifeApplication getInstance() {
        return sInstance;
    }

    public Locale getLocale() {
        if (current == null) {
            current = Locale.US;
        }
        return current;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void setNotificationListener(FCMNotificationService.NotificationReceiverListener listener) {
        FCMNotificationService.notificationReceiverListener = listener;
    }

    public static SharedPreferences getSharedPreference(Context context) {
        if (mSharedPreference == null) {
            mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreference;
    }

    public Intent getIndexIntent() {
        return indexIntent;
    }

    public void setIndexIntent(Intent indexIntent) {
        this.indexIntent = indexIntent;
    }
}
