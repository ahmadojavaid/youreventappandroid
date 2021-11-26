package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Activities.SingleSessionDetail;
import com.jobesk.yea.AttendeArea.Models.SessionsModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;

import java.util.ArrayList;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.MyViewHolder> {

    private ArrayList<SessionsModel> sessionList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView session_venue_tv, session_name_tv, session_date;
        private ImageView calander_img;
        private LinearLayout orange_line;

        public MyViewHolder(View view) {
            super(view);


            session_venue_tv = view.findViewById(R.id.session_venue_tv);
            session_name_tv = view.findViewById(R.id.session_name_tv);
            session_date = view.findViewById(R.id.session_date);
            calander_img = view.findViewById(R.id.calander_img);
            orange_line = view.findViewById(R.id.orange_line);

        }
    }


    public SessionsAdapter(Activity activity, ArrayList<SessionsModel> sessionList) {
        this.sessionList = sessionList;
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
                .inflate(R.layout.row_sessions, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (!appMainColor.equalsIgnoreCase("")) {

            holder.session_venue_tv.setTextColor(appMainColor_int);
            holder.session_name_tv.setTextColor(appMainColor_int);
            holder.orange_line.setBackgroundColor(btnColor_int);
            holder.calander_img.setColorFilter(appMainColor_int);
        }


        final SessionsModel model = sessionList.get(position);
        holder.session_venue_tv.setText(model.getSessionVenue());
        holder.session_name_tv.setText(model.getSessionName());


        holder.session_date.setTextColor(activity.getResources().getColor(R.color.text_color_grey));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SingleSessionDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("sessionID", model.getPostID());
                i.putExtras(bundle);

                activity.startActivity(i);


            }
        });


        try {
            String toTime = model.getTimeTo();

            String[] namesList = toTime.split(":");
            String to1 = namesList[0];
            String to2 = namesList[1];

            String valueTo = to1 + ":" + to2;

            String fromTime = model.getTimeFrom();
            String[] list2 = fromTime.split(":");
            String from1 = list2[0];
            String from2 = list2[1];

            String valueFrom = from1 + ":" + from2;
            holder.session_date.setText(valueFrom + " - " + valueTo);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }
}