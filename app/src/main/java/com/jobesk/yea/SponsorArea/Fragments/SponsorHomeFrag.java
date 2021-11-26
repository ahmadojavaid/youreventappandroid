package com.jobesk.yea.SponsorArea.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.SponsorArea.AttendeeMenu.Adapters.SponsorTimeAvailableAdapter;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.DrawerActivitySponsor;
import com.jobesk.yea.SponsorArea.SponsorEditProfile;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SponsorHomeFrag extends Fragment {
    private SponsorTimeAvailableAdapter mAdapter;
    private Activity activity;
    private RecyclerView recyclerView;
    private ImageView meun_img;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private TextView toolbar_title_tv;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private ImageView edit_img;
    private String
            sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;
    private String TAG = "SponsorHomeFrag";
    private ImageView
            useriamge;
    private String userID;
    private TextView username_tv, weblink_tv, description_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_sponsor_home, container, false);

        userID = GlobalClass.getPref("userID", getActivity());

        activity = (DrawerActivitySponsor) rootView.getContext();

        description_tv = rootView.findViewById(R.id.description_tv);
        username_tv = rootView.findViewById(R.id.username_tv);
        useriamge = rootView.findViewById(R.id.useriamge);
        weblink_tv = rootView.findViewById(R.id.weblink_tv);


        toolbar_title_tv = rootView.findViewById(R.id.toolbar_title_tv);



        toolbar_title_tv.setText(getActivity().getResources().getString(R.string.profile));

        meun_img = rootView.findViewById(R.id.meun_img);


        meun_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerActivitySponsor.openDrawer();

            }
        });


        edit_img = rootView.findViewById(R.id.edit_img);
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GlobalClass.isOnline(getActivity()) == true) {

                    Intent i = new Intent(getActivity(), SponsorEditProfile.class);

                    i.putExtra("image", sponsorImage);
                    i.putExtra("name", sponsorName);
                    i.putExtra("description", sponsorDescription);
                    i.putExtra("website", sponsorwebLink);

                    startActivity(i);
                } else {

                    Toast.makeText(activity, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                }


            }
        });


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


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(activity)
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        getSponsorDetail();
    }

    private void getSponsorDetail() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);


            WebReq.get(getActivity(), "sponsor/" + userID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
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


                        JSONObject jsonObject = mResponse.getJSONObject("Result");


                        sponsorName = jsonObject.getString("sponsorName");
                        sponsorImage = jsonObject.getString("sponsorImage");
                        sponsorDescription = jsonObject.getString("sponsorDescription");
                        sponsorTitle = jsonObject.getString("sponsorTitle");
                        sponsorshipLevel = jsonObject.getString("sponsorshipLevel");
                        sponsorwebLink = jsonObject.getString("sponsorwebLink");



                        Picasso.with(getActivity())
                                .load(Urls.BASE_URL_IMAGE + sponsorImage)
                                .fit().centerCrop()
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.profile_placeholder)
                                .into(useriamge);


                        description_tv.setText(sponsorDescription);
                        weblink_tv.setText(sponsorwebLink);
                        username_tv.setText(sponsorName);










                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
