package com.donate.savelife.core.utils;

/**
 * Created by ravi on 12/09/16.
 */
public interface SharedPreferenceService {

    void setLoginUserPreference(String loginUser);

    String getLoginUserPreference();

    void setRegistrationId(String registrationId);

    String getRegistrationId();

    void setRegistrationComplete();

    boolean isRegistrationComplete();

    void setNotificationEnabled(boolean notificationEnabled);

    boolean isNotificationEnabled();

    boolean isVersionDeprecated();

    void setVersionDeprecated(boolean deprecated);
}
