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
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Activities.SpeakerDeatilActivity;
import com.jobesk.yea.AttendeArea.Models.AgendaSpeakerModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AgendaSpeakersAdapter extends RecyclerView.Adapter<AgendaSpeakersAdapter.MyViewHolder> {

    private ArrayList<AgendaSpeakerModel> speakersLList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView calenderimg, imageView;

        private TextView useName_tv, companyName_tv, occupation_tv;

        public MyViewHolder(View view) {
            super(view);
            useName_tv = (TextView) view.findViewById(R.id.useName_tv);
            companyName_tv = (TextView) view.findViewById(R.id.companyName_tv);
            occupation_tv = (TextView) view.findViewById(R.id.occupation_tv);


            imageView = view.findViewById(R.id.imageView);
            calenderimg = view.findViewById(R.id.calenderimg);


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
    }


    public AgendaSpeakersAdapter(Activity activity, ArrayList<AgendaSpeakerModel> speakersLList) {
        this.speakersLList = speakersLList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_agenda_speaker, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AgendaSpeakerModel model = speakersLList.get(position);


        if (!appMainColor.equalsIgnoreCase("")) {

            holder.companyName_tv.setTextColor(btnColor_int);

            holder.calenderimg.setColorFilter(btnColor_int);
        }

        holder.useName_tv.setText(model.getSpeakerName());
        holder.companyName_tv.setText(model.getSpeakerCompanyName());
        holder.occupation_tv.setText(model.getSpeakerOccupation());


        Picasso.with(activity)
                .load(Urls.BASE_URL_IMAGE + model.getSpeakerProfileImage())
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform()).into(holder.imageView);


        holder.useName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.callOnClick();
            }
        });
        holder.occupation_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.callOnClick();
            }
        });
        holder.companyName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.callOnClick();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(activity, SpeakerDeatilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("speakerID", model.getSpeakerId());
                i.putExtras(bundle);

                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return speakersLList.size();
    }
}