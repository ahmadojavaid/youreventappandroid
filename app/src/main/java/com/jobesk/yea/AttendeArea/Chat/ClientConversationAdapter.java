package com.jobesk.yea.AttendeArea.Chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientConversationAdapter extends RecyclerView.Adapter<ClientConversationAdapter.MyViewHolder> {


    private Activity activity;
    private ArrayList<GetconversationModel> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_tv, body_tv, userName_tv;
        ImageView userImage;

        public MyViewHolder(View view) {
            super(view);
            date_tv = (TextView) view.findViewById(R.id.date_tv);
            body_tv = (TextView) view.findViewById(R.id.body_tv);
            userName_tv = (TextView) view.findViewById(R.id.userName_tv);
            userImage = (ImageView) view.findViewById(R.id.userImage);

        }
    }


    public ClientConversationAdapter(Activity activity, ArrayList<GetconversationModel> arrayList) {
        this.arrayList = arrayList;

        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_row_conversation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GetconversationModel model = arrayList.get(position);
        String date = model.getDate();

        holder.date_tv.setText(GlobalClass.parseDate(date, activity));
        holder.body_tv.setText(model.getMessage());
        holder.userName_tv.setText(model.getReceivername());

        Picasso.with(activity)
                .load(Urls.BASE_URL_IMAGE + model.getReceiverImage())
                .transform(new CircleTransform())
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .into((holder.userImage));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, GetAllChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("userID", model.getReceiverID());
                bundle.putString("userimage", model.getReceiverImage());
                bundle.putString("userName", model.getReceivername());

                intent.putExtras(bundle);
                activity.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}