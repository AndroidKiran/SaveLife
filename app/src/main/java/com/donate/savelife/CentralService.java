package com.donate.savelife;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.service.NotificationRegistrationService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.donate.savelife.apputils.UtilBundles.EXTRA_BUNDLE;


/**
 * Created by ravi on 09/01/17.
 */

public class CentralService extends IntentService {

    public static final String SEND_REG_ID_TO_SERVER = "com.donate.savelife.registration";
    private final SharedPreferenceService preference;
    private final ErrorLogger errorLogger;
    CompositeSubscription compositeSubscription;

    public CentralService() {
        super("CentralService");
        compositeSubscription = new CompositeSubscription();
        preference = Dependencies.INSTANCE.getPreference();
        errorLogger = Dependencies.INSTANCE.getErrorLogger();
    }

    public static void startActionSend(Context context, Bundle bundle, String action) {
        Intent intent = new Intent(context, CentralService.class);
        intent.setAction(action);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null){
            String action = intent.getAction();
            Bundle actionBundle = intent.getBundleExtra(EXTRA_BUNDLE);

            if (actionBundle != null && !TextUtils.isEmpty(action)){
                switch (action){
                    case SEND_REG_ID_TO_SERVER:
                        sendRegIdToServer(actionBundle);
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

    private void sendRegIdToServer(Bundle bundle){
        NotificationRegistrationService notificationRegistrationService = Dependencies.INSTANCE.getNotificationRegistrationService();
        String userId = bundle.getString(UtilBundles.USER_EXTRA, "");
        String regId = bundle.getString(UtilBundles.REG_EXTRA, "");
        compositeSubscription.add(
                notificationRegistrationService.writeRegistration(userId, regId)
                .subscribe(new Action1<DatabaseResult<String>>() {
                    @Override
                    public void call(DatabaseResult<String> stringDatabaseResult) {
                        if (stringDatabaseResult.isSuccess()){
                            preference.setRegistrationComplete();
                        } else {
                            errorLogger.reportError(stringDatabaseResult.getFailure(), "Registration failed");
                        }
                    }
                })

        );
    }
}
