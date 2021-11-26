package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.SessionsAdapter;
import com.jobesk.yea.AttendeArea.Models.SessionsModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
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

public class SpeakerDeatilActivity extends AppCompatActivity {
    private ImageView back_img;
    private SessionsAdapter mAdapter;
    private RecyclerView recyclerView;

    private ArrayList<SessionsModel> sessionArrayList = new ArrayList<>();
    private TextView toolbar_title_tv;
    private String speakerID;
    private String TAG = "SpeakerDeatilActivity";
    private ImageView userImage;
    private TextView companyName_tv, username_tv, occupation_tv;
    private String postID, speakerId, sessionName, sessionVenue, date, timeFrom, timeTo;
    private NestedScrollView rootlayout;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private LinearLayout bottom_line, blue_line_ln, ornage_line_ln;

    private TextView fulture_session_tv;
    private LinearLayout future_session_container;

    private WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_deatil);

        Bundle bundle = getIntent().getExtras();
        speakerID = bundle.getString("speakerID");


        rootlayout = findViewById(R.id.rootlayout);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);


        userImage = findViewById(R.id.userImage);


//        speakerDetail_tv = findViewById(R.id.speakerDetail_tv);
        companyName_tv = findViewById(R.id.companyName_tv);
        username_tv = findViewById(R.id.username_tv);
        occupation_tv = findViewById(R.id.occupation_tv);
        future_session_container = findViewById(R.id.future_session_container);
        mywebview = findViewById(R.id.mywebview);

        getDetailSpeaker();


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);


        blue_line_ln = findViewById(R.id.blue_line_ln);
        ornage_line_ln = findViewById(R.id.ornage_line_ln);
        fulture_session_tv = findViewById(R.id.fulture_session_tv);


        bottom_line = findViewById(R.id.bottom_line);


        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            blue_line_ln.setBackgroundColor(btnColor_int);
            bottom_line.setBackgroundColor(btnColor_int);
            ornage_line_ln.setBackgroundColor(btnColor_int);

            companyName_tv.setTextColor(btnColor_int);

            fulture_session_tv.setBackgroundColor(appMainColor_int);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);
            username_tv.setTextColor(appMainColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


    }
//    speakerID

    private void getDetailSpeaker() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);

            WebReq.get(getApplicationContext(), "speaker/" + speakerID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(SpeakerDeatilActivity.this);
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

                        JSONArray jsonArray = mResponse.getJSONArray("Result");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String speakerName = jsonObject.getString("speakerName");
                                String speakerOccupation = jsonObject.getString("speakerOccupation");
                                String speakerCompanyName = jsonObject.getString("speakerCompanyName");
                                String speakerDetails = jsonObject.getString("speakerDetails");
                                String speakerProfileImage = jsonObject.getString("speakerProfileImage");
                                toolbar_title_tv.setText(speakerName);


                                if (speakerDetails.equalsIgnoreCase("null") || speakerDetails.equalsIgnoreCase("")) {
                                    ornage_line_ln.setVisibility(View.GONE);
                                    mywebview.setVisibility(View.GONE);
                                } else {

                                    final String mimeType = "text/html";
                                    final String encoding = "UTF-8";

                                    mywebview.loadDataWithBaseURL("", speakerDetails, mimeType, encoding, "");

                                }


                                companyName_tv.setText(speakerCompanyName);
                                username_tv.setText(speakerName);
                                occupation_tv.setText(speakerOccupation);

                                Picasso.with(getApplicationContext())
                                        .load(Urls.BASE_URL_IMAGE + speakerProfileImage)
                                        .fit()
                                        .centerCrop()
                                        .transform(new CircleTransform())
                                        .placeholder(R.drawable.speaker_profile_placeholder)
                                        .into(userImage);


                                JSONArray jsonSessions = jsonObject.getJSONArray("sessions");


                                if (jsonSessions.length() > 0) {
                                    for (int j = 0; j < jsonSessions.length(); j++) {

                                        JSONObject jsonObjectSession = jsonSessions.getJSONObject(i);
                                        postID = jsonObjectSession.getString("id");
                                        speakerId = jsonObjectSession.getString("speakerId");
                                        sessionName = jsonObjectSession.getString("sessionName");
                                        sessionVenue = jsonObjectSession.getString("sessionVenue");
                                        date = jsonObjectSession.getString("date");
                                        timeFrom = jsonObjectSession.getString("timeFrom");
                                        timeTo = jsonObjectSession.getString("timeTo");


                                        SessionsModel model = new SessionsModel();
                                        model.setPostID(postID);
                                        model.setSpeakerId(speakerId);
                                        model.setSessionName(sessionName);
                                        model.setSessionVenue(sessionVenue);
                                        model.setDate(date);
                                        model.setTimeFrom(timeFrom);
                                        model.setTimeTo(timeTo);
                                        sessionArrayList.add(model);


                                    }

                                } else {

                                    future_session_container.setVisibility(View.GONE);
                                }


                            }
                        }
                        rootlayout.setVisibility(View.VISIBLE);
                        populateRecyclerView();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(SpeakerDeatilActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void populateRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new SessionsAdapter(SpeakerDeatilActivity.this, sessionArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

    }


}
