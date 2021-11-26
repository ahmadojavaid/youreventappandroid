package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.AttendeProfileActivity;

import com.jobesk.yea.AttendeArea.EventBuses.AttendeeEventBus;

import com.jobesk.yea.AttendeArea.EventBuses.EventBusOpenFilter;
import com.jobesk.yea.AttendeArea.Models.AttendeModel;

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

public class AttendeesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AttendeModel> arrayList;
    Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;


    public AttendeesAdapter(Activity activity, ArrayList<AttendeModel> arrayList) {
        this.arrayList = arrayList;
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
            case AttendeModel.TYEPE_SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
                return new SearchViewHolder(view);
            case AttendeModel.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendees, parent, false);
                return new UserViewHolder(view);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final AttendeModel model = arrayList.get(position);


        if (model != null) {
            switch (model.getType()) {

                case AttendeModel.TYEPE_SEARCH:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((SearchViewHolder) holder).search_container.setBackgroundColor(btnColor_int);

                    }

                    ((SearchViewHolder) holder).search_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            Log.d("inputCount", s.length() + "");
                            int countOFChars = s.length();
                            if (countOFChars == 0) {
                                EventBus.getDefault().post(new AttendeeEventBus(""));
                            }

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            // TODO Auto-generated method stub
                        }
                    });
                    ((SearchViewHolder) holder).search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                                String keywordSearch = ((SearchViewHolder) holder).search_et.getText().toString().trim();
                                if (keywordSearch.equalsIgnoreCase("")) {

                                    Toast.makeText(activity, activity.getApplicationContext().getResources().getString(R.string.enter_search_title), Toast.LENGTH_SHORT).show();


                                } else {

                                    EventBus.getDefault().post(new AttendeeEventBus(keywordSearch));


                                }


                                return true;
                            }
                            return false;
                        }
                    });
                    ((SearchViewHolder) holder).filter_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            EventBus.getDefault().post(new EventBusOpenFilter("1"));


                        }
                    });


                    break;
                case AttendeModel.TYPE_USER:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((UserViewHolder) holder).username_tv.setTextColor(btnColor_int);
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
                                followUNfollow(model.getID());
                                model.setIsFollow("0");
                            } else {
                                followUNfollow(model.getID());
                                model.setIsFollow("1");
                                ((UserViewHolder) holder).follow_tv.setText(activity.getResources().getString(R.string.follow));
                            }

                            notifyDataSetChanged();
                        }
                    });

                    ((UserViewHolder) holder).username_tv.setText(model.getName() + " " + model.getSurName());


                    String jobTitle = model.getJobTitle();
                    if (!jobTitle.equalsIgnoreCase("null")) {

                        ((UserViewHolder) holder).job_title_tv.setText(jobTitle);
                    }


                    String company = model.getCompanyName();
                    if (!company.equalsIgnoreCase("null")) {

                        ((UserViewHolder) holder).companyName_tv.setText(company);
                    }


                    String imageLink = Urls.BASE_URL_IMAGE + model.getImage();


                    Picasso.with(activity)
                            .load(imageLink)
                            .fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((UserViewHolder) holder).userImage);


                    ((UserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, AttendeProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("isMyyProfile", "0");
                            bundle.putString("nature", "attendee");
                            bundle.putString("userID", model.getID());
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
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {


        switch (arrayList.get(position).getType()) {

            case 1:
                return AttendeModel.TYEPE_SEARCH;
            case 2:
                return AttendeModel.TYPE_USER;

            default:
                return -1;
        }


    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private EditText search_et;
        private RelativeLayout search_container;
        private ImageView filter_img;

        public SearchViewHolder(View itemView) {
            super(itemView);

            search_et = itemView.findViewById(R.id.search_et);
            search_container = itemView.findViewById(R.id.search_container);


            filter_img = itemView.findViewById(R.id.filter_img);

        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView username_tv, job_title_tv, companyName_tv, follow_tv;
        private ImageView userImage;


        public UserViewHolder(View itemView) {
            super(itemView);
            username_tv = (TextView) itemView.findViewById(R.id.username_tv);
            job_title_tv = (TextView) itemView.findViewById(R.id.job_title_tv);
            companyName_tv = (TextView) itemView.findViewById(R.id.companyName_tv);

            userImage = itemView.findViewById(R.id.userImage);

            follow_tv = itemView.findViewById(R.id.follow_tv);

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