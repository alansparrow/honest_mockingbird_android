package com.trungbao.honestmockingbird.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by baotrungtn on 9/18/17.
 */

public class NewsFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "FirebaseFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "My Token: " + FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Body: " + remoteMessage.getNotification().getBody());

    }
}
