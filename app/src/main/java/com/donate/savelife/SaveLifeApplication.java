package com.donate.savelife;

import android.support.multidex.MultiDexApplication;

import com.donate.savelife.firebase.Dependencies;

import java.util.Locale;

/**
 * Created by ravi on 28/06/16.
 */
public class SaveLifeApplication extends MultiDexApplication {

    private static SaveLifeApplication sInstance;
    private Locale current;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Dependencies.INSTANCE.init(this);
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
}
