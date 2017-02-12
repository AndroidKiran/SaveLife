package com.donate.savelife.core.utils;


import android.os.Build;

import com.donate.savelife.core.chats.model.Map;

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

    public static class ContentType {
        public static final int TXT = 1;
        public static final int MAP = 2;
    }

    public static String local(Map map){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+map.getLatitude()+","+map.getLongitude()+"&zoom=18&size=280x280&markers=color:red|"+map.getLatitude()+","+map.getLongitude();
    }

}
