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

import com.jobesk.yea.AttendeArea.Activities.AgendaDetailActivity;
import com.jobesk.yea.AttendeArea.Models.AgendaModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;

import java.util.ArrayList;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

    private ArrayList<AgendaModel> arryList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_agenda, location_tv, time_tv;
        private ImageView calanderImage, location_img;
        private LinearLayout vertical_line_ln;

        public MyViewHolder(View view) {
            super(view);

            title_agenda = view.findViewById(R.id.title_agenda);
            location_tv = view.findViewById(R.id.location_tv);
            time_tv = view.findViewById(R.id.time_tv);
            calanderImage = view.findViewById(R.id.calanderImage);
            location_img = view.findViewById(R.id.location_img);
            vertical_line_ln = view.findViewById(R.id.vertical_line_ln);

        }
    }


    public AgendaAdapter(Activity activity, ArrayList<AgendaModel> arryList) {
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
                .inflate(R.layout.row_agenda, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AgendaModel model = arryList.get(position);


        if (!appMainColor.equalsIgnoreCase("")) {


            holder.location_img.setColorFilter(btnColor_int);
            holder.calanderImage.setColorFilter(btnColor_int);
            holder.vertical_line_ln.setBackgroundColor(btnColor_int);
        }

        holder.title_agenda.setText(model.getSessionName());
        holder.location_tv.setText(model.getSessionVenue());


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

            holder.time_tv.setText(valueFrom + " - " + valueTo);
        } catch (Exception e) {
            e.printStackTrace();

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AgendaDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idSession", model.getSessionID());

                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arryList.size();
    }
}