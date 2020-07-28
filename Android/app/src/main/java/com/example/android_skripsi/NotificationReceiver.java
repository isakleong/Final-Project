package com.example.android_skripsi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.Map;

public class NotificationReceiver {
    private static int count = 0;
    private static Context mcontext;

    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        intent = new Intent(context, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pushnotif", "yes");
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0 /* Requst code */,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT);
        String channelId = "ha?";
        Uri defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.unnamed)
                        .setContentTitle(intent.getStringExtra("messageTitle"))
                        .setContentText(intent.getStringExtra("messageBody"))
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#01AB6D"))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(count, notificationBuilder.build());
        count++;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, "Channel title", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(intent.getStringExtra("messageBody"));
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
        }
    }

//    private Intent setNotificationRoute(Map<String, String> messageBody) {
//        Intent intent = null;
//        String type = messageBody.get("id_aspirasi");
//        if (type != null) {
//            intent = new Intent(mcontext, Main2Activity.class);
//        }
//        return intent;
//    }
}
