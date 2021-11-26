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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.SpeakerDeatilActivity;
import com.jobesk.yea.AttendeArea.EventBuses.SpeakersEventBus;

import com.jobesk.yea.AttendeArea.Models.SpeakersModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SpeakersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SpeakersModel> speakersLList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public SpeakersAdapter(Activity activity, ArrayList<SpeakersModel> speakersLList) {
        this.speakersLList = speakersLList;
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
            case SpeakersModel.TYEPE_SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
                return new SearchViewHolder(view);
            case SpeakersModel.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_speakers, parent, false);
                return new UserViewHolder(view);


        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final SpeakersModel model = speakersLList.get(position);


        if (model != null) {
            switch (model.getType()) {

                case SpeakersModel.TYEPE_SEARCH:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((SearchViewHolder) holder).search_container.setBackgroundColor(btnColor_int);

                    }


                    ((SearchViewHolder) holder).search_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            Log.d("inputCount", s.length() + "");
                            int countOFChars = s.length();
                            if (countOFChars == 0) {
                                EventBus.getDefault().post(new SpeakersEventBus(""));
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

                                    EventBus.getDefault().post(new SpeakersEventBus(keywordSearch));


                                }


                                return true;
                            }
                            return false;
                        }
                    });

                    break;
                case SpeakersModel.TYPE_USER:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((UserViewHolder) holder).useName_tv.setTextColor(btnColor_int);
                        ((UserViewHolder) holder).companyName_tv.setTextColor(btnColor_int);

                        ((UserViewHolder) holder).contact_icon.setColorFilter(btnColor_int);


                    }
                    ((UserViewHolder) holder).useName_tv.setText(model.getSpeakerName());
                    ((UserViewHolder) holder).companyName_tv.setText(model.getSpeakerCompanyName());
                    ((UserViewHolder) holder).occupation_tv.setText(model.getSpeakerOccupation());


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + model.getSpeakerProfileImage())
                            .fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform()).into(((UserViewHolder) holder).imageView);


                    ((UserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);

                            activity.startActivity(i);
                        }
                    });
                    ((UserViewHolder) holder).row_speakers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });
                    ((UserViewHolder) holder).useName_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });
                    ((UserViewHolder) holder).companyName_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });
                    ((UserViewHolder) holder).occupation_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });
                    ((UserViewHolder) holder).contact_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });


                    ((UserViewHolder) holder).companyNameContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("speakerID", model.getId());
                            i.putExtras(bundle);
                            activity.startActivity(i);

                        }
                    });
                    ((UserViewHolder) holder).occupation_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((UserViewHolder) holder).companyNameContainer.callOnClick();

                        }
                    });

                    break;

            }

        }


    }

    @Override
    public int getItemCount() {
        return speakersLList.size();
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

        private ImageView imageView, contact_icon;
        private TextView useName_tv, companyName_tv, occupation_tv;
        private RelativeLayout row_speakers;
        private LinearLayout companyNameContainer;


        public UserViewHolder(View itemView) {
            super(itemView);
            useName_tv = (TextView) itemView.findViewById(R.id.useName_tv);
            companyName_tv = (TextView) itemView.findViewById(R.id.companyName_tv);
            occupation_tv = (TextView) itemView.findViewById(R.id.occupation_tv);


            contact_icon = itemView.findViewById(R.id.contact_icon);
            imageView = itemView.findViewById(R.id.imageView);
            row_speakers = itemView.findViewById(R.id.row_speakers);

            companyNameContainer = itemView.findViewById(R.id.companyNameContainer);

        }

    }

    @Override
    public int getItemViewType(int position) {


        switch (speakersLList.get(position).getType()) {

            case 1:
                return SpeakersModel.TYEPE_SEARCH;
            case 2:
                return SpeakersModel.TYPE_USER;

            default:
                return -1;
        }


    }
}