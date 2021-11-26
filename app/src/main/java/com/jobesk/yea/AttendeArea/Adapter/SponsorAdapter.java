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

import com.jobesk.yea.AttendeArea.Activities.SponsorDetailActivity;

import com.jobesk.yea.AttendeArea.EventBuses.SponsorEventBus;
import com.jobesk.yea.AttendeArea.Models.SponsorModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SponsorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SponsorModel> arrayList;
    private  Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public SponsorAdapter(Activity activity, ArrayList<SponsorModel> arrayList) {
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
            case SponsorModel.TYEPE_SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
                return new SearchViewHolder(view);
            case SponsorModel.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sponsor, parent, false);
                return new UserViewHolder(view);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final SponsorModel model = arrayList.get(position);


        if (model != null) {
            switch (model.getType()) {

                case SponsorModel.TYEPE_SEARCH:
                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((SearchViewHolder) holder).search_container.setBackgroundColor(btnColor_int);

                    }

                    ((SearchViewHolder) holder).search_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            Log.d("inputCount",s.length()+"");
                            int countOFChars=s.length();
                            if (countOFChars==0){
                                EventBus.getDefault().post(new SponsorEventBus(""));
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

                                    EventBus.getDefault().post(new SponsorEventBus(keywordSearch));


                                }


                                return true;
                            }
                            return false;
                        }
                    });

                    break;
                case SponsorModel.TYPE_USER:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((UserViewHolder) holder).sponser_name_tv.setTextColor(btnColor_int);
                        ((UserViewHolder) holder).sponsorShiplvl_tv.setTextColor(btnColor_int);




                    }


//                    String level = model.getLevel();
//                    if (level.equalsIgnoreCase("gold")) {
//
//                        ((UserViewHolder) holder).medal_icon.setImageResource(R.drawable.ic_gold);
//
//                    }
//                    if (level.equalsIgnoreCase("silver")) {
//
//                        ((UserViewHolder) holder).medal_icon.setImageResource(R.drawable.ic_silver);
//
//                    }
//                    if (level.equalsIgnoreCase("platinum")) {
//
//                        ((UserViewHolder) holder).medal_icon.setImageResource(R.drawable.ic_platinum);
//
//                    }
//

                    ((UserViewHolder) holder).sponser_name_tv.setText(model.getSponserName());
                    ((UserViewHolder) holder).sponsorShiplvl_tv.setText(model.getLevel());
                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + model.getSponsorImage())
                            .fit().centerCrop()
                            .placeholder(R.drawable.building_placeholder)
                            .into(((UserViewHolder) holder).userImage);


                    ((UserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent i = new Intent(activity, SponsorDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("SponsorID", model.getSponsorID());

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
        public TextView sponser_name_tv, sponsorShiplvl_tv;
        ImageView  userImage;

        public UserViewHolder(View itemView) {
            super(itemView);
//            medal_icon = (ImageView) itemView.findViewById(R.id.medal_icon);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            sponser_name_tv = (TextView) itemView.findViewById(R.id.sponser_name_tv);
            sponsorShiplvl_tv = (TextView) itemView.findViewById(R.id.sponsorShiplvl_tv);
        }

    }


    @Override
    public int getItemViewType(int position) {


        switch (arrayList.get(position).getType()) {

            case 1:
                return SponsorModel.TYEPE_SEARCH;
            case 2:
                return SponsorModel.TYPE_USER;

            default:
                return -1;
        }


    }
}