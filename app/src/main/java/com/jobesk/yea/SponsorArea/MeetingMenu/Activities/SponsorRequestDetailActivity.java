package com.jobesk.yea.SponsorArea.MeetingMenu.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Models.AAvaliableModel;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.MeetingMenu.FragSponsorMeetingMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SponsorRequestDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView toolbar_title_tv;
    private ImageView back_img;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    private TextView show_time_tv, attendee_massage_tv, cencel_tv, confirm_tv;
    private EditText message_et;

    private String TAG = "AttendeRequestDetailActivity";
    private ArrayList<AAvaliableModel> arrayList = new ArrayList<>();
    private String userID;
    private String time, timeID, attendeMsg;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_meeting_requests_detail);


        userID = GlobalClass.getPref("userID", getApplicationContext());


        Intent bundle = getIntent();
        time = bundle.getStringExtra("time");
        timeID = bundle.getStringExtra("timeID");
        attendeMsg = bundle.getStringExtra("attendeMsg");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(FragSponsorMeetingMain.FragSponsorMeetingMain);


        show_time_tv = findViewById(R.id.show_time_tv);
        attendee_massage_tv = findViewById(R.id.attendee_massage_tv);
        cencel_tv = findViewById(R.id.cencel_tv);
        confirm_tv = findViewById(R.id.confirm_tv);
        cencel_tv.setOnClickListener(this);
        confirm_tv.setOnClickListener(this);

        show_time_tv.setText(time + "");
        attendee_massage_tv.setText(attendeMsg + "");


        message_et = findViewById(R.id.message_et);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.hideKeyboard(SponsorRequestDetailActivity.this);

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

            case R.id.cencel_tv:


                msg = message_et.getText().toString().trim();
                if (msg.equalsIgnoreCase("")) {


                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.enter_msg), Toast.LENGTH_SHORT).show();
                    return;
                }

                getData("Cancelled");

                break;

            case R.id.confirm_tv:


                msg = message_et.getText().toString().trim();
                if (msg.equalsIgnoreCase("")) {


                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.enter_msg), Toast.LENGTH_SHORT).show();
                    return;
                }



                getData("Confirmed");

                break;

        }
    }


    private void getData(String status) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("timeslot_manage_id", timeID + "");
            mParams.put("message", msg);
            mParams.put("status", status);

            WebReq.post(getApplicationContext(), "sponsor-response", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(SponsorRequestDetailActivity.this);
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
