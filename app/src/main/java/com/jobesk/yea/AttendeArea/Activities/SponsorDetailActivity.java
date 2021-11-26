package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.AttendeeMenu.AttendeeMainCalender;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class SponsorDetailActivity extends AppCompatActivity {
    private ImageView back_img;
    private TextView toolbar_title_tv;
    private String SponsorID;
    private String TAG = "SponsorDetailActivity";
    private String
            sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;
    private TextView company_tv, level_name_tv, visitWeb_tv;
    private ImageView userimage;
    private ScrollView scrollView;

    private ImageView insta_img, facebook_img, twitter_img, linnked_img;
    private String fbLink, instaLink, linkedInLink, twitterLink;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;

    private TextView contact_us_txt, sponsor_ship_lvl_txt;
    private LinearLayout social_share_container;
    private WebView mywebview;
    private ImageView calander_img;
    private String sponsorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_detail);
        scrollView = findViewById(R.id.scrollView);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        Bundle extras = getIntent().getExtras();
        SponsorID = extras.getString("SponsorID");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.sponsor));


        userimage = findViewById(R.id.userimage);
        company_tv = findViewById(R.id.company_tv);
//        medal_icon = findViewById(R.id.medal_icon);
        level_name_tv = findViewById(R.id.level_name_tv);
        visitWeb_tv = findViewById(R.id.visitWeb_tv);

        social_share_container = findViewById(R.id.social_share_container);


        insta_img = findViewById(R.id.insta_img);
        facebook_img = findViewById(R.id.facebook_img);
        twitter_img = findViewById(R.id.twitter_img);
        linnked_img = findViewById(R.id.linnked_img);
        mywebview = findViewById(R.id.mywebview);


        insta_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = instaLink;

                openChrome(url);
            }
        });

        facebook_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = fbLink;

                openChrome(url);
            }
        });
        twitter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = twitterLink;

                openChrome(url);
            }
        });
        linnked_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = linkedInLink;

                openChrome(url);
            }
        });


        calander_img = findViewById(R.id.calander_img);
        calander_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GlobalClass.isOnline(getApplicationContext()) == true) {
                    Intent atendeMainCalander = new Intent(SponsorDetailActivity.this, AttendeeMainCalender.class);
                    atendeMainCalander.putExtra("SponsorID", SponsorID);
                    startActivity(atendeMainCalander);
                } else {
                    Toast.makeText(SponsorDetailActivity.this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }


            }
        });


        getSponsorDetail();


        visitWeb_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(sponsorwebLink));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(SponsorDetailActivity.this, getApplicationContext().getResources().getString(R.string.correct_link), Toast.LENGTH_SHORT).show();
//                }


                if (isValid(sponsorwebLink) == true) {

                    Intent i = new Intent(SponsorDetailActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("link", sponsorwebLink);
                    bundle.putString("headerName", sponsorName);
                    i.putExtras(bundle);
                    startActivity(i);

                } else {
                    Toast.makeText(SponsorDetailActivity.this, getApplicationContext().getResources().getString(R.string.correct_link), Toast.LENGTH_SHORT).show();
                }


            }
        });


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);
        sponsor_ship_lvl_txt = findViewById(R.id.sponsor_ship_lvl_txt);
        contact_us_txt = findViewById(R.id.contact_us_txt);

        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);
            company_tv.setBackgroundColor(appMainColor_int);
            contact_us_txt.setBackgroundColor(appMainColor_int);


            visitWeb_tv.setBackgroundColor(btnColor_int);
            sponsor_ship_lvl_txt.setBackgroundColor(appMainColor_int);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);
            calander_img.setColorFilter(btnColor_int);
            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }
    }


    private void openChrome(String URL) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
//            startActivity(intent);
//        } catch (Exception e)
//
//        {
//
//            Toast.makeText(SponsorDetailActivity.this, getApplicationContext().getResources().getString(R.string.correct_link), Toast.LENGTH_SHORT).show();
//        }

        boolean val = isValid(URL);


        if (val == true) {

            Intent i = new Intent(SponsorDetailActivity.this, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("link", URL);
            bundle.putString("headerName", sponsorName);
            i.putExtras(bundle);
            startActivity(i);

        } else {
            Toast.makeText(SponsorDetailActivity.this, getApplicationContext().getResources().getString(R.string.correct_link), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isValid(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException e) {

        }

        return false;
    }

    private void getSponsorDetail() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);


            WebReq.get(getApplicationContext(), "sponsor/" + SponsorID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(SponsorDetailActivity.this);
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

//
//                        sponsorID = jsonObject.getString("user_id");
                        sponsorName = jsonObject.getString("sponsorName");
                        sponsorImage = jsonObject.getString("sponsorImage");
                        sponsorDescription = jsonObject.getString("sponsorDescription");
                        sponsorTitle = jsonObject.getString("sponsorTitle");
                        sponsorshipLevel = jsonObject.getString("sponsorshipLevel");
                        sponsorwebLink = jsonObject.getString("sponsorwebLink");

                        fbLink = jsonObject.getString("fbLink");
                        instaLink = jsonObject.getString("instaLink");
                        linkedInLink = jsonObject.getString("linkedInLink");
                        twitterLink = jsonObject.getString("twitterLink");


                        if (fbLink.equalsIgnoreCase("null") || fbLink.equalsIgnoreCase("")) {
                            facebook_img.setVisibility(View.GONE);
                        }
                        if (instaLink.equalsIgnoreCase("null") || instaLink.equalsIgnoreCase("")) {
                            insta_img.setVisibility(View.GONE);
                        }
                        if (linkedInLink.equalsIgnoreCase("null") || linkedInLink.equalsIgnoreCase("")) {
                            linnked_img.setVisibility(View.GONE);
                        }
                        if (twitterLink.equalsIgnoreCase("null") || twitterLink.equalsIgnoreCase("")) {

                            twitter_img.setVisibility(View.GONE);
                        }


                        if (fbLink.equalsIgnoreCase("") && instaLink.equalsIgnoreCase("") &&
                                linkedInLink.equalsIgnoreCase("") && twitterLink.equalsIgnoreCase("")
                        ) {

                            social_share_container.setVisibility(View.GONE);
                            contact_us_txt.setVisibility(View.GONE);
                        }


                        if (sponsorDescription.equalsIgnoreCase("null") || sponsorDescription.equalsIgnoreCase("")) {
                            mywebview.setVisibility(View.GONE);
                        } else {

                            final String mimeType = "text/html";
                            final String encoding = "UTF-8";

                            mywebview.loadDataWithBaseURL("", sponsorDescription, mimeType, encoding, "");
                        }


                        Picasso.with(getApplicationContext())
                                .load(Urls.BASE_URL_IMAGE + sponsorImage)
                                .fit().centerCrop()
                                .placeholder(R.drawable.sponsor_detail_placeholder)
                                .into(userimage);

                        level_name_tv.setText(sponsorshipLevel);
                        company_tv.setText(sponsorName);


//                        medal_icon = findViewById(R.id.medal_icon);


//                        String level = sponsorshipLevel;
//                        if (level.equalsIgnoreCase("gold")) {
//
//                            medal_icon.setImageResource(R.drawable.ic_gold);
//
//                        }
//                        if (level.equalsIgnoreCase("silver")) {
//
//                            medal_icon.setImageResource(R.drawable.ic_silver);
//
//                        }
//                        if (level.equalsIgnoreCase("platinum")) {
//
//                            medal_icon.setImageResource(R.drawable.ic_platinum);
//
//                        }

                        scrollView.setVisibility(View.VISIBLE);
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(SponsorDetailActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
