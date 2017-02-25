package com.donate.savelife.core.requirement.presenter;

import android.os.Bundle;
import android.os.Handler;

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
    private static final int HANDLER_SLEEP_TIME = 1000;

    private final NeedDisplayer needDisplayer;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final NeedService needService;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final User user;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private Runnable mRunnable;
    private Handler mHandler;

    private boolean success;

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

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
            mRunnable = null;
        }

        if (success){
            navigator.toParent();
        }
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
                                success = true;
                                needDisplayer.displaySuccessLayout();
                                pushToNotifiationQueue(need);
                                mRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        success = false;
                                        navigator.toMyNeeds(true);
                                    }
                                };

                                mHandler = new Handler();
                                mHandler.postDelayed(mRunnable, HANDLER_SLEEP_TIME);
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
        String topic = need.getCity().toLowerCase()+need.getCountry().toLowerCase();
        FCMRemoteMsg fcmRemoteMsg = new FCMRemoteMsg();
        fcmRemoteMsg.setTo(AppConstant.TOPIC + topic);
        fcmRemoteMsg.setCollapse_key(topic);
        fcmRemoteMsg.setPriority("high");
        fcmRemoteMsg.setContent_available(true);

        FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(user.getName()+ " required ");
        stringBuilder.append(need.getBloodGroup() + " on urgent basis");
        notification.setBody(stringBuilder.toString());

        FCMRemoteMsg.Data data = new FCMRemoteMsg.Data();
        data.setClick_action(AppConstant.CLICK_ACTION_HOME);

        fcmRemoteMsg.setNotification(notification);
        fcmRemoteMsg.setData(data);

        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
        navigator.startAppCentralService(notificationQueueBundle, AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE);
    }
}
