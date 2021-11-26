package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Chat.ClientGetConversation;
import com.jobesk.yea.AttendeArea.EventBuses.FollowSelectedEvent;
import com.jobesk.yea.AttendeArea.Fragments.FeedFragment;
import com.jobesk.yea.AttendeArea.Fragments.FragAgenda;
import com.jobesk.yea.AttendeArea.Fragments.FragAttendees;
import com.jobesk.yea.AttendeArea.Fragments.FragLeaderBoards;

import com.jobesk.yea.AttendeArea.Fragments.FragSpeakers;
import com.jobesk.yea.AttendeArea.Fragments.FragSponsor;
import com.jobesk.yea.AttendeArea.MeetingMenu.FragAttendeMeetingMain;
import com.jobesk.yea.R;

import com.jobesk.yea.SponsorArea.MeetingMenu.FragSponsorMeetingMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DrawerActivity extends AppCompatActivity implements View.OnClickListener {

//    NavigationView.OnNavigationItemSelectedListener,

    public static DrawerLayout drawer;
    private TextView feed_tv, profile_tv, leatherboards_tv, speakers_tv, attendes_tv, agenda_tv, logout_tv;
    private TextView sponser_tv;
    private FrameLayout messageContainer;
    private TextView userName_tv, userEmail_tv;
    private TextView messageCounter_tv;
    private int delay = 3000;
    private Runnable runnable;
    private Handler h;
    private String TAG = "DrawerActivity";
    private ImageView drawer_logo_img;
    private LinearLayout drawer_bg_ln;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private int count;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private FeedFragment feedFragment;
    private TextView meetings_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        GlobalClass.hideKeyboard(DrawerActivity.this);

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

        messageCounter_tv = findViewById(R.id.messageCounter_tv);


        String userEmail = GlobalClass.getPref("userEmail", getApplicationContext());
        String userName = GlobalClass.getPref("userName", getApplicationContext());


        userName_tv.setText(userName);
        userEmail_tv.setText(userEmail);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        String followingCount = GlobalClass.getPref("followingCount", getApplicationContext());
//        if (Integer.valueOf(followingCount) < 3) {
//            FragAttendees f1 = new FragAttendees();
//            FragmentManager FM = getSupportFragmentManager();
//            FragmentTransaction FT = FM.beginTransaction();
////            FT.addToBackStack(null);
//            FT.replace(R.id.frame_container, f1);
//            FT.commit();
//        } else {
//
//        }
        FeedFragment f1 = new FeedFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
//            FT.addToBackStack(null);
        FT.replace(R.id.frame_container, f1);
        FT.commit();

        feed_tv = findViewById(R.id.feed_tv);
        profile_tv = findViewById(R.id.profile_tv);
        leatherboards_tv = findViewById(R.id.leatherboards_tv);
        speakers_tv = findViewById(R.id.speakers_tv);
        attendes_tv = findViewById(R.id.attendes_tv);
        agenda_tv = findViewById(R.id.agenda_tv);
        logout_tv = findViewById(R.id.logout_tv);
        sponser_tv = findViewById(R.id.sponser_tv);


        profile_tv.setOnClickListener(this);
        leatherboards_tv.setOnClickListener(this);
        speakers_tv.setOnClickListener(this);
        attendes_tv.setOnClickListener(this);
        agenda_tv.setOnClickListener(this);
        logout_tv.setOnClickListener(this);
        sponser_tv.setOnClickListener(this);
        feed_tv.setOnClickListener(this);


        messageContainer = findViewById(R.id.messageContainer);
        messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(DrawerActivity.this, ClientGetConversation.class);
                startActivity(i);


            }
        });


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


        h = new Handler();
        recusiveCallForMessages();


    }


    private void recusiveCallForMessages() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {

                OnlineUsersRequest();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
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

        GlobalClass.hideKeyboard(DrawerActivity.this);

        switch (v.getId()) {

            case R.id.feed_tv:
                drawer.closeDrawer(GravityCompat.START);

                feedFragment = new FeedFragment();
                Openfrags(feedFragment);

                break;
            case R.id.profile_tv:


                drawer.closeDrawer(GravityCompat.START);

                Intent i = new Intent(getApplicationContext(), AttendeProfileActivity.class);
                String userIDCurent = GlobalClass.getPref("userID", getApplicationContext());
                Bundle bundle = new Bundle();
                bundle.putString("isMyyProfile", "1");
                bundle.putString("nature", "attendee");
                bundle.putString("userID", userIDCurent);
                i.putExtras(bundle);
                startActivity(i);

                feedFragment = new FeedFragment();
                Openfrags(feedFragment);

                break;
            case R.id.sponser_tv:


                drawer.closeDrawer(GravityCompat.START);

                FragSponsor sponsorFrag = new FragSponsor();
                Openfrags(sponsorFrag);
                break;
            case R.id.leatherboards_tv:


                drawer.closeDrawer(GravityCompat.START);
                FragLeaderBoards fragLeaderBoards = new FragLeaderBoards();
                Openfrags(fragLeaderBoards);
                break;
            case R.id.speakers_tv:


                drawer.closeDrawer(GravityCompat.START);
                FragSpeakers speakers = new FragSpeakers();
                Openfrags(speakers);
                break;
            case R.id.attendes_tv:


                drawer.closeDrawer(GravityCompat.START);
                FragAttendees attendees = new FragAttendees();
                Openfrags(attendees);
                break;
            case R.id.agenda_tv:

                drawer.closeDrawer(GravityCompat.START);

                FragAgenda fragAgenda = new FragAgenda();
                Openfrags(fragAgenda);

                break;
            case R.id.meetings_tv:

                drawer.closeDrawer(GravityCompat.START);


                if (GlobalClass.isOnline(getApplicationContext()) == true) {
                    FragAttendeMeetingMain fragMeet = new FragAttendeMeetingMain();
                    Openfrags(fragMeet);
                } else {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet));
                }


                break;
            case R.id.logout_tv:
                drawer.closeDrawer(GravityCompat.START);

                GlobalClass.clearPref(getApplicationContext());
                Intent signInIntent = new Intent(DrawerActivity.this, LoginActivity.class);
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

    private void OnlineUsersRequest() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("user_id", UserID);
            String userID = GlobalClass.getPref("userID", getApplicationContext());
            WebReq.get(getApplicationContext(), "showMessageCount?userId=" + userID, mParams, new MyTextHttpResponseHandlerOnlineUsers());
        } else {

//            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

        }
    }

    private class MyTextHttpResponseHandlerOnlineUsers extends JsonHttpResponseHandler {

        MyTextHttpResponseHandlerOnlineUsers() {


        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d(TAG, " OnStart ");


        }

        @Override
        public void onFinish() {
            super.onFinish();
            Log.d(TAG, " OnFinish ");

        }


        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            try {
                Log.d(TAG, "Fail: " + e.toString() + " ");
            } catch (Exception s) {
                s.printStackTrace();
            }


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject mResponse) {
            Log.d(TAG, "Success: " + mResponse.toString() + " ");


            try {
                String msgCounterValue = mResponse.getString("Result");

                if (msgCounterValue.equalsIgnoreCase("0")) {
                    messageCounter_tv.setVisibility(View.GONE);
                } else {
                    messageCounter_tv.setVisibility(View.VISIBLE);


                    if (Integer.valueOf(msgCounterValue) > 9) {

                        messageCounter_tv.setText("9+");
                    } else {
                        messageCounter_tv.setText(msgCounterValue + "");
                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, "Fail: " + responseString.toString() + " " + throwable);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d(TAG, "Fail: " + errorResponse.toString() + " " + throwable);

        }
    }

    @Override
    protected void onPause() {

        h.removeCallbacks(runnable); //stop handler when activity not visible
        h.removeCallbacksAndMessages(null);
        super.onPause();

    }

    @Override
    protected void onStop() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recusiveCallForMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FollowSelectedEvent event) {

        unLockDrawer();
        getSupportFragmentManager().popBackStack();

        FeedFragment f1 = new FeedFragment();
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.addToBackStack(null);
        FT.add(R.id.frame_container, f1);
        FT.commit();
    }

}
