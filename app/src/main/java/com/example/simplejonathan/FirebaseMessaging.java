package com.example.simplejonathan;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.liveperson.messaging.sdk.api.LivePerson;

public class FirebaseMessaging extends FirebaseMessagingService {

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Sends the message into the SDK
        LivePerson.handlePushMessage(this, remoteMessage.getData(), "64353961", true);
    }

}
