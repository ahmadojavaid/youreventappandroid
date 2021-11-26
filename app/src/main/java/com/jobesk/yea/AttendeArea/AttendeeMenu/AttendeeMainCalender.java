package com.jobesk.yea.AttendeArea.AttendeeMenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.AgendaAdapter;
import com.jobesk.yea.AttendeArea.AttendeeMenu.Frags.FragAttendeTimeAvaliable;
import com.jobesk.yea.AttendeArea.AttendeeMenu.Frags.FragAttendeTimeConfirmed;
import com.jobesk.yea.AttendeArea.AttendeeMenu.Frags.FragAttendeTimeNotConfirmed;
import com.jobesk.yea.AttendeArea.Models.AgendaDocModel;
import com.jobesk.yea.AttendeArea.Models.AgendaModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSpeakerModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSponsorModel;
import com.jobesk.yea.EventBuses.AttendeDateTitleEvent;
import com.jobesk.yea.R;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AttendeeMainCalender extends AppCompatActivity {

    private String TAG = "FragAgenda";

    private TextView toolbar_title_tv;
    private ImageView back_img;
    private AgendaAdapter mAdapter;
    private ArrayList<AgendaModel> arrayList = new ArrayList<>();
    private String sponserID, sessionId, speakerId, created_at, speakerName, speakerOccupation, speakerCompanyName, speakerDetails, speakerProfileImage;
    private String sponsorID, sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;
    private String docId, DocattachementURl;
    private ArrayList<AgendaSponsorModel> sponsersList = new ArrayList<>();
    private ArrayList<AgendaSpeakerModel> speakserList = new ArrayList<>();
    private ArrayList<AgendaDocModel> doclist = new ArrayList<>();
    private String sessionID, sessionName, sessionVenue, date, desc, timeFrom, timeTo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private ImageView logo_toolbar;

    private TabLayout tabLayout;
    public static String SponsorID;


    public static String AttendeeMainCalenderDate;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_attendee_sponsor);


        Intent bundle = getIntent();


        SponsorID = bundle.getStringExtra("SponsorID");


        back_img = findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);


        tabLayout = findViewById(R.id.tab_layout);

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

            back_img.setColorFilter(btnColor_int);


            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);
            tabLayout.setBackgroundColor(appMainColor_int);

            tabLayout.setSelectedTabIndicatorColor(btnColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }


        }


        makeToolbar();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AttendeDateTitleEvent event) {
        String eventDate = event.getDate().toString().trim();
        toolbar_title_tv.setText(eventDate);
        AttendeeMainCalenderDate = eventDate;
    }


    private void makeToolbar() {


        tabLayout.addTab(tabLayout.newTab().setText(getApplicationContext().getResources().getString(R.string.available)));
        tabLayout.addTab(tabLayout.newTab().setText(getApplicationContext().getResources().getString(R.string.not_confirmed)));
        tabLayout.addTab(tabLayout.newTab().setText(getApplicationContext().getResources().getString(R.string.confirmed)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class TabsAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public TabsAdapter(FragmentManager fm, int NoofTabs) {
            super(fm);
            this.mNumOfTabs = NoofTabs;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    FragAttendeTimeAvaliable avaliable = new FragAttendeTimeAvaliable();
                    return avaliable;

                case 1:

                    FragAttendeTimeNotConfirmed NotConfirmed = new FragAttendeTimeNotConfirmed();
                    return NotConfirmed;

                case 2:

                    FragAttendeTimeConfirmed confirmed = new FragAttendeTimeConfirmed();
                    return confirmed;


                default:

                    return null;
            }
        }
    }

    private void getAgendas() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);

            WebReq.get(getApplicationContext(), "session", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AttendeeMainCalender.this);
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

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            sessionID = jsonObject.getString("id");
                            sessionName = jsonObject.getString("sessionName");
                            sessionVenue = jsonObject.getString("sessionVenue");
                            date = jsonObject.getString("date");
                            timeFrom = jsonObject.getString("timeFrom");
                            timeTo = jsonObject.getString("timeTo");
                            desc = jsonObject.getString("sessionDescription");


                            AgendaModel model = new AgendaModel();
                            model.setSessionID(sessionID);
                            model.setSessionName(sessionName);
                            model.setSessionVenue(sessionVenue);
                            model.setDate(date);
                            model.setTimeFrom(timeFrom);
                            model.setTimeTo(timeTo);
                            model.setSessionDesc(desc);


                            JSONArray session_sponsorsArray = jsonObject.getJSONArray("session_sponsors");
                            if (session_sponsorsArray.length() > 0) {

                                sponsersList = new ArrayList<>();
                                for (int j = 0; j < session_sponsorsArray.length(); j++) {


                                    JSONObject jsonObject1 = session_sponsorsArray.getJSONObject(j);


                                    sponsorID = jsonObject1.getString("id");
                                    sessionId = jsonObject1.getString("sessionId");

                                    created_at = jsonObject1.getString("created_at");
                                    sponsorName = jsonObject1.getString("sponsorName");
                                    sponsorImage = jsonObject1.getString("sponsorImage");
                                    sponsorDescription = jsonObject1.getString("sponsorDescription");
                                    sponsorTitle = jsonObject1.getString("sponsorTitle");
                                    sponsorshipLevel = jsonObject1.getString("sponsorshipLevel");
                                    sponsorwebLink = jsonObject1.getString("sponsorwebLink");


                                    AgendaSponsorModel modelSponsor = new AgendaSponsorModel();
                                    modelSponsor.setSponsorID(sponsorID);
                                    modelSponsor.setSessionId(sessionId);

                                    modelSponsor.setCreated_at(created_at);
                                    modelSponsor.setSponsorName(sponsorName);
                                    modelSponsor.setSponsorImage(sponsorImage);
                                    modelSponsor.setSponsorDescription(sponsorDescription);
                                    modelSponsor.setSponsorTitle(sponsorTitle);
                                    modelSponsor.setSponsorshipLevel(sponsorshipLevel);
                                    modelSponsor.setSponsorwebLink(sponsorwebLink);


                                    sponsersList.add(modelSponsor);


                                }

                                model.setSponsersList(sponsersList);
                            }


                            JSONArray session_speakersArray = jsonObject.getJSONArray("session_speakers");
                            if (session_speakersArray.length() > 0) {

                                speakserList = new ArrayList<>();
                                for (int j = 0; j < session_speakersArray.length(); j++) {

                                    JSONObject jsonObject1 = session_speakersArray.getJSONObject(j);

                                    sessionId = jsonObject1.getString("sessionId");
                                    speakerId = jsonObject1.getString("speakerId");
                                    created_at = jsonObject1.getString("created_at");
                                    speakerName = jsonObject1.getString("speakerName");
                                    speakerOccupation = jsonObject1.getString("speakerOccupation");
                                    speakerCompanyName = jsonObject1.getString("speakerCompanyName");
                                    speakerDetails = jsonObject1.getString("speakerDetails");
                                    speakerProfileImage = jsonObject1.getString("speakerProfileImage");

                                    AgendaSpeakerModel modelSpeaker = new AgendaSpeakerModel();
                                    modelSpeaker.setSessionId(sessionId);
                                    modelSpeaker.setSpeakerId(speakerId);
                                    modelSpeaker.setCreated_at(created_at);
                                    modelSpeaker.setSpeakerName(speakerName);
                                    modelSpeaker.setSpeakerOccupation(speakerOccupation);
                                    modelSpeaker.setSpeakerCompanyName(speakerCompanyName);
                                    modelSpeaker.setSpeakerDetails(speakerDetails);
                                    modelSpeaker.setSpeakerProfileImage(speakerProfileImage);
                                    speakserList.add(modelSpeaker);


                                }

                                model.setSpeakserList(speakserList);
                            }


                            JSONArray documentsArray = jsonObject.getJSONArray("documents");
                            if (documentsArray.length() > 0) {

                                doclist = new ArrayList<>();
                                for (int j = 0; j < documentsArray.length(); j++) {


                                    JSONObject jsonObject1 = documentsArray.getJSONObject(j);

                                    docId = jsonObject1.getString("id");
                                    speakerId = jsonObject1.getString("speakerId");
                                    DocattachementURl = jsonObject1.getString("DocattachementURl");
                                    created_at = jsonObject1.getString("created_at");

                                    AgendaDocModel modelDoc = new AgendaDocModel();
                                    modelDoc.setDocId(docId);
                                    modelDoc.setSpeakerId(speakerId);
                                    modelDoc.setDocattachementURl(DocattachementURl);
                                    modelDoc.setCreated_at(created_at);
                                    doclist.add(modelDoc);

                                }


                                model.setDoclist(doclist);

                            }


                            arrayList.add(model);

                        }


                        mAdapter.notifyDataSetChanged();
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