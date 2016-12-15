package com.donate.savelife.apputils;

import android.content.Context;
import android.content.SharedPreferences;

import com.donate.savelife.core.utils.PreferenceService;


/**
 * Created by ravi on 12/08/16.
 */
public class AppPreferencesImpl implements PreferenceService {

    private final static String PREFS_FILE = "app_prefs";
    private SharedPreferences prefs;

    private final static String USER_DATA = "user_data";
    private final static String FLAT_ONBOARDING_DONE = "flat_onboarding";


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
    public void setFirstFlowPreference(boolean firstFlowCompleted) {
        prefs.edit().putBoolean(FLAT_ONBOARDING_DONE, firstFlowCompleted).apply();
    }

    @Override
    public boolean getFirstFlowValue() {
        return prefs.getBoolean(FLAT_ONBOARDING_DONE, false);
    }

    public SharedPreferences getPreference(){
        return prefs;
    }
}
