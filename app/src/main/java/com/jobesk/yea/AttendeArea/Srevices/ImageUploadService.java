package com.jobesk.yea.AttendeArea.Srevices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.EventBus.FeedRefreshEvent;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;


public class ImageUploadService extends Service {

    private ArrayList<String> imagesList;
    private NotificationManagerCompat notificationManager;

    private StringBuilder imagesStringBuilding = new StringBuilder();
    private String TAG = "ImageUploadService";
    private int counter = 0;
    private int progressCounter = 0;
    private NotificationCompat.Builder notification;
    private String notiChannelID = "334";
    private int progressMax = 100, progress = 0;
    private String postType = "image";
    private String imagez = "";
    private String bodyValue = "";


    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        imagesList = (ArrayList<String>) intent.getSerializableExtra("ImageList");
        bodyValue = intent.getStringExtra("bodyValue");


        notificationManager = NotificationManagerCompat.from(this);
        sendOnChannel2();


        counter = 0;
        String imagepath = imagesList.get(0);
        uploadWithOurAPi(imagepath);
        return Service.START_REDELIVER_INTENT;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void uploadWithOurAPi(String imagepath) {


        RequestParams mParams = new RequestParams();

        String encodedImage = "";
        File file = new File(imagepath);
        try {
            File compressedImageFile = new Compressor(this)
                    .setQuality(50)
                    .compressToFile(file);

            encodedImage = GlobalClass.getStringFile(compressedImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }


        mParams.put("attachmentURL", encodedImage);


        WebReq.post(getApplicationContext(), "upload", mParams, new MyTextHttpResponseHandlerUpload());


    }


    private class MyTextHttpResponseHandlerUpload extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerUpload() {
        }

        @Override
        public void onStart() {
            super.onStart();


            progressCounter = progressCounter + 1;
            notification.setContentTitle("Uploading Images")
                    .setContentText(progressCounter + "/" + imagesList.size());

            progressMax = 0;
            progress = 0;
            notiUpdate();

        }

        @Override
        public void onFinish() {
            super.onFinish();


        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d("response_Upload", "OnFailure" + e);
            stopSelf();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response_Upload", responseString + throwable);

            stopSelf();
        }


        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            Log.d("bytesWritten", "bytesWritten:" + bytesWritten + "    totalSize:" + totalSize);
//            progressMax = Integer.valueOf((int) (totalSize / 100));
////
////
////            progress = Integer.valueOf((int) (bytesWritten * 100));

            progressMax = Integer.valueOf((int) (totalSize));
            progress = Integer.valueOf((int) (bytesWritten));
            Log.d("Progress", "max:" + progressMax + "    min:" + progress);
            notiUpdate();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject mResponse) {


            Log.d("response_Upload", mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {


                String url = null;
                try {
                    url = mResponse.getString("url");

                    imagesStringBuilding.append(url).append(",");
                    Log.d("appendedString", imagesStringBuilding + "");


                    Log.d("counter", counter + "");
                    if (progressCounter < imagesList.size()) {
                        counter = counter + 1;


                        uploadWithOurAPi(imagesList.get(counter));

                    } else {
                        counter = 0;

                        PostFeed();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                stopSelf();
            }
        }
    }


    public void sendOnChannel2() {
        final int progressMax = 100;


        notification = new NotificationCompat.Builder(this, notiChannelID)
                .setSmallIcon(R.drawable.noti_48)
                .setContentTitle("Uploading Images")
                .setContentText(counter + "/" + imagesList.size())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(progressMax, 0, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel2 = new NotificationChannel(
                    notiChannelID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel2);
        }
        notificationManager.notify(2, notification.build());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(2000);
//                for (int progress = 0; progress <= progressMax; progress += 20) {
//                    /*notification.setProgress(progressMax, progress, false);
//                    notificationManager.notify(2, notification.build());*/
//
//                    notification.setProgress(progressMax, progress, false)
//                            .setOngoing(false);
//                    notificationManager.notify(2, notification.build());
//                    SystemClock.sleep(1000);
//                }
//
//                notification.setContentText("Download finished");
//                notificationManager.notify(2, notification.build());
////                notificationManager.cancel(2);
//            }
//        }).start();
    }


    private void notiUpdate() {

        notification.setSmallIcon(R.drawable.noti_48)
                .setContentTitle("Uploading Images")
                .setContentText(progressCounter + "/" + imagesList.size());

        notification.setProgress(progressMax, progress, false)
                .setOngoing(false);
        notificationManager.notify(2, notification.build());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        notificationManager.cancelAll();
    }

    private void PostFeed() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            String userID = GlobalClass.getPref("userID", getApplicationContext());


            RequestParams mParams = new RequestParams();
            mParams.put("userId", userID);
            mParams.put("postDescription", bodyValue);
            mParams.put("postMediaType", "image");


            imagez = imagesStringBuilding.toString();
            if (imagez.endsWith(",")) {
                imagez = imagez.substring(0, imagez.length() - 1);
            }

            mParams.put("attachmentURL", imagez);


            WebReq.post(getApplicationContext(), "/post", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();


            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            stopSelf();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            stopSelf();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {


            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {

                EventBus.getDefault().post(new FeedRefreshEvent());
                stopSelf();
//                try {
//
//
//                    String status = mResponse.getString("statusCode");
//
//                    if (status.equals("1")) {
//
//
//                    } else {
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            } else {
                stopSelf();
            }
        }
    }


}
