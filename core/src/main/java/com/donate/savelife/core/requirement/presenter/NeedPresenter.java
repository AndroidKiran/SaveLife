package com.donate.savelife.core.requirement.presenter;

import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.requirement.displayer.NeedDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 19/11/16.
 */

public class NeedPresenter {

    public static final String TAG = NeedPresenter.class.getSimpleName();
    private final NeedDisplayer needDisplayer;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final NeedService needService;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final User user;
    private CompositeSubscription subscriptions = new CompositeSubscription();

        public NeedPresenter(
                NeedDisplayer needDisplayer,
                Navigator navigator,
                NeedService needService,
                ErrorLogger errorLogger,
                Analytics analytics,
                SharedPreferenceService preferenceService,
                GsonService gsonService
        ) {
            this.needDisplayer = needDisplayer;
            this.navigator = navigator;
            this.needService = needService;
            this.errorLogger = errorLogger;
            this.analytics = analytics;
            this.preferenceService = preferenceService;
            this.gsonService = gsonService;
            this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
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
        public void onNeedPost(final Need need) {
            needDisplayer.showProgress();
            subscriptions.add(
                    needService.writeNeed(need)
                    .subscribe(new Action1<DatabaseResult<Need>>() {
                        @Override
                        public void call(DatabaseResult<Need> needDatabaseResult) {
                            needDisplayer.dismissProgress();
                            if (needDatabaseResult.isSuccess()){
                                pushToNotifiationQueue(need);
                                navigator.toMyNeeds();
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

    private void pushToNotifiationQueue(Need need) {
        Bundle notificationQueueBundle = new Bundle();
        FCMRemoteMsg fcmRemoteMsg = new FCMRemoteMsg();
        fcmRemoteMsg.setTo(need.getCity()+need.getCountry());
        fcmRemoteMsg.setCollapse_key(need.getCity()+need.getCountry());
        fcmRemoteMsg.setPriority("high");
        FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
        notification.setTitle(user.getName());
        notification.setBody(need.getBloodGroup() + "required on urgent basis");
        fcmRemoteMsg.setNotification(notification);
        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
        navigator.startAppCentralService(notificationQueueBundle, AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE);
    }
}
