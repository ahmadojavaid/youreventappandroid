package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LeaderboardInstructionActivity extends AppCompatActivity {
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private TextView toolbar_title_tv;
    private ImageView back_img;
    private WebView mywebview;
    private String TAG = "LeaderboardInstructionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_instruction);


        back_img = findViewById(R.id.back_img);
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);

        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.leaderboard_criteria));

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mywebview = findViewById(R.id.mywebview);

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

        getInfor();
    }


    private void getInfor() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);
//            String userID = GlobalClass.getPref("userID", activity);

            WebReq.get(getApplicationContext(), "getinfo", mParams, new MyTextHttpResponseHandler());


//            }

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
            GlobalClass.showLoading(LeaderboardInstructionActivity.this);
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


                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        String text = jsonObject.getString("text");


                        if (text.equalsIgnoreCase("") || text.equalsIgnoreCase("null")) {
                            mywebview.setVisibility(View.GONE);

                        } else {


                            final String mimeType = "text/html";
                            final String encoding = "UTF-8";

                            mywebview.loadDataWithBaseURL("", text, mimeType, encoding, "");
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

}
