package com.jobesk.yea.SponsorArea;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.LoginActivity;
import com.jobesk.yea.AttendeArea.Activities.NotificationActivity;
import com.jobesk.yea.AttendeArea.Chat.ClientGetConversation;
import com.jobesk.yea.AttendeArea.Fragments.FeedFragment;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.Fragments.FragSAttendees;
import com.jobesk.yea.SponsorArea.MeetingMenu.FragSponsorMeetingMain;
import com.jobesk.yea.SponsorArea.Fragments.SFragAgenda;
import com.jobesk.yea.SponsorArea.Fragments.SponsorHomeFrag;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class DrawerActivitySponsor extends AppCompatActivity implements View.OnClickListener {

//    NavigationView.OnNavigationItemSelectedListener,

    public static DrawerLayout drawer;

    private TextView profile_tv, attendes_tv, meetings_tv, agenda_tv, logout_tv;


    private TextView userName_tv, userEmail_tv;

    private String TAG = "DrawerActivity";
    private ImageView drawer_logo_img;
    private LinearLayout drawer_bg_ln;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private int count;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private FeedFragment feedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_sponsor);

        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        GlobalClass.hideKeyboard(DrawerActivitySponsor.this);

        String token = GlobalClass.getPref("clientToken", getApplicationContext());
        Log.d("token", token);


        meetings_tv = findViewById(R.id.meetings_tv);
        meetings_tv.setOnClickListener(this);


        drawer_bg_ln = findViewById(R.id.drawer_bg_ln);
        drawer_logo_img = findViewById(R.id.drawer_logo_img);

        if (!appMainColor.equalsIgnoreCase("")) {


            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }


            drawer_bg_ln.setBackgroundResource(0);

            drawer_bg_ln.setBackgroundColor(appMainColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(drawer_logo_img);
        }


        userName_tv = findViewById(R.id.userName_tv);
        userEmail_tv = findViewById(R.id.userEmail_tv);


        String userEmail = GlobalClass.getPref("userEmail", getApplicationContext());
        String userName = GlobalClass.getPref("userName", getApplicationContext());


        userName_tv.setText(userName);
        userEmail_tv.setText(userEmail);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        SponsorHomeFrag f1 = new SponsorHomeFrag();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.frame_container, f1);
        FT.commit();


        profile_tv = findViewById(R.id.profile_tv);

        attendes_tv = findViewById(R.id.attendes_tv);
        agenda_tv = findViewById(R.id.agenda_tv);
        logout_tv = findViewById(R.id.logout_tv);


        profile_tv.setOnClickListener(this);
        attendes_tv.setOnClickListener(this);
        agenda_tv.setOnClickListener(this);
        logout_tv.setOnClickListener(this);


        Intent intent = getIntent();

        if (intent.hasExtra("notiValueStartCheck")) {
            String notiValueStartCheck = intent.getExtras().getString("notiValueStartCheck");
            if (notiValueStartCheck.equalsIgnoreCase("1")) {
                Intent NotiIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(NotiIntent);
                GlobalClass.putPref("notiValueStartCheck", "0", getApplicationContext());
            } else if (notiValueStartCheck.equalsIgnoreCase("5")) {

                Intent chatIntent = new Intent(getApplicationContext(), ClientGetConversation.class);
                startActivity(chatIntent);
                GlobalClass.putPref("notiValueStartCheck", "0", getApplicationContext());
            }

        }


    }


    private void Openfrags(Fragment f1) {


//        FeedFragment f1 = new FeedFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
//        FT.addToBackStack(null);
        FT.replace(R.id.frame_container, f1);
        FT.commit();

    }


    @Override
    public void onClick(View v) {

        GlobalClass.hideKeyboard(DrawerActivitySponsor.this);

        switch (v.getId()) {

            case R.id.profile_tv:

                drawer.closeDrawer(GravityCompat.START);

//                Intent i = new Intent(getApplicationContext(), AttendeProfileActivity.class);
//                String userIDCurent = GlobalClass.getPref("userID", getApplicationContext());
//                Bundle bundle = new Bundle();
//                bundle.putString("isMyyProfile", "1");
//                bundle.putString("nature", "attendee");
//                bundle.putString("userID", userIDCurent);
//                i.putExtras(bundle);
//                startActivity(i);

                SponsorHomeFrag f1 = new SponsorHomeFrag();
                Openfrags(f1);


                break;
            case R.id.attendes_tv:

//
//                drawer.closeDrawer(GravityCompat.START);
//                FragAttendees attendees = new FragAttendees();
//                Openfrags(attendees);

                drawer.closeDrawer(GravityCompat.START);

                FragSAttendees asd = new FragSAttendees();
                Openfrags(asd);
                break;

            case R.id.meetings_tv:

                drawer.closeDrawer(GravityCompat.START);


                if (GlobalClass.isOnline(getApplicationContext()) == true) {
                    FragSponsorMeetingMain fragMeet = new FragSponsorMeetingMain();
                    Openfrags(fragMeet);
                } else {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet));
                }


                break;

            case R.id.agenda_tv:

                drawer.closeDrawer(GravityCompat.START);

                SFragAgenda fragAgenda = new SFragAgenda();
                Openfrags(fragAgenda);

                break;

            case R.id.logout_tv:
                drawer.closeDrawer(GravityCompat.START);

                GlobalClass.clearPref(getApplicationContext());
                Intent signInIntent = new Intent(DrawerActivitySponsor.this, LoginActivity.class);
                signInIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInIntent);

                break;


        }

    }

    public static void openDrawer() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

    }

    public static void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    public static void unLockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.double_tap_to_exit), Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

//            count = getSupportFragmentManager().getBackStackEntryCount();
//
//            if (count == 1) {
//                super.onBackPressed();
//            finish();
//            } else {
//                getSupportFragmentManager().popBackStack();
//            }

        }
    }


    @Override
    protected void onPause() {


        super.onPause();

    }

    @Override
    protected void onStop() {

        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(FollowSelectedEvent event) {
//
//        unLockDrawer();
//        getSupportFragmentManager().popBackStack();
//
//        FeedFragment f1 = new FeedFragment();
//        FragmentManager FM = getSupportFragmentManager();
//        FragmentTransaction FT = FM.beginTransaction();
//        FT.addToBackStack(null);
//        FT.add(R.id.frame_container, f1);
//        FT.commit();
//    }

}
