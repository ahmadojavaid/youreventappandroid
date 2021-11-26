package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.SessionsSingleAdapter;
import com.jobesk.yea.AttendeArea.Models.AgendaDocModel;
import com.jobesk.yea.AttendeArea.Models.AgendaModel;
import com.jobesk.yea.R;
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

public class SingleSessionDetail extends AppCompatActivity {
    private ImageView back_img;
    private RecyclerView recyclerView;
    private SessionsSingleAdapter mAdapter;

    private TextView toolbar_title_tv;
    private String TAG = "SingleSessionDetail";
    private String idSession, sessionID, sessionName, sessionVenue, date, desc, timeFrom, timeTo;
    private TextView sessionTitle_tv, speakers_txt_tv, speakers_tv;
    private String sponserID, sessionId, speakerId, created_at, speakerName, speakerOccupation, speakerCompanyName, speakerDetails, speakerProfileImage;
    private String sponsorID, sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;
    private ArrayList<AgendaDocModel> doclist = new ArrayList<>();
    private String docId, DocattachementURl;
    private NestedScrollView scrollView;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private LinearLayout blue_line_ln;
    private RelativeLayout title_bg_ln;

    private WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_session_detail);


        scrollView = findViewById(R.id.scrollview);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        idSession = bundle.getString("sessionID");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.session));


        sessionTitle_tv = findViewById(R.id.sessionTitle_tv);
        speakers_tv = findViewById(R.id.speakers_tv);
        speakers_txt_tv = findViewById(R.id.speakers_txt_tv);
//        description_tv = findViewById(R.id.description_tv);


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        title_bg_ln = findViewById(R.id.title_bg_ln);
        mywebview = findViewById(R.id.mywebview);


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);


        blue_line_ln = findViewById(R.id.blue_line_ln);


        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            blue_line_ln.setBackgroundColor(appMainColor_int);

            speakers_txt_tv.setBackgroundColor(btnColor_int);
            speakers_tv.setTextColor(btnColor_int);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);
            title_bg_ln.setBackgroundColor(appMainColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        getAgendas();
    }


    private void getAgendas() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);

            WebReq.get(getApplicationContext(), "session/" + idSession, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(SingleSessionDetail.this);
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

                            JSONObject jsonObject = jsonArray.getJSONObject(0);


                            sessionID = jsonObject.getString("id");
                            sessionName = jsonObject.getString("sessionName");
                            sessionVenue = jsonObject.getString("sessionVenue");
                            date = jsonObject.getString("date");
                            timeFrom = jsonObject.getString("timeFrom");
                            timeTo = jsonObject.getString("timeTo");
                            desc = jsonObject.getString("sessionDescription");


                            sessionTitle_tv.setText(sessionName);


                            if (desc.equalsIgnoreCase("") || desc.equalsIgnoreCase("null")) {

                                mywebview.setVisibility(View.GONE);
                            } else {
                                final String mimeType = "text/html";
                                final String encoding = "UTF-8";

                                mywebview.loadDataWithBaseURL("", desc, mimeType, encoding, "");

                            }


                            AgendaModel model = new AgendaModel();
                            model.setSessionID(sessionID);
                            model.setSessionName(sessionName);
                            model.setSessionVenue(sessionVenue);
                            model.setDate(date);
                            model.setTimeFrom(timeFrom);
                            model.setTimeTo(timeTo);
                            model.setSessionDesc(desc);


//                            JSONArray session_sponsorsArray = jsonObject.getJSONArray("session_sponsors");
//                            if (session_sponsorsArray.length() > 0) {
//
////                                sponsersList = new ArrayList<>();
//                                for (int j = 0; j < session_sponsorsArray.length(); j++) {
//
//                                    JSONObject jsonObject1 = session_sponsorsArray.getJSONObject(j);
//                                    sponsorID = jsonObject1.getString("id");
//                                    sessionId = jsonObject1.getString("sessionId");
//                                    created_at = jsonObject1.getString("created_at");
//                                    sponsorName = jsonObject1.getString("sponsorName");
//                                    sponsorImage = jsonObject1.getString("sponsorImage");
//                                    sponsorDescription = jsonObject1.getString("sponsorDescription");
//                                    sponsorTitle = jsonObject1.getString("sponsorTitle");
//                                    sponsorshipLevel = jsonObject1.getString("sponsorshipLevel");
//                                    sponsorwebLink = jsonObject1.getString("sponsorwebLink");
//
//
//                                    AgendaSponsorModel modelSponsor = new AgendaSponsorModel();
//                                    modelSponsor.setSponsorID(sponsorID);
//                                    modelSponsor.setSessionId(sessionId);
//
//                                    modelSponsor.setCreated_at(created_at);
//                                    modelSponsor.setSponsorName(sponsorName);
//                                    modelSponsor.setSponsorImage(sponsorImage);
//                                    modelSponsor.setSponsorDescription(sponsorDescription);
//                                    modelSponsor.setSponsorTitle(sponsorTitle);
//                                    modelSponsor.setSponsorshipLevel(sponsorshipLevel);
//                                    modelSponsor.setSponsorwebLink(sponsorwebLink);
//
//
//                                    sponsersList.add(modelSponsor);
//
//
//                                }
//
//                                model.setSponsersList(sponsersList);
//
////                                agendaAdapter.notifyDataSetChanged();
//
//
//
//
//
//                            }


                            JSONArray session_speakersArray = jsonObject.getJSONArray("session_speakers");
                            if (session_speakersArray.length() > 0) {

                                StringBuilder sb = new StringBuilder();
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


                                    sb.append(speakerName);
                                    sb.append(",");

//                                    AgendaSpeakerModel modelSpeaker = new AgendaSpeakerModel();
//                                    modelSpeaker.setSessionId(sessionId);
//                                    modelSpeaker.setSpeakerId(speakerId);
//                                    modelSpeaker.setCreated_at(created_at);
//                                    modelSpeaker.setSpeakerName(speakerName);
//                                    modelSpeaker.setSpeakerOccupation(speakerOccupation);
//                                    modelSpeaker.setSpeakerCompanyName(speakerCompanyName);
//                                    modelSpeaker.setSpeakerDetails(speakerDetails);
//                                    modelSpeaker.setSpeakerProfileImage(speakerProfileImage);
//                                    speakserList.add(modelSpeaker);


                                }

                                String OurString = sb.toString();
                                if (OurString.toString().endsWith(",")) {
                                    OurString = OurString.substring(0, OurString.length() - 1);
                                }

                                speakers_tv.setText(OurString);

//                                model.setSpeakserList(speakserList);


                            }
                            JSONArray documentsArray = jsonObject.getJSONArray("documents");
                            if (documentsArray.length() > 0) {

//                                doclist = new ArrayList<>();
                                for (int j = 0; j < documentsArray.length(); j++) {


                                    JSONObject jsonObject1 = documentsArray.getJSONObject(j);


                                    docId = jsonObject1.getString("id");
                                    speakerId = jsonObject1.getString("speakerId");
                                    DocattachementURl = jsonObject1.getString("DocattachementURl");
                                    created_at = jsonObject1.getString("created_at");
                                    String documentName = jsonObject1.getString("documentName");


                                    AgendaDocModel modelDoc = new AgendaDocModel();
                                    modelDoc.setDocId(docId);
                                    modelDoc.setSpeakerId(speakerId);
                                    modelDoc.setDocattachementURl(DocattachementURl);
                                    modelDoc.setCreated_at(created_at);
                                    modelDoc.setDocname(documentName);

                                    doclist.add(modelDoc);


                                }

                                model.setDoclist(doclist);


//                                docAdapter = new AgendaSessionsSingleAdapter(AgendaDetailActivity.this, doclist);
//                                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
//                                recycler_view_presentations.setLayoutManager(mLayoutManager2);
//                                recycler_view_presentations.setItemAnimator(new DefaultItemAnimator());
//                                recycler_view_presentations.setAdapter(docAdapter);


                                populateRecyclerView();
                            }

                        }

                        scrollView.setVisibility(View.VISIBLE);
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

    private void populateRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new SessionsSingleAdapter(SingleSessionDetail.this, doclist);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
//        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

    }
}
