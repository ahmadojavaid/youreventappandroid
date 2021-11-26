package com.jobesk.yea.SponsorArea.AttendeeMenu.DetailActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.AttendeeMenu.SponsorAttendeeMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SponsorTimeAvaliableDetails extends AppCompatActivity implements View.OnClickListener {
    TextView toolbar_title_tv;
    ImageView back_img;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private String time, timeID;
    private String TAG = "AttendeTimeAvaliableDetails";
    private TextView send_tv, time_tv_show;
    private String userID;
    private EditText message_et;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_time_avaliable_detail);


        userID = GlobalClass.getPref("userID", getApplicationContext());

        Intent bundle = getIntent();
        time = bundle.getStringExtra("time");
        timeID = bundle.getStringExtra("timeID");


        time_tv_show = findViewById(R.id.time_tv_show);
        time_tv_show.setText(time + "");
        message_et = findViewById(R.id.message_et);

        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(SponsorAttendeeMain.SponsorAttendeeMainDate);


        send_tv = findViewById(R.id.send_tv);
        send_tv.setOnClickListener(this);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.hideKeyboard(SponsorTimeAvaliableDetails.this);
                finish();

            }
        });


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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_tv:


                msg = message_et.getText().toString().trim();

                if (msg.equals("")) {
                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.enter_msg), Toast.LENGTH_SHORT).show();
                    return;
                }


                updateData();
                break;

        }

    }


    private void updateData() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("timeslot_id", timeID);
            mParams.put("attendee_id", SponsorAttendeeMain.attendeID);
            mParams.put("sponsor_id", userID);
            mParams.put("message", msg);

            WebReq.post(getApplicationContext(), "sponsor-request-ts", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(SponsorTimeAvaliableDetails.this);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);


            String statusCode = null;
            try {
                statusCode = e.getString("statusCode");
                if (statusCode.equalsIgnoreCase("0")) {
                    String statusMessage = e.getString("statusMessage");
                    GlobalClass.showToast(getApplicationContext(), statusMessage);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }






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
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        finish();


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


}
