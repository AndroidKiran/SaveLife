package com.donate.savelife.core.user.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HerosDisplayer;
import com.donate.savelife.core.user.service.UserService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 14/12/16.
 */

public class HeroPresenter {

    private final HerosDisplayer herosDisplayer;
    private final UserService userService;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public HeroPresenter(
            HerosDisplayer herosDisplayer,
            UserService userService,
            Navigator navigator,
            ErrorLogger errorLogger,
            Analytics analytics
    ) {
        this.herosDisplayer = herosDisplayer;
        this.userService = userService;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
    }

    public void startPresenting(){
        herosDisplayer.attach(heroInteractionListener);
        compositeSubscription.add(
                userService.getTopHeros()
                .subscribe(new Action1<DatabaseResult<Users>>() {
                    @Override
                    public void call(DatabaseResult<Users> usersDatabaseResult) {
                        if (usersDatabaseResult.isSuccess()){
                            herosDisplayer.display(usersDatabaseResult.getData());
                        } else {
                            errorLogger.reportError(usersDatabaseResult.getFailure(), "Failed to fetch the heros");
                        }
                    }
                })
        );
    }

    public void stopPresenting(){
        this.heroInteractionListener = null;
        herosDisplayer.detach(heroInteractionListener);
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
    }

    HerosDisplayer.HeroInteractionListener heroInteractionListener = new HerosDisplayer.HeroInteractionListener() {
        @Override
        public void onContentLoaded() {
            herosDisplayer.displayContent();
        }

        @Override
        public void onError() {
            herosDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            herosDisplayer.displayEmpty();
        }
    };
}
