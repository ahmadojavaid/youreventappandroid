package com.jobesk.yea.SponsorArea.AttendeeMenu.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Models.AAvaliableModel;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.AttendeeMenu.DetailActivities.SponsorTimeNotConfirmDetails;
import com.jobesk.yea.Utils.GlobalClass;

import java.util.ArrayList;

public class SponsorTimeNotConfirmedAdapter extends RecyclerView.Adapter<SponsorTimeNotConfirmedAdapter.MyViewHolder> {

    private ArrayList<AAvaliableModel> arryList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView username_tv;

        public MyViewHolder(View view) {
            super(view);

            username_tv = view.findViewById(R.id.username_tv);


        }
    }


    public SponsorTimeNotConfirmedAdapter(Activity activity, ArrayList<AAvaliableModel> arryList) {
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
                .inflate(R.layout.row_available, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AAvaliableModel model = arryList.get(position);


        String from = model.getFrom();
        String to = model.getTo();


        holder.username_tv.setText(from + " to " + to);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(activity, SponsorTimeNotConfirmDetails.class);
                i.putExtra("time", model.getFrom() + " to " + model.getTo());
                i.putExtra("timeID", model.getTime_id());
                i.putExtra("timeStatus", model.getStatus());
                i.putExtra("attendeMsg", model.getAttendee_msg());
                i.putExtra("sponsorMsg", model.getSponsor_msg());
                activity.startActivity(i);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arryList.size();
    }
}