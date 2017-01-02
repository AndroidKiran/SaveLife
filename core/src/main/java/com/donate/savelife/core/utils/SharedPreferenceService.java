package com.donate.savelife.core.utils;

/**
 * Created by ravi on 12/09/16.
 */
public interface SharedPreferenceService {

    void setLoginUserPreference(String loginUser);

    String getLoginUserPreference();

    void setFirstFlowPreference(boolean firstFlowCompleted);

    boolean getFirstFlowValue();

    void setNotificationCity(String city);

    String getNotificationCity();

}
