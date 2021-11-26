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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class GetAllChat extends AppCompatActivity {
    private ImageView back_img;
    private TextView toolbar_title;
    private String TAG = "GetAllChat";

    private String message, chatStatus, created_at, newMessage = "";


    private ArrayList<ClientGetChatMessagesModel> chatArrayList = new ArrayList<>();

    private ClientGetChatMessagesModel model;
    private RecyclerView recyclerView;
    private ClientChatAdapter mAdapter;

    private TextView send_tv;
    private int previousLength = 0;
    private EditText send_et;

    private int delay = 5000;
    private Runnable runnable;
    private Handler h;

    private static boolean firtTime = true;
    private String userID_other;
    private ImageView userImageView;
    private String userIDCurrent, userName, userimage;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_get_chat);
        firtTime = true;

        userIDCurrent = GlobalClass.getPref("userID", getApplicationContext());

        Bundle bundle = getIntent().getExtras();


        userID_other = bundle.getString("userID");
        userimage = bundle.getString("userimage");
        userName = bundle.getString("userName");


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title_tv);
        toolbar_title.setText(userName);

        userImageView = findViewById(R.id.userImageView);
//        online_img = findViewById(R.id.online_img);
//
//        try {
//            Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + preImage).fit().centerCrop().into(userImageView);
//
//            if (preOnline.equalsIgnoreCase("1")) {
//                online_img.setImageResource(R.drawable.circle_online_chat_green);
//            } else {
//                online_img.setImageResource(R.drawable.circle_online_chat_green);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ClientChatAdapter(GetAllChat.this, chatArrayList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        send_tv = findViewById(R.id.send_tv);
        send_et = findViewById(R.id.send_et);


        send_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newMessage = send_et.getText().toString().trim();

                if (newMessage.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_message), Toast.LENGTH_SHORT).show();
                    return;
                }


                send_et.setText("");
                SendMessage();

            }
        });

        Apicalls();


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
            send_tv.setBackgroundColor(appMainColor_int);

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


                Apicalls();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }


    private void Apicalls() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


//            if (apiStaus == 1) {
            // getPrevious chat


            RequestParams mParams = new RequestParams();
//                mParams.put("screenName", screen);
            WebReq.get(getApplicationContext(), "message?fromUser=" + userIDCurrent + "&toUser=" + userID_other, mParams, new MyTextHttpResponseHandler());


//            }


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

            Log.d("FirstTime", firtTime + "");
            if (firtTime == true) {
                GlobalClass.showLoading(GetAllChat.this);

            }


            Log.d(TAG, "getChat:onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "getChat:onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "getChat:OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, "getChat:" + mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {


                            if (chatArrayList.size() > 0) {

                                chatArrayList.clear();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                message = jsonObject.getString("messageText");

                                created_at = jsonObject.getString("created_at");
                                String toUser = jsonObject.getString("toUser");
                                String fromUser = jsonObject.getString("fromUser");


                                model = new ClientGetChatMessagesModel();
                                model.setMessage(message);
                                model.setCreated_at(created_at);

                                if (fromUser.equalsIgnoreCase(userIDCurrent)) {
                                    model.setType(ClientGetChatMessagesModel.TYPE_right);
                                } else {
                                    model.setType(ClientGetChatMessagesModel.TYPE_left);
                                }

                                chatArrayList.add(model);


                            }
                            mAdapter.notifyDataSetChanged();


                            if (firtTime == true) {

                                recyclerView.scrollToPosition(chatArrayList.size() - 1);


                            }
                            firtTime = false;
                            if (previousLength > 0) {
                                if (chatArrayList.size() > previousLength) {
                                    //MediaPlayer.create(mContext,R.raw.message).start();
                                    recyclerView.scrollToPosition(chatArrayList.size() - 1);
                                }
                            }
                            previousLength = chatArrayList.size();


                        } else {
                            firtTime = false;
                            if (firtTime == true) {
                                Toast.makeText(GetAllChat.this, getApplicationContext().getResources().getString(R.string.no_chat_messages_found), Toast.LENGTH_SHORT).show();
                            }


                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void SendMessage() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
            String userIDCurrent = GlobalClass.getPref("userID", getApplicationContext());
            mParams.put("toUser", userID_other);
            mParams.put("fromUser", userIDCurrent);
            mParams.put("messageText", newMessage);


            GlobalClass.hideKeyboard(GetAllChat.this);

            WebReq.post(getApplicationContext(), "message", mParams, new MyTextHttpResponseHandlerSendMsg());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerSendMsg extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerSendMsg() {

        }

        @Override
        public void onStart() {
            super.onStart();
//            GlobalClass.showLoading(GetAllChat.this);
            Log.d(TAG, "sendMsg:onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "sendMsg:onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "sendMsg:OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, "sendMsg:" + responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, "sendMsg:" + mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
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
