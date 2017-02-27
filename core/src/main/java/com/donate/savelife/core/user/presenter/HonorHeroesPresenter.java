package com.donate.savelife.core.user.presenter;

import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HonorHeroesDisplayer;
import com.donate.savelife.core.user.service.HeroService;
import com.donate.savelife.core.utils.AppConstant;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 26/02/17.
 */

public class HonorHeroesPresenter {
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    private final HeroService heroService;
    private final Navigator navigator;
    private final HonorHeroesDisplayer honorHeroesDisplayer;
    private final String needId;
    private final Users users;
    private final Analytics analytics;
    private final ErrorLogger errorLogger;

    public HonorHeroesPresenter(HeroService heroService, Navigator navigator, HonorHeroesDisplayer honorHeroesDisplayer,
                                Bundle bundle, Analytics analytics, ErrorLogger errorLogger){
        this.heroService = heroService;
        this.navigator = navigator;
        this.honorHeroesDisplayer = honorHeroesDisplayer;
        this.needId = bundle.getString(AppConstant.NEED_EXTRA, "");
        this.users = bundle.getParcelable(AppConstant.USER_EXTRA);
        this.analytics = analytics;
        this.errorLogger = errorLogger;
    }

    public void startPresenting(){
        honorHeroesDisplayer.attach(honorHeroesInteractionListener);
        honorHeroesDisplayer.display(users);
    }


    public void onSavedInstanceStatePresneter(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(AppConstant.USER_EXTRA, honorHeroesDisplayer.getUsers());
    }

    public void stopPresenting(){
        honorHeroesDisplayer.detach(null);
        compositeSubscription.clear();
    }


    private final HonorHeroesDisplayer.HonorHeroesInteractionListener honorHeroesInteractionListener = new HonorHeroesDisplayer.HonorHeroesInteractionListener() {
        @Override
        public void onHeroesHonored(User user, int value) {
            Message message = new Message();
            message.setNeedId(needId);
            message.setUserID(user.getId());
            compositeSubscription.add(
                    heroService.honorHero(message, value)
                    .subscribe(new Action1<DatabaseResult<User>>() {
                        @Override
                        public void call(DatabaseResult<User> userDatabaseResult) {
                            if (userDatabaseResult.isSuccess()){

                            } else {
                                errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to honor");
                            }
                        }
                    })
            );
        }
    };


}
