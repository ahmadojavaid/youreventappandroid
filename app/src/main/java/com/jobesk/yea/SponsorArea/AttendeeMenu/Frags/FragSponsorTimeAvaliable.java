package com.jobesk.yea.SponsorArea.AttendeeMenu.Frags;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.MeetingMenu.FragAttendeMeetingMain;
import com.jobesk.yea.EventBuses.SponsorDateEventBus;
import com.jobesk.yea.SponsorArea.AttendeeMenu.Adapters.SponsorTimeAvailableAdapter;
import com.jobesk.yea.AttendeArea.Models.AAvaliableModel;
import com.jobesk.yea.R;

import com.jobesk.yea.SponsorArea.AttendeeMenu.SponsorAttendeeMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragSponsorTimeAvaliable extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SponsorTimeAvailableAdapter mAdapter;
    private Activity activity;
    private ArrayList<AAvaliableModel> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String TAG = "FragSponsorTimeAvaliable";
    private String userID;
    private SwipeRefreshLayout refreshLayout;
    private boolean refresh = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_sponsor_time_avaliable, container, false);


        userID = GlobalClass.getPref("userID", getActivity());

        activity = (SponsorAttendeeMain) rootView.getContext();

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);


        recyclerView = rootView.findViewById(R.id.recyclerview);


        mAdapter = new SponsorTimeAvailableAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return rootView;

    }

    @Override
    public void onRefresh() {
        refresh = true;

        getData();


    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);


            WebReq.get(activity, "sponsor-available-ts?attendee_id=" + SponsorAttendeeMain.attendeID + "&sponsor_id=" + userID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            if (refresh == true) {
                refreshLayout.setRefreshing(true);

            }

//            GlobalClass.showLoading(activity);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            refreshLayout.setRefreshing(false);

//            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            refreshLayout.setRefreshing(false);
//            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            refreshLayout.setRefreshing(false);
//            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            refresh = false;
            refreshLayout.setRefreshing(false);
//            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    JSONObject dateEvent = mResponse.getJSONObject("eventDate");

                    String date = dateEvent.getString("DATE");
                    SponsorAttendeeMain.SponsorAttendeeMainDate = date;


                    EventBus.getDefault().post(new SponsorDateEventBus(date));


                    if (status.equals("1")) {

                        if (arrayList.size() > 0) {
                            arrayList.clear();

                        }


                        JSONArray jsonArray = mResponse.getJSONArray("Result");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String id = jsonObject.getString("ID");

                            String from = jsonObject.getString("TIME_FROM");
                            String to = jsonObject.getString("TIME_TO");


                            AAvaliableModel model = new AAvaliableModel();
                            model.setTime_id(id);
                            model.setFrom(from);
                            model.setTo(to);
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
