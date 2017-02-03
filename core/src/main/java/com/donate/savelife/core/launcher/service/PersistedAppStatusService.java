package com.donate.savelife.core.launcher.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.launcher.database.AppStatusDatabase;
import com.donate.savelife.core.launcher.model.AppStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 03/02/17.
 */

public class PersistedAppStatusService implements AppStatusService {

    private final AppStatusDatabase appStatusDatabase;

    public PersistedAppStatusService(AppStatusDatabase appStatusDatabase){
        this.appStatusDatabase = appStatusDatabase;
    }

    @Override
    public Observable<DatabaseResult<AppStatus>> observeLatestStatus() {
        return appStatusDatabase.observerLatestStatus()
                .map(new Func1<AppStatus, DatabaseResult<AppStatus>>() {
                    @Override
                    public DatabaseResult<AppStatus> call(AppStatus appStatus) {
                        return new DatabaseResult<AppStatus>(appStatus);
                    }
                })
                .onErrorReturn(DatabaseResult.<AppStatus>errorAsDatabaseResult());
    }
}
