package com.intl.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.intl.R;
import com.intl.utils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TAG = FCMMessagingService.class.getSimpleName();

    public FCMMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String bodyFcm;
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            if (data == null) {
                return;
            }

            bodyFcm = (String)data.get("title");
            final String body = (String)data.get("message");
            final String campaign = (String)data.get("campaign_name");
            String mediaUrl = (String)data.get("media_url");
            String mediaType = (String)data.get("media_type");
            if (mediaType != null && mediaType.contains("image") && !TextUtils.isEmpty(mediaUrl)) {
                final String finalBodyFcm = bodyFcm;
                Glide.with(this).asBitmap().load(mediaUrl).into(new CustomTarget<Bitmap>() {
                    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Utils.generateNotification(FCMMessagingService.this, finalBodyFcm, body, resource, campaign);
                    }

                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            } else {
                this.generateNotification(bodyFcm, body, (Bitmap)null, campaign);
            }
        }

        if (remoteMessage.getNotification() != null) {
            String titleFcm = remoteMessage.getNotification().getTitle();
            bodyFcm = remoteMessage.getNotification().getBody();
            if (!TextUtils.isEmpty(titleFcm) || !TextUtils.isEmpty(bodyFcm)) {
                this.generateNotification(titleFcm, bodyFcm, (Bitmap)null, (String)null);
            }
        }

    }

    public void onNewToken(String token) {
        AppsFlyerLib.getInstance().updateServerUninstallToken(this, token);
        this.sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        FCMRequest.sendRegistrationToServer(this, token);
    }

    @SuppressLint("WrongConstant")
    private void generateNotification(String title, String messageBody, Bitmap bitmap, String campaign) {
        Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());

        assert intent != null;

        intent.putExtra("open_notifi", true);
        if (!TextUtils.isEmpty(campaign)) {
            intent.putExtra("campaign", campaign);
        }

        intent.addFlags(603979776);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 134217728);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(messageBody);
        String channelId = this.getPackageName();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(2);
        NotificationCompat.Builder notificationBuilder = (new NotificationCompat.Builder(this, channelId)).setLargeIcon(BitmapFactory.decodeResource(this.getResources(), this.getApplicationInfo().icon)).setContentTitle(title).setContentText(messageBody).setStyle(bigTextStyle).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
        if (bitmap != null) {
            notificationBuilder.setStyle((new NotificationCompat.BigPictureStyle()).bigPicture(bitmap));
        }

        NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(channelId, "Soha", 3);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    class loadBitmap extends AsyncTask<String, Void, Bitmap> {
        String title;
        String content;
        String campaign;

        loadBitmap(String title, String content, String campaign) {
            this.title = title;
            this.content = content;
            this.campaign = campaign;
        }

        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                URL url = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            FCMMessagingService.this.generateNotification(this.title, this.content, bitmap, this.campaign);
        }
    }
}
