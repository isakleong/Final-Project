package com.example.android_skripsi;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.zxing.client.result.ParsedResult;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static protected int id = 0;
    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String message_title = remoteMessage.getNotification().getTitle();
            String message_body = remoteMessage.getNotification().getBody();

            Map<String, String> message_data = remoteMessage.getData();
//            String idAspirasi = message_data.get("id_aspirasi").toString();
//            String idStatusAspirasi = message_data.get("id_status_aspirasi").toString();

//            sendPushNotification(message_body, message_title, idAspirasi, idStatusAspirasi);
            sendPushNotification(remoteMessage);

//            Log.d("mako", "B Message data payload: " + remoteMessage.getData());
//            Log.d("mako", "B Message data notif: " + message_data.get("title"));
//            Log.d("mako", "C Message Notification Body: " + remoteMessage.getNotification().getBody());


            if (/* Check if data needs to be processed by long-running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();

            } else {
                // Handle message within 10 seconds
//                handleNow();

            }

        }

//        if(!ActivityApplication.isActivityVisible()){
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//            mBuilder.setContentTitle(getString(R.string.app_name))
//                    .setSmallIcon(R.drawable.logo);
//
//
//        }


        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("mako", "C Message Notification Body: " + remoteMessage.getNotification().getBody());
//            String message_body = remoteMessage.getNotification().getBody();
//            String message_title = remoteMessage.getNotification().getTitle();
////            String idStatusAspirasi = remoteMessage.getData().get("id_status_aspirasi");
////            String contentNamaBiroUnit = remoteMessage.getData().get("contentNamaBiroUnit");
////            String contentTeksAspirasi = remoteMessage.getData().get("contentTeksAspirasi");
////            String contentPhotoAvatarBiroUnit = remoteMessage.getData().get("contentPhotoAvatarBiroUnit");
////            String contentTglAspirasi = remoteMessage.getData().get("contentTglAspirasi");
////
////            String arrFotoBiroUnit[]   = contentPhotoAvatarBiroUnit.split(",");
//
////            //get result from encoded_image
////            byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
////            Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
////
////            ByteArrayOutputStream stream = new ByteArrayOutputStream();
////            decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
////            byte[] byteArray = stream.toByteArray();
//
//            sendPushNotification(message_body, message_title);
//        }
    }

    private void sendPushNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0 /* Requst code */,
                        setNotificationRoute(remoteMessage),
                        PendingIntent.FLAG_ONE_SHOT);
        String channelId = "ha?";
        Uri defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager)
                        this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.unnamed)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#01AB6D"))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, remoteMessage.getNotification().getTitle(), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(remoteMessage.getNotification().getBody());
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("E", "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }



    private Intent setNotificationRoute(RemoteMessage remoteMessage) {
        Intent intent = null;

        Map<String, String> message_data = remoteMessage.getData();
        String idAspirasi = message_data.get("id_aspirasi");
        String statusAspirasi = message_data.get("status_aspirasi");

        Bundle bundle = new Bundle();
//        String idAspirasi = remoteMessage.getData().get("id_aspirasi");
//        String statusAspirasi = remoteMessage.getData().get("status_aspirasi");
        bundle.putString("id_aspirasi", idAspirasi);
        bundle.putString("status_aspirasi", statusAspirasi);

        intent = new Intent(this, SplashScreen.class);
        intent.putExtras(bundle);

        return intent;
    }

}