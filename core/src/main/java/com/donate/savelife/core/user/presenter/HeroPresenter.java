package com.donate.savelife.core.user.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HeroesDisplayer;
import com.donate.savelife.core.user.service.UserService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 14/12/16.
 */

public class HeroPresenter {

    private final HeroesDisplayer heroesDisplayer;
    private final UserService userService;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public HeroPresenter(
            HeroesDisplayer heroesDisplayer,
            UserService userService,
            Navigator navigator,
            ErrorLogger errorLogger,
            Analytics analytics
    ) {
        this.heroesDisplayer = heroesDisplayer;
        this.userService = userService;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
    }

    public void startPresenting(){
        heroesDisplayer.attach(heroInteractionListener);
        compositeSubscription.add(
                userService.getTopHeros()
                .subscribe(new Action1<DatabaseResult<Users>>() {
                    @Override
                    public void call(DatabaseResult<Users> usersDatabaseResult) {
                        if (usersDatabaseResult.isSuccess()){
                            heroesDisplayer.display(usersDatabaseResult.getData());
                        } else {
                            errorLogger.reportError(usersDatabaseResult.getFailure(), "Failed to fetch the heros");
                        }
                    }
                })
        );
    }

    public void stopPresenting(){
        heroesDisplayer.detach(null);
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
    }

    HeroesDisplayer.HeroInteractionListener heroInteractionListener = new HeroesDisplayer.HeroInteractionListener() {
        @Override
        public void onContentLoaded() {
            heroesDisplayer.displayContent();
        }

        @Override
        public void onError() {
            heroesDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            heroesDisplayer.displayEmpty();
        }
    };
}
