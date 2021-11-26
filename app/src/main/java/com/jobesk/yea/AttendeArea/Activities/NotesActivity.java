package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NotesActivity extends AppCompatActivity {
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private ImageView back_img;
    private TextView toolbar_title_tv;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private TextView messageCounter_tv;
    private EditText et_notes;
    private TextView text_here, date_Tv, submit_Tv;
    private String notesValue;
    private String TAG = "NotesActivity";
    private String userID, sessionID;
    private RelativeLayout container_show, add_note_contaienr;
    private LinearLayout line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        userID = GlobalClass.getPref("userID", getApplicationContext());


        Intent intent = getIntent();
        sessionID = intent.getStringExtra("sessionID");
        line = findViewById(R.id.line);

        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.notes));


        container_show = findViewById(R.id.container_show);
        add_note_contaienr = findViewById(R.id.add_note_contaienr);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);


        et_notes = findViewById(R.id.et_notes);
        messageCounter_tv = findViewById(R.id.messageCounter_tv);
        submit_Tv = findViewById(R.id.submit_Tv);


        text_here = findViewById(R.id.text_here);
        date_Tv = findViewById(R.id.date_Tv);


        et_notes.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int
                    start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String value = s.length() + "/10000";

                messageCounter_tv.setText(value);


            }
        });

        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


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

            line.setBackgroundColor(btnColor_int);


            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }

        submit_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                notesValue = et_notes.getText().toString().trim();

                if (notesValue.equalsIgnoreCase("")) {
                    Toast.makeText(NotesActivity.this, getApplicationContext().getResources().getString(R.string.enter_note), Toast.LENGTH_SHORT).show();
                    return;
                }

                postStatus();
            }
        });


        container_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                container_show.setVisibility(View.GONE);
                add_note_contaienr.setVisibility(View.VISIBLE);


            }
        });


        getStatus();
    }

    private void getStatus() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);


            WebReq.get(getApplicationContext(), "note/" + sessionID + "/" + userID, mParams, new MyTextHttpResponseHandlerGetStatus());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetStatus extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerGetStatus() {

        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(NotesActivity.this);
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

                        JSONObject jsonObject = mResponse.getJSONObject("data");
                        String note_body = jsonObject.getString("note_body");
                        String created_at = jsonObject.getString("updated_at");

                        text_here.setText(note_body);
                        date_Tv.setText(created_at);
                        et_notes.setText(note_body);
                        add_note_contaienr.setVisibility(View.GONE);
                        container_show.setVisibility(View.VISIBLE);


                        return;
                    }
                    if (status.equals("0")) {

                        add_note_contaienr.setVisibility(View.VISIBLE);
                        container_show.setVisibility(View.GONE);
                        return;
                    }


                    String message = mResponse.getString("statusMessage");
                    Toast.makeText(NotesActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void postStatus() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("session_id", sessionID);
            mParams.put("user_id", userID);
            mParams.put("body", notesValue);


            String url = "create/note";
            WebReq.post(getApplicationContext(), url, mParams, new MyTextHttpResponseHandlerpostStatus());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerpostStatus extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerpostStatus() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(NotesActivity.this);
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

                        JSONObject jsonObject = mResponse.getJSONObject("data");
                        String note_body = jsonObject.getString("note_body");
                        String created_at = jsonObject.getString("updated_at");


                        text_here.setText(note_body);
                        date_Tv.setText(created_at);
                        et_notes.setText(note_body);
                        add_note_contaienr.setVisibility(View.GONE);
                        container_show.setVisibility(View.VISIBLE);


                        Toast.makeText(NotesActivity.this, getApplicationContext().getResources().getString(R.string.note_saved_and_email), Toast.LENGTH_SHORT).show();

                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
