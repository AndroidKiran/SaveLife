package com.donate.savelife.apputils;

import android.content.Context;
import android.content.SharedPreferences;

import com.donate.savelife.core.utils.SharedPreferenceService;


/**
 * Created by ravi on 12/08/16.
 */
public class AppPreferencesImpl implements SharedPreferenceService {

    private final static String PREFS_FILE = "app_prefs";
    private SharedPreferences prefs;

    private final static String USER_DATA = "user_data";
    private final static String REG_ID = "reg_id";
    private final static String NOTIFICATION_ENABLED = "notification_enabled";


    public AppPreferencesImpl(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void setLoginUserPreference(String loginUser) {
        prefs.edit().putString(USER_DATA, loginUser).apply();
    }

    @Override
    public String getLoginUserPreference() {
        return prefs.getString(USER_DATA, "");
    }

    @Override
    public void setRegistrationId(String registrationId) {
        prefs.edit().putString(REG_ID, registrationId).apply();
    }

    @Override
    public String getRegistrationId() {
        return prefs.getString(REG_ID, "");
    }

    @Override
    public void setRegistrationComplete() {
        prefs.edit().putBoolean(UtilBundles.REG_COMPLETE_EXTRA, true).apply();
    }

    @Override
    public boolean isRegistrationComplete() {
        return prefs.getBoolean(UtilBundles.REG_COMPLETE_EXTRA, false);
    }

    @Override
    public void setNotificationEnabled(boolean notificationEnabled) {
        prefs.edit().putBoolean(NOTIFICATION_ENABLED, notificationEnabled).apply();
    }

    @Override
    public boolean isNotificationEnabled() {
        return prefs.getBoolean(NOTIFICATION_ENABLED, true);
    }

}
