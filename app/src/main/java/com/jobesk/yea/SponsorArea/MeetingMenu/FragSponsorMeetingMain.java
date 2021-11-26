package com.jobesk.yea.SponsorArea.MeetingMenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.AgendaAdapter;
import com.jobesk.yea.AttendeArea.Models.AgendaDocModel;
import com.jobesk.yea.AttendeArea.Models.AgendaModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSpeakerModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSponsorModel;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.DrawerActivitySponsor;
import com.jobesk.yea.SponsorArea.MeetingMenu.Frags.FragSponsorMeetingCancelled;
import com.jobesk.yea.SponsorArea.MeetingMenu.Frags.FragSponsorMeetingConfirmed;
import com.jobesk.yea.SponsorArea.MeetingMenu.Frags.FragSponsorMeetingRequests;
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

public class FragSponsorMeetingMain extends Fragment {

    private String TAG = "FragAgenda";
    private Activity activity;
    private TextView toolbar_title_tv;
    private ImageView meun_img;
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
    private View rootView;
    private TabLayout tabLayout;
    public static String FragSponsorMeetingMain = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_sponsor_meeting_main, container, false);

        activity = (DrawerActivitySponsor) rootView.getContext();

        meun_img = rootView.findViewById(R.id.meun_img);

        meun_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerActivitySponsor.openDrawer();


            }
        });


        toolbar_title_tv = rootView.findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getActivity().getResources().getString(R.string.meetings));

        tabLayout = rootView.findViewById(R.id.tab_layout);

        String statusBarColor = GlobalClass.getPref("statusBarColor", getActivity());
        String appMainColor = GlobalClass.getPref("appMainColor", getActivity());
        String btnColor = GlobalClass.getPref("btnColor", getActivity());
        String appLogo = GlobalClass.getPref("appLogo", getActivity());


        logo_toolbar = rootView.findViewById(R.id.logo_toolbar);
        toolbar_header = rootView.findViewById(R.id.toolbar_header);
        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            meun_img.setColorFilter(btnColor_int);


            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);
            tabLayout.setBackgroundColor(appMainColor_int);

            tabLayout.setSelectedTabIndicatorColor(btnColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(activity)
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


//        getAgendas();


        makeToolbar();

        return rootView;
    }

    private void makeToolbar() {


        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.requests)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.confirmed)));
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getResources().getString(R.string.cancelled)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = rootView.findViewById(R.id.view_pager);

        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager(), tabLayout.getTabCount());
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

                    FragSponsorMeetingRequests requests = new FragSponsorMeetingRequests();
                    return requests;

                case 1:

                    FragSponsorMeetingConfirmed confirmed = new FragSponsorMeetingConfirmed();
                    return confirmed;

                case 2:

                    FragSponsorMeetingCancelled cancelled = new FragSponsorMeetingCancelled();
                    return cancelled;

                default:

                    return null;
            }
        }
    }

    private void getAgendas() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);

            WebReq.get(getActivity(), "session", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(activity, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(activity);
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
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}