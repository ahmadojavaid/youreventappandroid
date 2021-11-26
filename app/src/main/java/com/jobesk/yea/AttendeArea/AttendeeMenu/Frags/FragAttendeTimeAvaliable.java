package com.jobesk.yea.AttendeArea.AttendeeMenu.Frags;

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

import com.jobesk.yea.AttendeArea.AttendeeMenu.Adapters.AttendeTimeAvailableAdapter;

import com.jobesk.yea.AttendeArea.AttendeeMenu.AttendeeMainCalender;
import com.jobesk.yea.AttendeArea.Models.AAvaliableModel;
import com.jobesk.yea.EventBuses.AttendeDateTitleEvent;
import com.jobesk.yea.R;
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

public class FragAttendeTimeAvaliable extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private AttendeTimeAvailableAdapter mAdapter;
    private Activity activity;
    private ArrayList<AAvaliableModel> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private String TAG = "FragAttendeTimeAvaliable";
    private String userID;

    private   boolean refresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_sponsor_time_avaliable, container, false);


        userID = GlobalClass.getPref("userID", getActivity());

        activity = (AttendeeMainCalender) rootView.getContext();

        recyclerView = rootView.findViewById(R.id.recyclerview);


        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);

        mAdapter = new AttendeTimeAvailableAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return rootView;

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


            WebReq.get(activity, "available-timeslots?attendee_id=" + userID + "&sponsor_id=" + AttendeeMainCalender.SponsorID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRefresh() {
        refresh = true;

        getData();


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
//            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
//            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
//            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
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
                    JSONObject eventDate = mResponse.getJSONObject("eventDate");

                    String dateHere = eventDate.getString("DATE");

                    EventBus.getDefault().post(new AttendeDateTitleEvent(dateHere));


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
