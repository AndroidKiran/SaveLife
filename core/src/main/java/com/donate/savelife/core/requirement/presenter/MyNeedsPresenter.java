package com.donate.savelife.core.requirement.presenter;

import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 22/11/16.
 */

public class MyNeedsPresenter {

    private final NeedsDisplayer needsDisplayer;
    private final NeedService needService;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final Navigator navigator;
    private final User user;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MyNeedsPresenter(NeedsDisplayer needsDisplayer, NeedService needService,
                            ErrorLogger errorLogger, Analytics analytics,
                            Navigator navigator, SharedPreferenceService preferenceService,
                            GsonService gsonService) {
        this.needsDisplayer = needsDisplayer;
        this.needService = needService;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.navigator = navigator;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
    }

    public void startPresenting() {
        needsDisplayer.attach(needInteractionListener);
        compositeSubscription.add(
                needService.observeMyNeeds(user)
                        .subscribe(new Action1<DatabaseResult<Need>>() {
                            @Override
                            public void call(DatabaseResult<Need> needDatabaseResult) {
                                if (needDatabaseResult.isSuccess()) {
                                    needsDisplayer.display(needDatabaseResult.getData(), user);
                                } else {
                                    needsDisplayer.displayEmpty();
                                    errorLogger.reportError(needDatabaseResult.getFailure(), "Initial fetch needs failed");
                                }
                            }
                        })
        );
    }


    public void stopPresenting() {
        needsDisplayer.detach(null);
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
    }

    NeedsDisplayer.NeedInteractionListener needInteractionListener = new NeedsDisplayer.NeedInteractionListener() {
        @Override
        public void onNeedSelected(Need need) {
            navigator.toChat(need.getId());
            Bundle listItemBundle = new Bundle();
            listItemBundle.putString(Analytics.PARAM_NEED_ID, need.getId());
            listItemBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_CHAT);
            analytics.trackEventOnClick(listItemBundle);
        }

        @Override
        public void onContentLoaded() {
            needsDisplayer.displayContent();
        }

        @Override
        public void onError() {
            needsDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            needsDisplayer.displayEmpty();
        }
    };
}
