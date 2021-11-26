package com.jobesk.yea.AttendeArea.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.DrawerActivity;
import com.jobesk.yea.AttendeArea.Activities.LeaderboardInstructionActivity;
import com.jobesk.yea.AttendeArea.Adapter.LeatherBoardAdapter;
import com.jobesk.yea.AttendeArea.EventBuses.LeaderBoardEventBus;
import com.jobesk.yea.AttendeArea.Models.LeaderBoardModel;
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

public class FragLeaderBoards extends Fragment {

    private TextView toolbar_title_tv;
    private LeatherBoardAdapter mAdapter;
    private RecyclerView recyclerView;
    private Activity activity;
    private ArrayList<LeaderBoardModel> arrayList = new ArrayList<>();
    private ImageView meun_img;
    private String TAG = "FragLeaderBoards";
    private LeaderBoardModel model;
    private String searchKeyWord = "";
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private ImageView logo_toolbar;
    private ImageView instruction_tv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_leader_boards, container, false);


        activity = (DrawerActivity) rootView.getContext();

        meun_img = rootView.findViewById(R.id.meun_img);
        meun_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerActivity.openDrawer();

            }
        });

        toolbar_title_tv = rootView.findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getActivity().getResources().getString(R.string.leaderboards));

        String statusBarColor = GlobalClass.getPref("statusBarColor", getActivity());
        String appMainColor = GlobalClass.getPref("appMainColor", getActivity());
        String btnColor = GlobalClass.getPref("btnColor", getActivity());
        String appLogo = GlobalClass.getPref("appLogo", getActivity());


        logo_toolbar = rootView.findViewById(R.id.logo_toolbar);
        instruction_tv = rootView.findViewById(R.id.instruction_tv);
        toolbar_header = rootView.findViewById(R.id.toolbar_header);
        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            meun_img.setColorFilter(btnColor_int);

            instruction_tv.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);

            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(activity)
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new LeatherBoardAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        getLeaderboards();


        instruction_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LeaderboardInstructionActivity.class);
                startActivity(intent);


            }
        });


        return rootView;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LeaderBoardEventBus event) {
        searchKeyWord = event.getKeyword();

        Log.d("keywordTitleSearch", searchKeyWord);
        getLeaderboards();
    }


    private void getLeaderboards() {

        if (GlobalClass.isOnline(getActivity()) == true) {


//            if (searchKeyWord.equalsIgnoreCase("")) {
//                RequestParams mParams = new RequestParams();
////            mParams.put("postId", email);
//                String userID = GlobalClass.getPref("userID", activity);
//                mParams.put("userId", userID);
//                WebReq.get(getActivity(), "leaderboard", mParams, new MyTextHttpResponseHandler());
//
//            } else {
//
//
            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);
            String userID = GlobalClass.getPref("userID", activity);

            WebReq.get(getActivity(), "leaderboard?userId=" + userID, mParams, new MyTextHttpResponseHandler());


//            }

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


//                        if (arrayList.size() > 0) {
//                            arrayList.clear();
//                        }
//                        mAdapter.notifyDataSetChanged();
//
//
//                        model = new LeaderBoardModel();
//                        model.setType(LeaderBoardModel.TYEPE_SEARCH);
//                        arrayList.add(model);

                        JSONArray jsonArray = mResponse.getJSONArray("Result");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String image = jsonObject.getString("profileImage");
                                String name = jsonObject.getString("name");
                                String followCount = jsonObject.getString("followersCount");
                                String followingCount = jsonObject.getString("followingCount");
                                String companyName = jsonObject.getString("companyName");
                                String jobTitle = jsonObject.getString("jobTitle");
                                String isfollowed = jsonObject.getString("isfollowed");
                                String points = jsonObject.getString("points");

                                model = new LeaderBoardModel();
                                model.setId(id);
                                model.setImage(image);
                                model.setName(name);
                                model.setFollersCount(followCount);
                                model.setFollwingCount(followingCount);
                                model.setType(LeaderBoardModel.TYPE_USER);
                                model.setCompanyName(companyName);
                                model.setJobTitle(jobTitle);
                                model.setIsFollow(isfollowed);
                                model.setPoints(points);
                                arrayList.add(model);


                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_leader_boards_found), Toast.LENGTH_SHORT).show();
                        }


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
