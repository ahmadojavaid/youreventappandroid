package com.jobesk.yea.SponsorArea.MeetingMenu.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Models.AAvaliableModel;
import com.jobesk.yea.SponsorArea.MeetingMenu.Activities.SponsorCancelledDetailActivity;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SponsorMeetingsCancelledAdapter extends RecyclerView.Adapter<SponsorMeetingsCancelledAdapter.MyViewHolder> {

    private ArrayList<AAvaliableModel> arryList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        TextView username_tv, time_tv;

        public MyViewHolder(View view) {
            super(view);

            username_tv = view.findViewById(R.id.username_tv);
            time_tv = view.findViewById(R.id.time_tv);


            userImage = view.findViewById(R.id.userImage);


        }
    }


    public SponsorMeetingsCancelledAdapter(Activity activity, ArrayList<AAvaliableModel> arryList) {
        this.arryList = arryList;
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sponsor_meeting_requests, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AAvaliableModel model = arryList.get(position);

        holder.username_tv.setText(model.getName() + "");
        final String from = model.getFrom();
        final String to = model.getTo();
        holder.time_tv.setText(from + " to " + to);


        Picasso.with(activity)
                .load(Urls.BASE_URL_IMAGE + "" + model.getImage())
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(holder.userImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(activity, SponsorCancelledDetailActivity.class);


                i.putExtra("time", model.getFrom() + " to " + model.getTo());
                i.putExtra("timeID", model.getTime_id());
                i.putExtra("attendeMsg", model.getAttendee_msg());
                i.putExtra("sponsor_msg", model.getSponsor_msg());
                activity.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arryList.size();
    }
}