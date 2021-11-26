package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.AttendeProfileActivity;
import com.jobesk.yea.AttendeArea.EventBuses.LeaderBoardEventBus;
import com.jobesk.yea.AttendeArea.Models.LeaderBoardModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LeatherBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<LeaderBoardModel> leaderBoardList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public LeatherBoardAdapter(Activity activity, ArrayList<LeaderBoardModel> leaderBoardList) {
        this.leaderBoardList = leaderBoardList;
        this.activity = activity;


        statusBarColor = GlobalClass.getPref("statusBarColor", activity);
        appMainColor = GlobalClass.getPref("appMainColor", activity);
        btnColor = GlobalClass.getPref("btnColor", activity);
        appLogo = GlobalClass.getPref("appLogo", activity);

        if (!appMainColor.equalsIgnoreCase("")) {
            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case LeaderBoardModel.TYEPE_SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
                return new SearchViewHolder(view);
            case LeaderBoardModel.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leaderboards, parent, false);
                return new UserViewHolder(view);

        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final LeaderBoardModel model = leaderBoardList.get(position);

        if (model != null) {
            switch (model.getType()) {
                case LeaderBoardModel.TYEPE_SEARCH:

                    if (!appMainColor.equalsIgnoreCase("")) {
                        ((SearchViewHolder) holder).search_container.setBackgroundColor(btnColor_int);
                    }
                    ((SearchViewHolder) holder).search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                                String keywordSearch = ((SearchViewHolder) holder).search_et.getText().toString().trim();
                                if (keywordSearch.equalsIgnoreCase("")) {
                                    Toast.makeText(activity, activity.getApplicationContext().getResources().getString(R.string.enter_search_title), Toast.LENGTH_SHORT).show();
                                } else {
                                    EventBus.getDefault().post(new LeaderBoardEventBus(keywordSearch));
                                }


                                return true;
                            }
                            return false;
                        }
                    });

                    break;
                case LeaderBoardModel.TYPE_USER:


                    String points = model.getPoints();
                    if (Integer.valueOf(points) > 10000) {
                        ((UserViewHolder) holder).points_tv.setText(activity.getResources().getString(R.string.points) + ": 10000+");
                    } else {
                        ((UserViewHolder) holder).points_tv.setText(activity.getResources().getString(R.string.points) + ": " + points);
                    }


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((UserViewHolder) holder).companyName_tv.setTextColor(btnColor_int);
                        ((UserViewHolder) holder).follow_tv.setBackgroundColor(btnColor_int);

                    }

                    String isFollow = model.getIsFollow();
                    if (isFollow.equalsIgnoreCase("1")) {
                        ((UserViewHolder) holder).follow_tv.setText(activity.getResources().getString(R.string.unFollow));


                    } else {
                        ((UserViewHolder) holder).follow_tv.setText(activity.getResources().getString(R.string.follow));

                    }


                    ((UserViewHolder) holder).follow_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String isFollow = model.getIsFollow();
                            if (isFollow.equalsIgnoreCase("1")) {
                                ((UserViewHolder) holder).follow_tv.setText(activity.getResources().getString(R.string.unFollow));
                                followUNfollow(model.getId());
                                model.setIsFollow("0");
                            } else {
                                followUNfollow(model.getId());
                                model.setIsFollow("1");
                                ((UserViewHolder) holder).follow_tv.setText(activity.getResources().getString(R.string.follow));
                            }

                            notifyDataSetChanged();
                        }
                    });


                    ((UserViewHolder) holder).userName_tv.setText(model.getName());


                    String jobTitle = model.getJobTitle();
                    if (jobTitle.equalsIgnoreCase("null")) {
                        ((UserViewHolder) holder).jobtitle_tv.setVisibility(View.GONE);
                    } else {
                        ((UserViewHolder) holder).jobtitle_tv.setText(jobTitle);
                    }


                    String companyName = model.getCompanyName();
                    if (companyName.equalsIgnoreCase("null")) {
                        ((UserViewHolder) holder).companyName_tv.setVisibility(View.GONE);
                        ((UserViewHolder) holder).companyContainer.setVisibility(View.GONE);
                    } else {
                        ((UserViewHolder) holder).companyName_tv.setText(companyName);
                    }


                    String imageLink = model.getImage();
                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + imageLink)
                            .fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform()).into(((UserViewHolder) holder).userImage);


                    ((UserViewHolder) holder).jobtitle_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((UserViewHolder) holder).itemView.callOnClick();
                        }
                    });


                    ((UserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String userID = GlobalClass.getPref("userID", activity);

                            Intent i = new Intent(activity, AttendeProfileActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("nature", "attendee");

                            if (userID.equalsIgnoreCase(model.getId())) {
                                bundle.putString("isMyyProfile", "1");
                            } else {
                                bundle.putString("isMyyProfile", "0");
                            }

                            bundle.putString("userID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);


                        }
                    });

                    break;

            }

        }


    }

    @Override
    public int getItemCount() {
        return leaderBoardList.size();
    }


    @Override
    public int getItemViewType(int position) {


        switch (leaderBoardList.get(position).getType()) {

            case 1:
                return LeaderBoardModel.TYEPE_SEARCH;
            case 2:
                return LeaderBoardModel.TYPE_USER;

            default:
                return -1;
        }


    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private EditText search_et;
        private RelativeLayout search_container;

        public SearchViewHolder(View itemView) {
            super(itemView);

            search_et = itemView.findViewById(R.id.search_et);
            search_container = itemView.findViewById(R.id.search_container);

        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName_tv, jobtitle_tv, companyName_tv;
        private LinearLayout companyContainer;
        private TextView
                follow_tv, points_tv;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName_tv = (TextView) itemView.findViewById(R.id.userName_tv);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            jobtitle_tv = (TextView) itemView.findViewById(R.id.jobtitle_tv);
            companyName_tv = (TextView) itemView.findViewById(R.id.companyName_tv);
            companyContainer = itemView.findViewById(R.id.companyContainer);
            follow_tv = itemView.findViewById(R.id.follow_tv);
            points_tv = itemView.findViewById(R.id.points_tv);

        }

    }

    private void followUNfollow(String followerId) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();


            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("followerId", followerId);

            WebReq.post(activity, "follow", mParams, new MyTextHttpResponseHandlerFollow());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerFollow extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerFollow() {


        }

        @Override
        public void onStart() {
            super.onStart();


        }

        @Override
        public void onFinish() {
            super.onFinish();


        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d("postFollow", mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}