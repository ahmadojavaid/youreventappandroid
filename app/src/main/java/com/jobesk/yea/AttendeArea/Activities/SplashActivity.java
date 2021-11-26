package com.jobesk.yea.AttendeArea.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.DrawerActivitySponsor;
import com.jobesk.yea.Utils.GlobalClass;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private LinearLayout continue_btn;
    //    private ImageView background_img;
    String notiValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        Log.d("notivalueSharedPref", "value=" + GlobalClass.getPref("notivalue", getApplicationContext()));

//        background_img = findViewById(R.id.background_img);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {


                String userID = GlobalClass.getPref("userID", getApplicationContext());
                String role = GlobalClass.getPref("role", getApplicationContext());

                if (userID.equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                } else {
                    Intent intent = getIntent();

                    if (role.equalsIgnoreCase("1")) {
                        // For the sponsor

                        if (intent.hasExtra("notiValueStartCheck")) {
//                            String value = intent.getExtras().getString("notiValueStartCheck");
                            Intent NotiIntent = new Intent(SplashActivity.this, DrawerActivitySponsor.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("notiValueStartCheck", value);
//                            NotiIntent.putExtras(bundle);
                            startActivity(NotiIntent);

                        } else {
                            Intent NotiIntent = new Intent(SplashActivity.this, DrawerActivitySponsor.class);
                            startActivity(NotiIntent);

                        }

                    } else {

                        // for the attendee
                        if (intent.hasExtra("notiValueStartCheck")) {
                            String value = intent.getExtras().getString("notiValueStartCheck");
                            Intent NotiIntent = new Intent(SplashActivity.this, DrawerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("notiValueStartCheck", value);
                            NotiIntent.putExtras(bundle);
                            startActivity(NotiIntent);

                        } else {
                            Intent NotiIntent = new Intent(SplashActivity.this, DrawerActivity.class);
                            startActivity(NotiIntent);

                        }

                    }


                }


            }
        }, 3000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
