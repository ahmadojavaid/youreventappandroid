package com.jobesk.yea.AttendeArea.Chat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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

public class ClientGetConversation extends AppCompatActivity {
    private ImageView back_img;
    private TextView toolbar_title, availableconnections;
    private String TAG = "ClientGetConversation";
    private ListView listView;
    private int conversactionSize = 0;
    private GetconversationModel userchatmodel;
    private ArrayList<GetconversationModel> mUsersData = new ArrayList<>();
    private ClientConversationAdapter chatUsersAdapter;
    private RecyclerView recyclerView;
    private ClientConversationAdapter mAdapter;
    private boolean firstTime = true;
    private int delay = 5000;
    private Runnable runnable;
    private Handler h;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_get_conversation);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        GlobalClass.putPref("notiValueStartCheck", "0", getApplicationContext());
        toolbar_title = findViewById(R.id.toolbar_title_tv);
//        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.your_conversation));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ClientConversationAdapter(ClientGetConversation.this, mUsersData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        recyclerView.setAdapter(mAdapter);

        availableconnections = (TextView) findViewById(R.id.availableconnections);


        OnlineUsersRequest();







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

    @Override
    protected void onStart() {

        h = new Handler();
        recusiveCallForMessages();
        super.onStart();
    }

    private void recusiveCallForMessages() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {

                OnlineUsersRequest();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    protected void OnlineUsersRequest() {


        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
//            mParams.put("user_id", UserID);
            String userID = GlobalClass.getPref("userID", getApplicationContext());
            WebReq.get(getApplicationContext(), "getConv?userId=" + userID, mParams, new MyTextHttpResponseHandlerOnlineUsers());
        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    class MyTextHttpResponseHandlerOnlineUsers extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerOnlineUsers() {


        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d(TAG, " OnStart ");

            if (firstTime == true) {
                firstTime = false;

                GlobalClass.showLoading(ClientGetConversation.this);

            }


        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d(TAG, " OnFinish ");
            GlobalClass.dismissLoading();
        }


        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "Fail: " + e.toString() + " ");
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject mResponse) {
            Log.d(TAG, "Success: " + mResponse.toString() + " ");
            GlobalClass.dismissLoading();

            try {
                JSONArray jsonArray = mResponse.getJSONArray("Result");


                if (jsonArray.length() > 0) {

                    conversactionSize = jsonArray.length();


                    if (mUsersData.size() > 0) {
                        mUsersData.clear();

                    }


                    for (int i = 0; i < mResponse.length(); i++) {


                        JSONObject object = jsonArray.getJSONObject(i);
                        String message = object.getString("lastmessage");
                        String created_at = object.getString("created_at");

                        String currentUserId = object.getString("currentUserId");

                        String recieverId = object.getString("senderId");
                        String senderId = object.getString("recieverId");



                        userchatmodel = new GetconversationModel();

                        if (currentUserId.equalsIgnoreCase(senderId)) {

                            String recieverName = object.getString("senderName");
                            String image = object.getString("senderProfileImage");

                            userchatmodel.setReceiverID(recieverId);
                            userchatmodel.setReceivername(recieverName);
                            userchatmodel.setReceiverImage(image);
                        } else {

                            String senderName = object.getString("recieverName");
                            String image = object.getString("recieverProfileImage");


                            userchatmodel.setReceiverID(senderId);
                            userchatmodel.setReceivername(senderName);
                            userchatmodel.setReceiverImage(image);
                        }



                        userchatmodel.setMessage(message);
                        userchatmodel.setDate(created_at);

//                        userchatmodel.setIsOnline(isOnline);
                        mUsersData.add(userchatmodel);


                        availableconnections.setText(getApplicationContext().getResources().getString(R.string.online) + "(" + conversactionSize + ")");

                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    availableconnections.setText(getApplicationContext().getResources().getString(R.string.online) + "(0)");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            mAdapter.notifyDataSetChanged();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, "Fail: " + responseString.toString() + " " + throwable);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d(TAG, "Fail: " + errorResponse.toString() + " " + throwable);
            GlobalClass.dismissLoading();
        }
    }

    @Override
    protected void onPause() {

        h.removeCallbacks(runnable); //stop handler when activity not visible
        h.removeCallbacksAndMessages(null);
        super.onPause();

    }

    @Override
    protected void onStop() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onStop();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recusiveCallForMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
