package com.donate.savelife;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.donate.savelife.apputils.ConnectivityReceiver;
import com.donate.savelife.firebase.Dependencies;

import java.util.Locale;

/**
 * Created by ravi on 28/06/16.
 */
public class SaveLifeApplication extends MultiDexApplication {

    public static int APP_VERSION_CODE;
    public static String APP_VERSION;
    private static SaveLifeApplication sInstance;
    private Locale current;


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
}
