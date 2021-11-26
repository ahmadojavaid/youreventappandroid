package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Activities.AgendaDetailActivity;
import com.jobesk.yea.AttendeArea.Activities.ShowNewsActivity;
import com.jobesk.yea.AttendeArea.Activities.SinglePostActivity;
import com.jobesk.yea.AttendeArea.Models.NotificationModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<NotificationModel> list;
    private Context context;
    private String TagNames;
    private String postType, like_count, comment_count, userImage;
    private View v;

    private LinearLayoutManager MyLayoutManager;


    private String body = "";

    private String Tag = "FeedAdapter";
    private String postIDdelete;


    private String isUserLiked;
    private String location_name, location_username;

    private String userID_location;
    private int location_tags_size;
    private String privacy_value;
    private String modifiedTime;
    private String bodytext;
    private Activity activity;
    private Intent likesIntent;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    String modifiedTimeEvent;

    public NotificationAdapter(Activity activity, ArrayList<NotificationModel> list) {
        this.activity = activity;
        this.list = list;


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
            case NotificationModel.TYPE_EVENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_layout_event, parent, false);
                return new EventViewHolder(view);
            case NotificationModel.TYPE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_layout_message, parent, false);
                return new MessageViewHolder(view);
            case NotificationModel.TYPE_OTHER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_layout_other, parent, false);
                return new OtherViewHolder(view);
            case NotificationModel.TYPE_NEWS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_layout_news, parent, false);
                return new NewsViewholder(view);


        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getType()) {
            case 1:
                return NotificationModel.TYPE_EVENT;
            case 2:
                return NotificationModel.TYPE_MESSAGE;
            case 3:
                return NotificationModel.TYPE_OTHER;
            case 4:
                return NotificationModel.TYPE_NEWS;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final NotificationModel notiModel = list.get(position);

        Log.d("adapterFeedtype", notiModel.getType() + "");

        if (notiModel != null) {
            switch (notiModel.getType()) {


                case NotificationModel.TYPE_EVENT:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((EventViewHolder) holder).userImage.setColorFilter(btnColor_int);
                        ((EventViewHolder) holder).event_name_tv.setTextColor(btnColor_int);
                    }


                    ((EventViewHolder) holder).event_description.setText(notiModel.getNotificationData());
                    modifiedTimeEvent = GlobalClass.parseDate(notiModel.getCreated_at(), activity);
                    ((EventViewHolder) holder).event_time_tv.setText(modifiedTimeEvent);
                    ((EventViewHolder) holder).event_name_tv.setText(notiModel.getNotificationTitle());

                    ((EventViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, AgendaDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("idSession", notiModel.getPostId());
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    });


                    break;


                case NotificationModel.TYPE_MESSAGE:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((MessageViewHolder) holder).userName.setTextColor(btnColor_int);

                    }


                    break;
                case NotificationModel.TYPE_OTHER:

                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((OtherViewHolder) holder).username_tv.setTextColor(btnColor_int);

                    }


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + notiModel.getUserImage())
                            .fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((OtherViewHolder) holder).userImage);

                    ((OtherViewHolder) holder).username_tv.setText(notiModel.getNotificationTitle());
                    String modifiedTime = GlobalClass.parseDate(notiModel.getCreated_at(), activity);
                    ((OtherViewHolder) holder).timeTV.setText(modifiedTime);
                    ((OtherViewHolder) holder).description_tv.setText(notiModel.getNotificationData());


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent postIntent = new Intent(activity, SinglePostActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("postId", notiModel.getPostId());


                            postIntent.putExtras(bundle);
                            activity.startActivity(postIntent);


                        }
                    });

                    break;
                case NotificationModel.TYPE_NEWS:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((NewsViewholder) holder).userImage.setColorFilter(btnColor_int);
                        ((NewsViewholder) holder).event_name_tv.setTextColor(btnColor_int);
                    }


                    modifiedTimeEvent = GlobalClass.parseDate(notiModel.getCreated_at(), activity);
                    ((NewsViewholder) holder).event_time_tv.setText(modifiedTimeEvent);
                    ((NewsViewholder) holder).event_name_tv.setText(notiModel.getNotificationTitle());

                    String textvalue = notiModel.getNotificationData();
                    ((NewsViewholder) holder).event_description.setText(textvalue);

                    ((NewsViewholder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, ShowNewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("bodyText", notiModel.getBody());
                            bundle.putString("headerName", notiModel.getNotificationTitle());
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    });

                    break;


            }
        }

    }

    @Override
    public int getItemCount() {

        Log.d("listSize", list.size() + "");
        return list.size();


    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView event_description, event_time_tv;
        TextView event_name_tv;

        ImageView userImage;

        public EventViewHolder(View itemView) {
            super(itemView);


            userImage = itemView.findViewById(R.id.userImage);
            event_description = itemView.findViewById(R.id.event_description);
            event_name_tv = itemView.findViewById(R.id.event_name_tv);
            event_time_tv = itemView.findViewById(R.id.timeTV);

        }

    }

    public static class NewsViewholder extends RecyclerView.ViewHolder {
        TextView event_description, event_time_tv;
        TextView event_name_tv;

        ImageView userImage;

        public NewsViewholder(View itemView) {
            super(itemView);


            userImage = itemView.findViewById(R.id.userImage);
            event_description = itemView.findViewById(R.id.event_description);
            event_name_tv = itemView.findViewById(R.id.event_name_tv);
            event_time_tv = itemView.findViewById(R.id.timeTV);

        }

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView userName;

        public MessageViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);

        }

    }


    public static class OtherViewHolder extends RecyclerView.ViewHolder {


        ImageView userImage;
        TextView username_tv, timeTV, description_tv;

        public OtherViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
//            postImage = itemView.findViewById(R.id.postImage);
            username_tv = itemView.findViewById(R.id.username_tv);
            timeTV = itemView.findViewById(R.id.timeTV);
            description_tv = itemView.findViewById(R.id.description_tv);

        }

    }


}