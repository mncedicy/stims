package com.mncedicy.stims.api.Services;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public PushNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotificationToDevice(String deviceToken, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return firebaseMessaging.send(message);
    }

    public String sendNotificationToTopic(String topic, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return firebaseMessaging.send(message);
    }
}