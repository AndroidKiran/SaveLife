package com.donate.savelife;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.notifications.service.NotificationQueueService;
import com.donate.savelife.core.notifications.service.NotificationRegistrationService;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by ravi on 09/01/17.
 */

public class CentralAppServiceIml extends IntentService {

    private final SharedPreferenceService preference;
    private final ErrorLogger errorLogger;
    private final GsonService gsonService;
    CompositeSubscription compositeSubscription;

    public CentralAppServiceIml() {
        super("CentralService");
        compositeSubscription = new CompositeSubscription();
        preference = Dependencies.INSTANCE.getPreference();
        errorLogger = Dependencies.INSTANCE.getErrorLogger();
        gsonService = Dependencies.INSTANCE.getGsonService();
    }

    public static void startActionSend(Context context, Bundle bundle, String action) {
        Intent intent = new Intent(context, CentralAppServiceIml.class);
        intent.setAction(action);
        intent.putExtra(AppConstant.EXTRA_BUNDLE, bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            Bundle actionBundle = intent.getBundleExtra(AppConstant.EXTRA_BUNDLE);

            if (actionBundle != null && !TextUtils.isEmpty(action)) {
                switch (action) {
                    case AppConstant.ACTION_SEND_REG_ID_TO_SERVER:
                        sendRegIdToServer(actionBundle);
                        break;

                    case AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE:
                        addNotificationToQueue(actionBundle);
                        break;

                    case AppConstant.ACTION_ADD_CHAT_NOTIFICATION_TO_QUEUE:
                        addChatNotificationToQueue(actionBundle);
                        break;
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
        super.onDestroy();
    }

    private void sendRegIdToServer(Bundle bundle) {
        NotificationRegistrationService notificationRegistrationService = Dependencies.INSTANCE.getNotificationRegistrationService();
        String regId = bundle.getString(UtilBundles.REG_EXTRA, "");
        User user = gsonService.toUser(preference.getLoginUserPreference());
        compositeSubscription.add(
                notificationRegistrationService.writeRegistration(user.getId(), regId)
                        .subscribe(new Action1<DatabaseResult<String>>() {
                            @Override
                            public void call(DatabaseResult<String> stringDatabaseResult) {
                                if (stringDatabaseResult.isSuccess()) {
                                    preference.setRegistrationComplete();
                                } else {
                                    errorLogger.reportError(stringDatabaseResult.getFailure(), "Registration failed");
                                }
                            }
                        })

        );
    }

    private void addNotificationToQueue(Bundle bundle) {
        FCMRemoteMsg fcmRemoteMsg = (FCMRemoteMsg) bundle.getParcelable(AppConstant.NOFICATION_QUEUE_EXTRA);
        NotificationQueueService notificationQueueService = Dependencies.INSTANCE.getNotificationQueueService();
        compositeSubscription.add(
                notificationQueueService.writeIntoQueue(fcmRemoteMsg)
                        .subscribe(new Action1<DatabaseResult<FCMRemoteMsg>>() {
                            @Override
                            public void call(DatabaseResult<FCMRemoteMsg> notificationQueueDatabaseResult) {
                                if (!notificationQueueDatabaseResult.isSuccess()) {
                                    errorLogger.reportError(notificationQueueDatabaseResult.getFailure(), "RNotification push failed");
                                }
                            }
                        })
        );

    }

    private void addChatNotificationToQueue(final Bundle bundle) {
        Need need = bundle.getParcelable(AppConstant.NEED_EXTRA);
        NotificationRegistrationService notificationRegistrationService = Dependencies.INSTANCE.getNotificationRegistrationService();
        compositeSubscription.add(
                notificationRegistrationService.observeRegistrationsForNeed(need)
                        .subscribe(new Action1<DatabaseResult<String>>() {
                                       @Override
                                       public void call(DatabaseResult<String> stringDatabaseResult) {
                                           if (stringDatabaseResult.isSuccess()) {
                                               if (!TextUtils.isEmpty(stringDatabaseResult.getData())) {
                                                   FCMRemoteMsg fcmRemoteMsg = (FCMRemoteMsg) bundle.getParcelable(AppConstant.NOFICATION_QUEUE_EXTRA);
                                                   fcmRemoteMsg.setRegistration_ids(stringDatabaseResult.getData());
                                                   bundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
                                                   addNotificationToQueue(bundle);
                                               }
                                           }
                                       }
                                   }
                        )

        );
    }
}
