package com.donate.savelife.core.utils;


import android.os.Build;

/**
 * Created by ravi on 18/07/16.
 */
public class CoreUtils {
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
