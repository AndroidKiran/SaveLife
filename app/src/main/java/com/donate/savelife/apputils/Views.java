package com.donate.savelife.apputils;

import android.app.Activity;
import android.view.View;

/**
 * Created by ravi on 26/12/16.
 */

public final class Views {

    private Views() {
    }

    /**
     * Simpler version of {@link View#findViewById(int)} which infers the target type.
     */
    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T extends View> T findById(View view, int id) {
        return (T) view.findViewById(id);
    }

    /**
     * Simpler version of {@link Activity#findViewById(int)} which infers the target type.
     */
    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T extends View> T findById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

}
