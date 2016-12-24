package com.donate.savelife.apputils;


import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;

/**
 * Created by ravi on 18/07/16.
 */
public class Utils {

    public static boolean isEmptyList(ArrayList list){
        if (list != null && list.size() != 0){
            return false;
        }

        return true;
    }


    public static boolean isValidMobile(String mobileStr, String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(mobileStr, countryCode);
        } catch (NumberParseException e) {
            return false;
        }

        // Hack for indian mobile numbers
        if (phoneNumber.getCountryCode() == 91 &&
                String.valueOf(phoneNumber.getNationalNumber()).matches("[789]\\d{9}")) {
            return true;
        }

        return false;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
