package com.example.manit;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.firebase.inappmessaging.display.internal.FiamImageLoader;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.lang.invoke.MethodHandleInfo;
import java.net.HttpURLConnection;
import java.net.URL;

public class notification extends FirebaseMessagingService {
    final String channelId="first";
     Bitmap bitmap;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String image=remoteMessage.getData().get("image");
        bitmap = getBitmapfromUrl(image);
       String url=remoteMessage.getData().get("url");
        shownotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),bitmap,url);
    }

    public void shownotification(String title, String body, Bitmap image,String ur){
        NotificationManagerCompat notificationManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "first",
                    "MANIT",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setShowBadge(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
        Uri sound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = NotificationManagerCompat.from(this);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(ur));
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, "first")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(image)
                .setContentTitle(title)
                .setColorized(true)
                 .setColor(ContextCompat.getColor(this,R.color.egypt))
                .setSound(sound)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(image))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
