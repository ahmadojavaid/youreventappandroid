package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

public class ShowNewsActivity extends AppCompatActivity {
    private String bodyText = "";

    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private ImageView back_img;
    private String headerName = "";
    private TextView toolbar_title_tv;
    private TextView title_tv;
    private WebView
            mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        title_tv = findViewById(R.id.title_tv);
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);

        Bundle extras = getIntent().getExtras();
        bodyText = extras.getString("bodyText");
        headerName = extras.getString("headerName");


        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.alert));


        mywebview = findViewById(R.id.mywebview);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";


        mywebview.loadDataWithBaseURL("", bodyText, mimeType, encoding, "");


        title_tv.setText(headerName);


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
