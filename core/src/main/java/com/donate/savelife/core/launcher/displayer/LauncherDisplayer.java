package com.donate.savelife.core.launcher.displayer;

import com.donate.savelife.core.launcher.model.AppStatus;

/**
 * Created by ravi on 02/02/17.
 */

public interface LauncherDisplayer {

    boolean isVersionDeprecated();

    void display(AppStatus appStatus);

    void attach(LauncherInteractionListener launcherInteractionListener);

    void detach(LauncherInteractionListener launcherInteractionListener);

    public interface LauncherInteractionListener {
        void onUpdate();

        void onRetry();
    }
}
