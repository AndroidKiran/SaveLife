package com.donate.savelife.core.launcher.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.launcher.model.AppStatus;

import rx.Observable;

/**
 * Created by ravi on 02/02/17.
 */

public interface AppStatusService {

    Observable<DatabaseResult<AppStatus>> observeLatestStatus();
}
