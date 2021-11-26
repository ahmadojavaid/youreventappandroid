package com.jobesk.yea.AttendeArea.Activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.CommentAdapter;

import com.jobesk.yea.AttendeArea.EventBuses.FeedGetCommentsEvent;
import com.jobesk.yea.AttendeArea.Models.CommentsModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentActivity extends AppCompatActivity {

    private ImageView back_img;
    private TextView toolbar_title_tv;
    private RecyclerView recyclerView;
    private CommentAdapter mAdapter;
    private ArrayList<CommentsModel> commentList = new ArrayList();
    private String TAG = "CommentActivity";
    private TextView send_tv;
    private EditText comment_et;
    private String commentTxt;
    private String postID;
    private String userID;
    private int checkFirstTime = 1;
    private String position;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userID = GlobalClass.getPref("userID", getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        postID = bundle.getString("postID");
        position = bundle.getString("position");


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.comments));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CommentAdapter(CommentActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getComments();

        comment_et = findViewById(R.id.comment_et);
        send_tv = findViewById(R.id.send_tv);
        send_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentTxt = comment_et.getText().toString().trim();

                if (commentTxt.equalsIgnoreCase("")) {

                    Toast.makeText(CommentActivity.this, getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                PostComment();


                mAdapter.notifyDataSetChanged();


            }
        });



        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);



        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            send_tv.setBackgroundColor(appMainColor_int);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


    }

    private void PostComment() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("userId", userID);
            mParams.put("postId", postID);
            mParams.put("comment", commentTxt);

            WebReq.post(getApplicationContext(), "comment", mParams, new MyTextHttpResponseHandlerPostComment());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerPostComment extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerPostComment() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(CommentActivity.this);
            Log.d(TAG, "onStart");
            comment_et.setText("");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {
                        EventBus.getDefault().post(new FeedGetCommentsEvent(Integer.valueOf(position),Integer.valueOf(postID) ));
                        getComments();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(CommentActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void getComments() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);

            WebReq.get(getApplicationContext(), "showcomment?postId=" + postID + "&userId=" + userID, mParams, new MyTextHttpResponseHandlerGetComments());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetComments extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerGetComments() {

        }

        @Override
        public void onStart() {
            super.onStart();

            if (checkFirstTime == 1) {
                GlobalClass.showLoading(CommentActivity.this);
                checkFirstTime = 0;
            }


            if (commentList.size() > 0) {
                commentList.clear();
            }
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String commentID = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String profileImage = jsonObject.getString("profileImage");
                                String userId = jsonObject.getString("userId");
                                String postId = jsonObject.getString("postId");
                                String comment = jsonObject.getString("comment");
                                String likeCount = jsonObject.getString("commentlikesCount");
                                String isLiked = jsonObject.getString("isLikedTheComment");
                                String created_at = jsonObject.getString("created_at");


                                CommentsModel model = new CommentsModel();
                                model.setCommentID(commentID);
                                model.setImage(profileImage);
                                model.setUserID(userId);
                                model.setPostID(postId);
                                model.setComment(comment);
                                model.setTotlaLikes(likeCount);
                                model.setIsLike(isLiked);
                                model.setDate(created_at);
                                model.setName(name);

                                commentList.add(model);


                            }
                            mAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(commentList.size() - 1);
                        } else {
                            Toast.makeText(CommentActivity.this, getApplicationContext().getResources().getString(R.string.no_comment_found), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(CommentActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
