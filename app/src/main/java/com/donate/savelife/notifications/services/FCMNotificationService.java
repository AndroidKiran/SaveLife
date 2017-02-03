package com.donate.savelife.notifications.services;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.utils.AppConstant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FCMNotificationService extends FirebaseMessagingService {

    private static final String TAG = FCMNotificationService.class.getSimpleName();

    public static NotificationReceiverListener notificationReceiverListener;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (notificationReceiverListener != null){
            FCMRemoteMsg fcmRemoteMsg = new FCMRemoteMsg();

            Bundle notificationBundle = new Bundle();

            FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
            RemoteMessage.Notification remoteMessageNotification= remoteMessage.getNotification();
            notification.setBody(remoteMessageNotification.getBody());
            notification.setTitle(remoteMessageNotification.getTitle());
            fcmRemoteMsg.setNotification(notification);

            ArrayMap<String, String> remoteMsgData = (ArrayMap<String, String>) remoteMessage.getData();
            if (remoteMsgData != null && remoteMsgData.size() > 0){
                FCMRemoteMsg.Data data = new FCMRemoteMsg.Data();
                if (remoteMsgData.containsKey(AppConstant.NEED_ID_EXTRA)){
                    data.setNeed_id(remoteMsgData.get(AppConstant.NEED_ID_EXTRA));
                }

                if (remoteMsgData.containsKey(AppConstant.CLICK_ACTION_EXTRA)){
                    data.setClick_action(remoteMsgData.get(AppConstant.CLICK_ACTION_EXTRA));
                }

                if (data != null){
                    fcmRemoteMsg.setData(data);
                }
            }

            notificationBundle.putParcelable(AppConstant.FCM_REMOTE_MSG_EXTRA, fcmRemoteMsg);
            notificationReceiverListener.onNotificationReceived(notificationBundle);
        }
    }


    public interface NotificationReceiverListener {
        void onNotificationReceived(Bundle bundle);
    }
}
