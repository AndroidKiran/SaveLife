package com.donate.savelife.core.requirement.presenter;

import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.PreferenceService;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.requirement.displayer.NeedDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.service.NeedService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 19/11/16.
 */

public class NeedPresenter {


    private final NeedDisplayer needDisplayer;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final NeedService needService;
    private final PreferenceService preferenceService;
    private final GsonService gsonService;
    private CompositeSubscription subscriptions = new CompositeSubscription();

        public NeedPresenter(
                NeedDisplayer needDisplayer,
                Navigator navigator,
                NeedService needService,
                ErrorLogger errorLogger,
                Analytics analytics,
                PreferenceService preferenceService,
                GsonService gsonService
        ) {
            this.needDisplayer = needDisplayer;
            this.navigator = navigator;
            this.needService = needService;
            this.errorLogger = errorLogger;
            this.analytics = analytics;
            this.preferenceService = preferenceService;
            this.gsonService = gsonService;
        }

    public void startPresenting(){
        needDisplayer.attach(onNeedInteractionListener);
        needDisplayer.displayUser(gsonService.toUser(preferenceService.getLoginUserPreference()));
    }

    public void pausePresenting(){
        needDisplayer.dismissCountryDialog();
    }


    public void stopPresenting(){
        needDisplayer.detach(null);
        subscriptions.clear();
        subscriptions = new CompositeSubscription();
    }


    public void onFragmentInteractionListener(Country country){
        needDisplayer.displayCountry(country);
    }

    NeedDisplayer.OnNeedInteractionListener onNeedInteractionListener = new NeedDisplayer.OnNeedInteractionListener() {
        @Override
        public void onNeedPost(Need need) {
            needDisplayer.showProgress();
            subscriptions.add(
                    needService.writeNeed(need)
                    .subscribe(new Action1<DatabaseResult<Need>>() {
                        @Override
                        public void call(DatabaseResult<Need> needDatabaseResult) {
                            needDisplayer.dismissProgress();
                            if (needDatabaseResult.isSuccess()){
                                navigator.toParent();
                            } else {

                            }
                        }
                    })
            );
        }

        @Override
        public void onNavigateClick() {
            navigator.toParent();
        }
    };
}
