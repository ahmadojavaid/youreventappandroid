package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.NotificationAdapter;
import com.jobesk.yea.AttendeArea.Models.NotificationModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView noti_img, back_img;
    private TextView toolbar_title_tv;
    private NotificationAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> arrayListNotis = new ArrayList<>();
    private String noti_id, userId, postId, notificationTitle, notificationData, notifiacationStatus, notificationType, notificationBody, imageUrl, created_at;
    private String TAG = "NotificationActivity";
    private NotificationModel model;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private boolean refresh = false;
    private boolean loading = true;
    private int visibleThreshold = 10;
    private int totalPage;
    private int currentPage;
    private int previousTotal = 0;
    private int hasScrollrecyler = 0;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        noti_img = findViewById(R.id.noti_img);
        noti_img.setVisibility(View.GONE);
        GlobalClass.putPref("notiValueStartCheck", "0", getApplicationContext());

        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.notifications));

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        GlobalClass.putPref("notiCounter", "", getApplicationContext());


        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new NotificationAdapter(NotificationActivity.this, arrayListNotis);
        layoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        currentPage = 1;
        totalPage = 1;
        previousTotal = 0;
        getNotifications();


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);


        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);
        }
    }

    private void getNotifications() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);

            String userID = GlobalClass.getPref("userID", getApplicationContext());
            WebReq.get(getApplicationContext(), "showNotifications?userId=" + userID + "&page=" + currentPage, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        refresh = true;

        currentPage = 1;
        totalPage = 1;
        previousTotal = 0;
        hasScrollrecyler = 0;
        if (arrayListNotis.size() > 0) {
            arrayListNotis.clear();
        }
        getNotifications();
    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            Log.d(TAG, "onStart");

            if (refresh == true) {

                refresh = false;
            } else {
                GlobalClass.showLoading(NotificationActivity.this);
            }

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            refreshLayout.setRefreshing(false);
            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        JSONObject jsonObjectMain = mResponse.getJSONObject("Result");

                        currentPage = Integer.valueOf(jsonObjectMain.getString("current_page"));
                        totalPage = Integer.valueOf(jsonObjectMain.getString("total"));

                        JSONArray jsonArray = jsonObjectMain.getJSONArray("data");


                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                noti_id = jsonObject.getString("id");
                                userId = jsonObject.getString("userId");
                                postId = jsonObject.getString("postId");
                                notificationTitle = jsonObject.getString("notificationTitle");
                                notificationData = jsonObject.getString("notificationData");
                                notifiacationStatus = jsonObject.getString("notifiacationStatus");
                                notificationType = jsonObject.getString("notificationType");
                                created_at = jsonObject.getString("created_at");
                                imageUrl = jsonObject.getString("imageUrl");
                                notificationBody = jsonObject.getString("notificationBody");



//                                notificationType =1 for Admin
//                                notificationType =2 Like on your post
//                                notificationType =3 comment on your post
//                                notificationType =4 Like on your comment
//                                  notificationType =5 Message

                                if (notificationType.equalsIgnoreCase("2")
                                        || notificationType.equalsIgnoreCase("3")
                                        || notificationType.equalsIgnoreCase("4")
                                        ) {

                                    model = new NotificationModel();
                                    model.setNoti_id(noti_id);
                                    model.setUserId(userId);
                                    model.setPostId(postId);
                                    model.setNotificationTitle(notificationTitle);
                                    model.setNotificationData(notificationData);
                                    model.setNotifiacationStatus(notifiacationStatus);
                                    model.setNotificationType(notificationType);
                                    model.setCreated_at(created_at);
                                    model.setUserImage(imageUrl);

                                    model.setType(NotificationModel.TYPE_OTHER);
                                    arrayListNotis.add(model);


                                }


                                if (notificationType.equalsIgnoreCase("1")) {

                                    model = new NotificationModel();
                                    model.setNoti_id(noti_id);
                                    model.setUserId(userId);
                                    model.setPostId(postId);
                                    model.setNotificationTitle(notificationTitle);
                                    model.setNotificationData(notificationData);
                                    model.setNotifiacationStatus(notifiacationStatus);
                                    model.setNotificationType(notificationType);
                                    model.setCreated_at(created_at);
                                    model.setUserImage(imageUrl);

                                    model.setType(NotificationModel.TYPE_EVENT);
                                    arrayListNotis.add(model);


                                }
                                if (notificationType.equalsIgnoreCase("5")) {

                                    model = new NotificationModel();
                                    model.setNoti_id(noti_id);
                                    model.setUserId(userId);
                                    model.setPostId(postId);
                                    model.setNotificationTitle(notificationTitle);
                                    model.setNotificationData(notificationData);
                                    model.setNotifiacationStatus(notifiacationStatus);
                                    model.setNotificationType(notificationType);
                                    model.setCreated_at(created_at);
                                    model.setUserImage(imageUrl);
                                    model.setBody(notificationBody);
                                    model.setType(NotificationModel.TYPE_NEWS);
                                    arrayListNotis.add(model);


                                }


                            }
                            mAdapter.notifyDataSetChanged();
                            if (hasScrollrecyler == 0) {
                                ScrollViewRecyclerAndroid();
                            }

                        } else {
//                            Toast.makeText(NotificationActivity.this, getApplicationContext().getResources().getString(R.string.no_notification_found), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(NotificationActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void ScrollViewRecyclerAndroid() {


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                    if (currentPage > totalPage) {


//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(activity, R.string.no_more_advisor, Toast.LENGTH_SHORT).show();
//                                }
//                            }, 2000);
                    } else {
                        try {
                            currentPage++;

                            if (GlobalClass.isOnline(getApplicationContext())) {
//                                    progressBar.setVisibility(View.VISIBLE);
                            } else {
//                                    progressBar.setVisibility(View.GONE);
                            }
                            if (currentPage <= totalPage) {

                                getNotifications();
                            } else {
//                                    progressBar.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    loading = true;
                }
            }
        });


    }


}
