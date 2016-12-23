package com.donate.savelife.core.requirement.presenter;

import android.os.Bundle;

import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.PreferenceService;
import com.donate.savelife.core.utils.UtilBundles;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.user.data.model.User;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 22/11/16.
 */

public class NeedsPresenter {

    private final NeedsDisplayer needsDisplayer;
    private final NeedService needService;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final Navigator navigator;
    private final User user;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public NeedsPresenter(NeedsDisplayer needsDisplayer, NeedService needService,
                          ErrorLogger errorLogger, Analytics analytics,
                          Navigator navigator, PreferenceService preferenceService,
                          GsonService gsonService) {
        this.needsDisplayer = needsDisplayer;
        this.needService = needService;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.navigator = navigator;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
    }

    public void initPresenter() {
        compositeSubscription.add(
                needService.observeNeedsWithUsers(user)
                        .subscribe(new Action1<DatabaseResult<Needs>>() {
                            @Override
                            public void call(DatabaseResult<Needs> needsDatabaseResult) {
                                if (needsDatabaseResult.isSuccess()) {
                                    needsDisplayer.display(needsDatabaseResult.getData());
                                } else {
                                    needsDisplayer.displayEmpty();
                                    errorLogger.reportError(needsDatabaseResult.getFailure(), "Initial fetch needs failed");
                                }
                            }
                        })
        );
    }

    public void startPresenting(){
        needsDisplayer.attach(needInteractionListener);
    }


    public void stopPresenting() {
        needsDisplayer.detach(null);
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
    }

    NeedsDisplayer.NeedInteractionListener needInteractionListener = new NeedsDisplayer.NeedInteractionListener() {
        @Override
        public void onNeedSelected(Need need) {
            analytics.trackSelectNeed(need.getUserID(), need.getAddress());
            navigator.toChat(need);
        }

        @Override
        public void onLoadMore(Need need) {
            compositeSubscription.add(
                    needService.observeMoreNeedsWithUsers(user, need)
                            .subscribe(new Action1<DatabaseResult<Needs>>() {
                                @Override
                                public void call(DatabaseResult<Needs> needsDatabaseResult) {
                                    if (needsDatabaseResult.isSuccess()) {
                                        needsDisplayer.displayMore(needsDatabaseResult.getData());
                                    } else {
                                        errorLogger.reportError(needsDatabaseResult.getFailure(), "load more fetch needs failed");
                                    }
                                }
                            })
            );
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


    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(UtilBundles.SAVED_LIST, needsDisplayer.getNeeds());
        outState.putParcelable(UtilBundles.SAVED_LAST_ITEM, needsDisplayer.getlastNeedItem());
    }

    private Func1<Needs, DatabaseResult<Needs>> asDatabaseResult() {
        return new Func1<Needs, DatabaseResult<Needs>>() {
            @Override
            public DatabaseResult<Needs> call(Needs needs) {
                return new DatabaseResult<Needs>(needs);
            }
        };
    }

    public void onRestoreInstanceState(Bundle outState) {
        Need need = (Need) outState.getParcelable(UtilBundles.SAVED_LAST_ITEM);
        Needs needs = (Needs) outState.getParcelable(UtilBundles.SAVED_LIST);
        if (null != need) {
            needsDisplayer.setLastNeedItem(need);
        }

        if (null != needs) {
            needsDisplayer.attach(needInteractionListener);
            compositeSubscription.add(
                    Observable.just(needs)
                            .map(asDatabaseResult())
                            .subscribe(new Action1<DatabaseResult<Needs>>() {
                                @Override
                                public void call(DatabaseResult<Needs> needsDatabaseResult) {
                                    if (needsDatabaseResult.isSuccess()) {
                                        needsDisplayer.display(needsDatabaseResult.getData());
                                    } else {
                                        errorLogger.reportError(needsDatabaseResult.getFailure(), "savedinstancestate fetch needs failed");
                                        needsDisplayer.displayError();
                                    }
                                }
                            })
            );
        } else {
            initPresenter();
        }


    }
}
