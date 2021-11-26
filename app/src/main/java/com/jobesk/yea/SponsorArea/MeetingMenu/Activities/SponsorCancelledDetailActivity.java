package com.jobesk.yea.SponsorArea.MeetingMenu.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.MeetingMenu.FragSponsorMeetingMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

public class SponsorCancelledDetailActivity extends AppCompatActivity {
    TextView toolbar_title_tv;
    ImageView back_img;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private int statusBarColor_int, appMainColor_int, btnColor_int;


    private String userID, sponsor_msg, time, timeID, attendeMsg;

    private TextView show_time_tv, attendee_massage_tv, sponsor_msg_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_meeting_cancelled_detail);



        Intent bundle = getIntent();
        time = bundle.getStringExtra("time");
        timeID = bundle.getStringExtra("timeID");
        attendeMsg = bundle.getStringExtra("attendeMsg");
        sponsor_msg = bundle.getStringExtra("sponsor_msg");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(FragSponsorMeetingMain.FragSponsorMeetingMain);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.hideKeyboard(SponsorCancelledDetailActivity.this);

                finish();
            }
        });


        show_time_tv = findViewById(R.id.show_time_tv);
        attendee_massage_tv = findViewById(R.id.attendee_massage_tv);

        show_time_tv.setText(time + "");
        attendee_massage_tv.setText(attendeMsg + "");


        sponsor_msg_tv = findViewById(R.id.sponsor_msg_tv);
        sponsor_msg_tv.setText(sponsor_msg);


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
}
