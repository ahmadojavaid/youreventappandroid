package com.jobesk.yea.AttendeArea.Chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;

import java.util.ArrayList;

public class ClientChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ClientGetChatMessagesModel> chatArrayList;

    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private Context context;

    public static class LeftRow extends RecyclerView.ViewHolder {
        private TextView message_tv_left, date_tv_left;


        public LeftRow(View itemView) {
            super(itemView);
            message_tv_left = (TextView) itemView.findViewById(R.id.message_tv);
            date_tv_left = (TextView) itemView.findViewById(R.id.date_tv);


        }

    }

    public static class RightRow extends RecyclerView.ViewHolder {
        private RelativeLayout rightlayout_re;
        private TextView message_tv_right, date_tv_right;

        public RightRow(View itemView) {
            super(itemView);

            message_tv_right = (TextView) itemView.findViewById(R.id.message_tv);
            date_tv_right = (TextView) itemView.findViewById(R.id.date_tv);
            rightlayout_re = itemView.findViewById(R.id.rightlayout_re);
        }

    }


    public ClientChatAdapter(Activity activity, ArrayList<ClientGetChatMessagesModel> chatArrayList, Context context) {
        this.chatArrayList = chatArrayList;
        this.context = context;

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
            case ClientGetChatMessagesModel.TYPE_left:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_left, parent, false);
                return new LeftRow(view);
            case ClientGetChatMessagesModel.TYPE_right:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_right, parent, false);
                return new RightRow(view);

        }
        return null;


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ClientGetChatMessagesModel object = chatArrayList.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case ClientGetChatMessagesModel.TYPE_left:
                    ((LeftRow) holder).message_tv_left.setText(object.getMessage());

                    String date1 = GlobalClass.parseDate(object.getCreated_at(), context);

                    ((LeftRow) holder).date_tv_left.setText(date1);
                    break;
                case ClientGetChatMessagesModel.TYPE_right:



                    ((RightRow) holder).message_tv_right.setText(object.getMessage());
                    String date2 = GlobalClass.parseDate(object.getCreated_at(), context);

                    ((RightRow) holder).date_tv_right.setText(date2);
                    break;

            }
        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (chatArrayList.get(position).type) {
            case 1:
                return ClientGetChatMessagesModel.TYPE_left;
            case 2:
                return ClientGetChatMessagesModel.TYPE_right;

            default:
                return -1;
        }


    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }
}
