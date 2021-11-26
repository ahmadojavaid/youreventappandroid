package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.AgendaSessionsSingleAdapter;
import com.jobesk.yea.AttendeArea.Adapter.AgendaSpeakersAdapter;
import com.jobesk.yea.AttendeArea.Adapter.AgendaSponsorAdapter;
import com.jobesk.yea.AttendeArea.Models.AgendaDocModel;
import com.jobesk.yea.AttendeArea.Models.AgendaModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSpeakerModel;
import com.jobesk.yea.AttendeArea.Models.AgendaSponsorModel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class AgendaDetailActivity extends AppCompatActivity {

    private ImageView back_img;
    private RecyclerView recycler_view_sponsors, recycler_view_speakers, recycler_view_presentations;
    private AgendaSponsorAdapter agendaAdapter;
    private AgendaSpeakersAdapter speakersAdapter;
    private TextView title_tv, location_tv, time_tv, date_tv, descriptiontv, textWall_tv;
    private ArrayList<AgendaModel> object;
    AgendaSessionsSingleAdapter docAdapter;
    private String TAG = "AgendaDetailActivity";
    private ArrayList<AgendaModel> arrayList = new ArrayList<>();
    private String sponserID, sessionId, speakerId, created_at, speakerName, speakerOccupation, speakerCompanyName, speakerDetails, speakerProfileImage;
    private String sponsorID, sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;
    private String docId, DocattachementURl;
    private ArrayList<AgendaSponsorModel> sponsersList = new ArrayList<>();
    private ArrayList<AgendaSpeakerModel> speakserList = new ArrayList<>();
    private ArrayList<AgendaDocModel> doclist = new ArrayList<>();
    private String sessionID, sessionName, sessionVenue, date, text_wall, desc, timeFrom, timeTo;
    private String idSession;
    private TextView sponsor_tv, speakers_tv, presentations_tv;
    private NestedScrollView scrollView;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private ImageView ic_date, ic_time, ic_location;
    private WebView mWebView;
    private ImageView note_img;
    private TextView toolbar_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_detail);


        scrollView = findViewById(R.id.scrollView);

        Bundle args = getIntent().getExtras();
//        object = (ArrayList<AgendaModel>) args.getSerializable("jobsArrayList");
        idSession = args.getString("idSession");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.session));


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        title_tv = findViewById(R.id.title_tv);
        location_tv = findViewById(R.id.location_tv);
        time_tv = findViewById(R.id.time_tv);
        date_tv = findViewById(R.id.date_tv);

        mWebView = findViewById(R.id.mywebview);

        textWall_tv = findViewById(R.id.textWall_tv);
        descriptiontv = findViewById(R.id.descriptiontv);


        sponsor_tv = findViewById(R.id.sponsor_tv);
        speakers_tv = findViewById(R.id.speakers_tv);
        presentations_tv = findViewById(R.id.presentations_tv);
        getAgendas();

        recycler_view_sponsors = (RecyclerView) findViewById(R.id.recycler_view_sponsors);

        recycler_view_speakers = (RecyclerView) findViewById(R.id.recycler_view_speakers);
        recycler_view_presentations = (RecyclerView) findViewById(R.id.recycler_view_presentations);

        recycler_view_sponsors.setNestedScrollingEnabled(false);
        recycler_view_speakers.setNestedScrollingEnabled(false);
        recycler_view_presentations.setNestedScrollingEnabled(false);


//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
//        recycler_view_sponsors.addItemDecoration(itemDecorator);
//        recycler_view_speakers.addItemDecoration(itemDecorator);
//        recycler_view_presentations.addItemDecoration(itemDecorator);


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);
        note_img = findViewById(R.id.note_img);

        ic_date = findViewById(R.id.ic_date);
        ic_time = findViewById(R.id.ic_time);
        ic_location = findViewById(R.id.ic_location);

        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);


            textWall_tv.setBackgroundColor(btnColor_int);

            ic_date.setColorFilter(appMainColor_int);
            ic_time.setColorFilter(appMainColor_int);
            ic_location.setColorFilter(appMainColor_int);
            title_tv.setBackgroundColor(appMainColor_int);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);
            note_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        scrollView.getParent().requestChildFocus(scrollView, scrollView);


        textWall_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalClass.isValidUrl(text_wall) == true) {
                    Intent i = new Intent(AgendaDetailActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("link", text_wall);
                    bundle.putString("headerName", getApplicationContext().getResources().getString(R.string.text_wall));
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Toast.makeText(AgendaDetailActivity.this, getApplicationContext().getResources().getString(R.string.correct_link), Toast.LENGTH_SHORT).show();
                }


            }
        });


        note_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(AgendaDetailActivity.this, NotesActivity.class);
                i.putExtra("sessionID", sessionID);
                startActivity(i);


            }
        });

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
            GlobalClass.showLoading(AgendaDetailActivity.this);
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
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(0);


                                sessionID = jsonObject.getString("id");
                                sessionName = jsonObject.getString("sessionName");
                                sessionVenue = jsonObject.getString("sessionVenue");
                                date = jsonObject.getString("date");
                                timeFrom = jsonObject.getString("timeFrom");
                                timeTo = jsonObject.getString("timeTo");
                                desc = jsonObject.getString("sessionDescription");
                                text_wall = jsonObject.getString("text_wall");

                                if (text_wall.equalsIgnoreCase("null") || text_wall.equalsIgnoreCase("")) {

                                    textWall_tv.setVisibility(View.GONE);
                                }


                                try {
                                    String toTime = timeTo;

                                    String[] namesList = toTime.split(":");
                                    String to1 = namesList[0];
                                    String to2 = namesList[1];

                                    String valueTo = to1 + ":" + to2;

                                    String fromTime = timeFrom;
                                    String[] list2 = fromTime.split(":");
                                    String from1 = list2[0];
                                    String from2 = list2[1];

                                    String valueFrom = from1 + ":" + from2;


                                    time_tv.setText(valueFrom + "-" + valueTo);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                String dateExtracted = parseDateToddMMyyyy(date);

                                title_tv.setText(sessionName);
                                location_tv.setText(sessionVenue);

                                date_tv.setText(dateExtracted);


                                if (desc.equalsIgnoreCase("null") || desc.equalsIgnoreCase("")) {

                                    mWebView.setVisibility(View.GONE);
                                    descriptiontv.setVisibility(View.GONE);
                                } else {


                                    final String mimeType = "text/html";
                                    final String encoding = "UTF-8";

                                    mWebView.loadDataWithBaseURL("", desc, mimeType, encoding, "");


                                }


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

//                                sponsersList = new ArrayList<>();
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

//                                agendaAdapter.notifyDataSetChanged();


                                    agendaAdapter = new AgendaSponsorAdapter(AgendaDetailActivity.this, sponsersList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    recycler_view_sponsors.setLayoutManager(mLayoutManager);
                                    recycler_view_sponsors.setItemAnimator(new DefaultItemAnimator());
                                    recycler_view_sponsors.setAdapter(agendaAdapter);


                                } else {

                                    sponsor_tv.setVisibility(View.GONE);
                                    recycler_view_sponsors.setVisibility(View.GONE);


                                }


                                JSONArray session_speakersArray = jsonObject.getJSONArray("session_speakers");
                                if (session_speakersArray.length() > 0) {
//                                speakserList = new ArrayList<>();

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


                                    speakersAdapter = new AgendaSpeakersAdapter(AgendaDetailActivity.this, speakserList);
                                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
                                    recycler_view_speakers.setLayoutManager(mLayoutManager1);
                                    recycler_view_speakers.setItemAnimator(new DefaultItemAnimator());
                                    recycler_view_speakers.setAdapter(speakersAdapter);
//                                speakersAdapter.notifyDataSetChanged();
                                } else {

                                    speakers_tv.setVisibility(View.GONE);
                                    recycler_view_speakers.setVisibility(View.GONE);

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


                                    docAdapter = new AgendaSessionsSingleAdapter(AgendaDetailActivity.this, doclist);
                                    RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
                                    recycler_view_presentations.setLayoutManager(mLayoutManager2);
                                    recycler_view_presentations.setItemAnimator(new DefaultItemAnimator());
                                    recycler_view_presentations.setAdapter(docAdapter);

//                                docAdapter.notifyDataSetChanged();
                                } else {

                                    presentations_tv.setVisibility(View.GONE);
                                    recycler_view_presentations.setVisibility(View.GONE);
                                }


                                arrayList.add(model);

                            }
                            scrollView.setVisibility(View.VISIBLE);
                        } else {
                            scrollView.setVisibility(View.GONE);
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

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
