package com.jobesk.yea;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jobesk.yea.AttendeArea.Activities.NotificationActivity;
import com.jobesk.yea.AttendeArea.Activities.SplashActivity;
import com.jobesk.yea.AttendeArea.Chat.ClientGetConversation;

import com.jobesk.yea.Utils.GlobalClass;

import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private SharedPreferences sharedpreferences;
    private Intent intent = null;
    private String message;
    private String getBody;
    private int counter;
    private String notificationType;
    private String title;


    private String NOTIFICATION_CHANNEL_ID_1="1";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("remoteMessage", "FROM:" + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("remoteMessage", "Message data: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("remoteMessage", "Mesage body:" + remoteMessage.getNotification().getBody());
        }

//        getBody = remoteMessage.getNotification().getBody();


        getBody = remoteMessage.getData().get("body");


        String userID = GlobalClass.getPref("userID", getApplicationContext());
        if (!userID.equalsIgnoreCase("")) {

//                                  notificationType =1 for Admin
//                                notificationType =2 Like on your post
//                                notificationType =3 comment on your post
//                                notificationType =4 Like on your comment
//                                  notificationType =5 Message

            if (isAppIsInBackground(getApplicationContext()) == true) {
                int notiCheck = 0;
                Log.d("appStatus", "inBackground");
                if (getBody.contains("message")) {

                    // go for chat

                    notiCheck = 5;
                } else {
                    // go for noti activity

                    notiCheck = 1;
                }

                intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("notiValueStartCheck", String.valueOf(notiCheck));
                intent.putExtras(bundle);


            } else {
                Log.d("appStatus", "inforeGround");


                if (getBody.contains("message")) {

//                    // go for chat


                    intent = new Intent(getApplicationContext(), ClientGetConversation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
//                    // go for noti activity


                    intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }


            }


            CreateNoti(remoteMessage);

            String notiCounter = GlobalClass.getPref("notiCounter", getApplicationContext());

            if (notiCounter.equalsIgnoreCase("")) {
                counter = 1;
                GlobalClass.putPref("notiCounter", String.valueOf(counter), getApplicationContext());
            } else {
                counter = Integer.valueOf(notiCounter) + 1;
                GlobalClass.putPref("notiCounter", String.valueOf(counter), getApplicationContext());
            }

        }

    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    private void CreateNoti(RemoteMessage mRemoteMsg) {


        Random r = new Random();
        int randomNo = r.nextInt(9999999 - 0) + 0;


        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomNo, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {



            String channelName = getApplicationContext().getResources().getString(R.string.miscellaneous);

            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID_1,channelName, NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel


            String channelDesCriptions = getApplicationContext().getResources().getString(R.string.all_notifications);
            notificationChannel.setDescription(channelDesCriptions);
            notificationChannel.enableLights(true);

            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500,
                    1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_1)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.noti_48)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.app_name))
                .setContentText(getBody);

        builder.setContentIntent(pendingIntent);
        notificationManager.notify(randomNo, builder.build());

    }

}
