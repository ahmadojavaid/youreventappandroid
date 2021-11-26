package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Models.CommentsModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private ArrayList<CommentsModel> commentList;
    private Activity activity;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private int statusBarColor_int, appMainColor_int, btnColor_int;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName_tv, comment_tv, time_tv, total_like_tv;
        ImageView userImage, likeImg;

        public MyViewHolder(View view) {
            super(view);
            userName_tv = (TextView) view.findViewById(R.id.userName_tv);
            comment_tv = (TextView) view.findViewById(R.id.comment_tv);
            time_tv = (TextView) view.findViewById(R.id.time_tv);
            total_like_tv = (TextView) view.findViewById(R.id.total_like_tv);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            likeImg = (ImageView) view.findViewById(R.id.likeImg);


        }
    }


    public CommentAdapter(Activity activity, ArrayList<CommentsModel> commentList) {
        this.commentList = commentList;
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
                .inflate(R.layout.row_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        if (!appMainColor.equalsIgnoreCase("")) {
            holder.userName_tv.setTextColor(btnColor_int);
        }


        final CommentsModel model = commentList.get(position);
        holder.userName_tv.setText(model.getName());
        holder.comment_tv.setText(model.getComment());

        holder.total_like_tv.setText(model.getTotlaLikes());


        String modifiedTime = GlobalClass.parseDate(model.getDate(), activity);
        holder.time_tv.setText(modifiedTime + "");

        Picasso.with(activity)
                .load(Urls.BASE_URL_IMAGE + model.getImage())
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(holder.userImage);


        String isLiked = model.getIsLike();
        if (isLiked.equalsIgnoreCase("0")) {
            holder.likeImg.setImageResource(R.drawable.ic_unlike);

        } else {
            holder.likeImg.setImageResource(R.drawable.ic_like);
        }


        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isLiked = model.getIsLike();
                String likeCount = model.getTotlaLikes();

                if (isLiked.equalsIgnoreCase("1")) {

                    int newLikeCount = Integer.valueOf(likeCount) - 1;

                    model.setTotlaLikes(String.valueOf(newLikeCount));
                    model.setIsLike("0");

                    holder.likeImg.setImageResource(R.drawable.ic_unlike);
                    LikeComment(model.getCommentID());
                } else {

                    int newLikeCount = Integer.valueOf(likeCount) + 1;

                    model.setTotlaLikes(String.valueOf(newLikeCount));
                    model.setIsLike("1");

                    holder.likeImg.setImageResource(R.drawable.ic_like);
                    LikeComment(model.getCommentID());
                }

                notifyDataSetChanged();
            }
        });


    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void LikeComment(String commentId) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("commentId", commentId);


            WebReq.post(activity, "likeToComment", mParams, new MyTextHttpResponseHandlerPostLike());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerPostLike extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerPostLike() {


        }

        @Override
        public void onStart() {
            super.onStart();


        }

        @Override
        public void onFinish() {
            super.onFinish();


        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d("postLike", mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}