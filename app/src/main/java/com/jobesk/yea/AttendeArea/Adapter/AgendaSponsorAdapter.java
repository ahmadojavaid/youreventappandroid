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

import com.jobesk.yea.AttendeArea.Activities.SponsorDetailActivity;
import com.jobesk.yea.AttendeArea.Models.AgendaSponsorModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AgendaSponsorAdapter extends RecyclerView.Adapter<AgendaSponsorAdapter.MyViewHolder> {

    private ArrayList<AgendaSponsorModel> arrayList;
    private  Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sponser_name_tv, sponsorShiplvl_tv;
        ImageView  userImage;

        public MyViewHolder(View view) {
            super(view);

            userImage = (ImageView) view.findViewById(R.id.userImage);
            sponser_name_tv = (TextView) view.findViewById(R.id.sponser_name_tv);
            sponsorShiplvl_tv = (TextView) view.findViewById(R.id.sponsorShiplvl_tv);

        }
    }


    public AgendaSponsorAdapter(Activity activity, ArrayList<AgendaSponsorModel> arrayList) {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_agenda_sponsor, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AgendaSponsorModel model = arrayList.get(position);
//        holder.title.setText(movie.getTitle());
//        holder.genre.setText(movie.getGenre());
//        holder.year.setText(movie.getYear());


        holder.sponser_name_tv.setText(model.getSponsorName());
        holder.sponsorShiplvl_tv.setText(model.getSponsorshipLevel());
        Picasso.with(activity)
                .load(Urls.BASE_URL_IMAGE + model.getSponsorImage())
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .into(holder.userImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(activity, SponsorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SponsorID", model.getSponsorID());

                i.putExtras(bundle);

                activity.startActivity(i);
            }
        });




        if (!appMainColor.equalsIgnoreCase("")) {



            holder.sponser_name_tv.setTextColor(btnColor_int);
            holder.sponsorShiplvl_tv.setTextColor(btnColor_int);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}