package com.donate.savelife.core.launcher.database;

import com.donate.savelife.core.launcher.model.AppStatus;

import rx.Observable;

/**
 * Created by ravi on 02/02/17.
 */

public interface AppStatusDatabase {

    Observable<AppStatus> observerLatestStatus();
}
