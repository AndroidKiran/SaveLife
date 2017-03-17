package com.donate.savelife.apputils;

import android.content.Context;
import android.content.SharedPreferences;

import com.donate.savelife.SaveLifeApplication;
import com.donate.savelife.core.utils.SharedPreferenceService;


/**
 * Created by ravi on 12/08/16.
 */
public class AppPreferencesImpl implements SharedPreferenceService {

    private final static String PREFS_FILE = "app_prefs";
    private SharedPreferences prefs;

    private static final  String USER_DATA = "user_data";
    private static final  String REG_ID = "reg_id";
    private static final  String NOTIFICATION_ENABLED = "notification_enabled";
    public static final String VERSION_DEPRECATED = "version_deprecated";
    public static final String FIRST_TIME_HEROES_DIALOG = "first_time_heroes_dialog";


    public AppPreferencesImpl(Context context) {
        prefs = SaveLifeApplication.getSharedPreference(context);
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

    @Override
    public boolean isVersionDeprecated() {
        return prefs.getBoolean(VERSION_DEPRECATED, false);
    }

    @Override
    public void setVersionDeprecated(boolean deprecated) {
        prefs.edit().putBoolean(VERSION_DEPRECATED, deprecated).apply();
    }

    @Override
    public boolean isFirstTimeHeroesDialog() {
        return prefs.getBoolean(FIRST_TIME_HEROES_DIALOG, true);
    }

    @Override
    public void setFirstTimeHeroesDialog(boolean firstTime) {
        prefs.edit().putBoolean(FIRST_TIME_HEROES_DIALOG, firstTime).apply();
    }

    @Override
    public void clear() {
        prefs.edit().clear().apply();
    }


}
