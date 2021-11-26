package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Models.AgendaDocModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;

import java.util.ArrayList;

public class AgendaSessionsSingleAdapter extends RecyclerView.Adapter<AgendaSessionsSingleAdapter.MyViewHolder> {

    private ArrayList<AgendaDocModel> arrayList;

    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_tv;
        private ImageView ic_eye;
        private HorizontalScrollView scroll_1;

        public MyViewHolder(View view) {
            super(view);
            title_tv = (TextView) view.findViewById(R.id.title_tv);
            ic_eye = (ImageView) view.findViewById(R.id.ic_eye);
            scroll_1 = view.findViewById(R.id.scroll_1);

        }
    }


    public AgendaSessionsSingleAdapter(Activity activity, ArrayList<AgendaDocModel> arrayList) {
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
                .inflate(R.layout.row_single_session, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AgendaDocModel model = arrayList.get(position);
        holder.title_tv.setText(model.getDocname());

        if (!appMainColor.equalsIgnoreCase("")) {
            holder.ic_eye.setColorFilter(btnColor_int);
        }


        holder.title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.callOnClick();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pdfUrl = Urls.BASE_URL_IMAGE + model.getDocattachementURl();
                Log.d("pdfUrl", pdfUrl + "");
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}