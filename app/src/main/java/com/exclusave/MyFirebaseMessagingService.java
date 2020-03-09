package com.exclusave;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.exclusave.Activities.MainActivity;
import com.exclusave.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    Context context;
    public static boolean check;
    private static final String PRIMARY_NOTIF_CHANNEL = "2";

//    int checking;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e(TAG, "From: " + remoteMessage.getData());

        //Check if the message contains data
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data: " + remoteMessage.getData().get("message"));
            sendNotification(remoteMessage.getData().get("message"));

//            Intent intent = new Intent(this, NotificationsActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            String channelId = "Default";
//            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
//                    .setSmallIcon(R.mipmap.app_icon1)
//                    .setContentTitle(remoteMessage.getData().get("message"))
//                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
//                manager.createNotificationChannel(channel);
//            }
//            manager.notify(0, builder.build());
        }


        //Check if the message contains notification

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());


        }



    }


    private void sendNotification(String body) {

//        Intent intent = new Intent(this, NotificationsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1/*Request code*/, intent, 0);
        //Set sound of notification

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel chan1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(
                    PRIMARY_NOTIF_CHANNEL,
                    "default",
                    NotificationManager.IMPORTANCE_HIGH);

            chan1.setLightColor(Color.TRANSPARENT);
//            chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager1.createNotificationChannel(chan1);
            Notification notification = new NotificationCompat.Builder(this, PRIMARY_NOTIF_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();

//            startForeground(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);
            notificationManager1.notify(0, notification);

        } else {

//            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, notifiBuilder.build());

        }
    }

}
