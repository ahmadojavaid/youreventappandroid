package com.jobesk.yea.SponsorArea;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jobesk.yea.AttendeArea.EventBuses.AttendeCommentsEvent;
import com.jobesk.yea.AttendeArea.Models.CommentsModel;
import com.jobesk.yea.AttendeArea.Models.FeedModel;
import com.jobesk.yea.AttendeArea.Models.HeaderModel;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.Adapters.SFeedAdapter;
import com.jobesk.yea.SponsorArea.AttendeeMenu.SponsorAttendeeMain;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class SAttendeProfileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView back_img;
    private TextView toolbar_title_tv;
    private SFeedAdapter profileAdapterFeed;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private FeedModel model;
    private ArrayList<FeedModel> FeedListData = new ArrayList<>();
    private String myProfileStaus;
    private String TAG = "AttendeProfileActivity";
    private String otherUserID, userName, profileImage, companyName, jobTitle, followersCount, followingCount, isfollowed;
    private String postCount, userIDCurrent;
    private String postID, userID, descriptionPost, postLikeCount, postCommentCount, postsharecount, postType, postTime, isLiked, userNameRes, userImage;
    private ArrayList<HeaderModel> headerList = new ArrayList<>();
    private ArrayList<String> attachments;
    private ArrayList<CommentsModel> commentList;
    private String userIDpre;
    private String nature;
    private boolean refresh = false;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private ImageView calander_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_attende_profile);


        back_img = findViewById(R.id.back_img);
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);


        Bundle bundle = getIntent().getExtras();
        userIDpre = bundle.getString("userID");
        myProfileStaus = bundle.getString("isMyyProfile");
        nature = bundle.getString("nature");


        calander_img = findViewById(R.id.calander_img);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userIDCurrent = GlobalClass.getPref("userID", getApplicationContext());


        refreshLayout = findViewById(R.id.feed_swipe);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recycler_feed);


        profileAdapterFeed = new SFeedAdapter(SAttendeProfileActivity.this, FeedListData, Integer.valueOf(myProfileStaus), headerList, nature);
        layoutManager = new LinearLayoutManager(SAttendeProfileActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        DividerItemDecoration divider = new DividerItemDecoration(AttendeProfileActivity.this, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(AttendeProfileActivity.this, R.drawable.line_divider));
//        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(profileAdapterFeed);

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


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor_int);
            }

            back_img.setColorFilter(btnColor_int);
            calander_img.setColorFilter(btnColor_int);
            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        calander_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GlobalClass.isOnline(getApplicationContext()) == true) {


                    Intent i = new Intent(SAttendeProfileActivity.this, SponsorAttendeeMain.class);
                    i.putExtra("attendeID", otherUserID);
                    startActivity(i);

                } else {
                    Toast.makeText(SAttendeProfileActivity.this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                }


            }
        });


        getFeedAttendee();
    }

    private void getFeedAttendee() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);


            if (myProfileStaus.equalsIgnoreCase("1")) {
                WebReq.get(getApplicationContext(), "user/" + userIDpre, mParams, new MyTextHttpResponseHandlerGETFEedAttendee());
            } else {
                WebReq.get(getApplicationContext(), "user/" + userIDpre + "?userId=" + userIDCurrent, mParams, new MyTextHttpResponseHandlerGETFEedAttendee());
            }


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRefresh() {


        refresh = true;
        refreshLayout.setRefreshing(true);

        if (FeedListData.size() > 0) {
            FeedListData.clear();
            profileAdapterFeed.notifyDataSetChanged();
        }

        getFeedAttendee();
    }


    private class MyTextHttpResponseHandlerGETFEedAttendee extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerGETFEedAttendee() {


        }

        @Override
        public void onStart() {
            super.onStart();
            if (refresh == true) {

            } else {
                GlobalClass.showLoading(SAttendeProfileActivity.this);
            }


            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            refreshLayout.setRefreshing(false);
            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        if (headerList.size() > 0) {
                            headerList.clear();
                        }

                        JSONArray jsonArray = mResponse.getJSONArray("Result");
                        if (jsonArray.length() > 0) {

                            model = new FeedModel();
                            model.setType(FeedModel.USER_INFO_TYPE);
                            FeedListData.add(model);


                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                otherUserID = jsonObject.getString("id");
                                userName = jsonObject.getString("name");
                                profileImage = jsonObject.getString("profileImage");
                                companyName = jsonObject.getString("companyName");
                                jobTitle = jsonObject.getString("jobTitle");
                                followersCount = jsonObject.getString("followersCount");
                                followingCount = jsonObject.getString("followingCount");
                                isfollowed = jsonObject.getString("isfollowed");
                                postCount = jsonObject.getString("postCount");


                                toolbar_title_tv.setText(userName);
                                HeaderModel model = new HeaderModel();
                                model.setUserName(userName);
                                model.setProfileImage(profileImage);
                                model.setCompanyName(companyName);
                                model.setJobTitle(jobTitle);
                                model.setFollowersCount(followersCount);
                                model.setFollowingCount(followingCount);
                                model.setIsfollowed(isfollowed);
                                model.setPostCount(postCount);
                                model.setId(otherUserID);
                                headerList.add(model);
// get all posts

                                JSONArray jsonArrayPosts = jsonObject.getJSONArray("posts");

                                if (jsonArrayPosts.length() > 0) {


                                    for (int j = 0; j < jsonArrayPosts.length(); j++) {


                                        JSONObject jsonObjectPosts = jsonArrayPosts.getJSONObject(j);
                                        postID = jsonObjectPosts.getString("id");
                                        userID = jsonObjectPosts.getString("userId");
                                        descriptionPost = jsonObjectPosts.getString("postDescription");
                                        postLikeCount = jsonObjectPosts.getString("postLikesCount");
                                        postCommentCount = jsonObjectPosts.getString("postCommentsCount");
                                        postsharecount = jsonObjectPosts.getString("postSharesCount");
                                        postType = jsonObjectPosts.getString("postMediaType");
                                        postTime = jsonObjectPosts.getString("created_at");
                                        isLiked = jsonObjectPosts.getString("isLiked");
//                                        userNameRes = jsonObjectPosts.getString("name");
                                        userNameRes = userName;
//                                        userImage = jsonObjectPosts.getString("profileImage");
                                        userImage = profileImage;


                                        FeedModel feedModel = new FeedModel();
                                        feedModel.setPostID(postID);
                                        feedModel.setUserId(userID);
                                        feedModel.setPostDescription(descriptionPost);
                                        feedModel.setPostLikesCount(postLikeCount);
                                        feedModel.setPostCommentsCount(postCommentCount);
                                        feedModel.setPostSharesCount(postsharecount);
                                        feedModel.setPostMediaType(postType);
                                        feedModel.setDate(postTime);
                                        feedModel.setIsLiked(isLiked);
                                        feedModel.setName(userName);
                                        feedModel.setImage(userImage);
                                        feedModel.setIsPined("0");

                                        JSONArray attachmentArray = jsonObjectPosts.getJSONArray("postattachments");
                                        int imageArryCount = attachmentArray.length();
                                        if (attachmentArray.length() > 0) {
                                            feedModel.setPostTotalImages(String.valueOf(imageArryCount));
                                            attachments = new ArrayList<>();
                                            for (int k = 0; k < attachmentArray.length(); k++) {
                                                JSONObject jsonObject1 = attachmentArray.getJSONObject(k);
                                                if (postType.equalsIgnoreCase("video")) {
                                                    String videoUrl = jsonObject1.getString("attachmentURL");
                                                    attachments.add(Urls.BASEURL + videoUrl);
                                                } else {
                                                    String imageUrl = jsonObject1.getString("attachmentURL");
                                                    attachments.add(Urls.BASE_URL_IMAGE + imageUrl);

                                                }


                                            }


                                        }


                                        if (postType.equalsIgnoreCase("video")) {
                                            feedModel.setType(FeedModel.VIDEO_TYPE);

                                        } else {


                                            switch (imageArryCount) {
                                                case 1:
                                                    feedModel.setType(FeedModel.SINGLE_IMAGE_TYPE);
                                                    break;
                                                case 2:
                                                    feedModel.setType(FeedModel.DOUBLE_IMAGE_TYPE);
                                                    break;
                                                case 3:
                                                    feedModel.setType(FeedModel.TRIPLE_IMAGE_TYPE);
                                                    break;
                                                case 4:
                                                    feedModel.setType(FeedModel.FOUR_IMAGE_TYPE);
                                                    break;
                                                case 5:
                                                case 6:
                                                case 7:
                                                case 8:
                                                case 9:
                                                case 10:
                                                case 11:
                                                case 12:
                                                    feedModel.setType(FeedModel.MULTI_IMAGE_TYPE);
                                                    break;


                                            }


                                        }


                                        feedModel.setPostattachments(attachments);

                                        /// get comments of the feed
                                        JSONArray jsonArrayComments = jsonObjectPosts.getJSONArray("comments");

                                        if (jsonArrayComments.length() > 0) {
                                            commentList = new ArrayList<>();
                                            for (int l = 0; l < jsonArrayComments.length(); l++) {


                                                JSONObject jsonObjectComment = jsonArrayComments.getJSONObject(l);
                                                String commentID = jsonObjectComment.getString("id");
                                                String name = jsonObjectComment.getString("name");
                                                String profileImage = jsonObjectComment.getString("profileImage");
                                                String userId = jsonObjectComment.getString("userId");
                                                String postId = jsonObjectComment.getString("postId");
                                                String comment = jsonObjectComment.getString("comment");
                                                String likeCount = jsonObjectComment.getString("commentlikesCount");
                                                String isLiked = jsonObjectComment.getString("isLikedTheComment");
                                                String created_at = jsonObjectComment.getString("created_at");


                                                CommentsModel modelComment = new CommentsModel();
                                                modelComment.setCommentID(commentID);
                                                modelComment.setImage(profileImage);
                                                modelComment.setUserID(userId);
                                                modelComment.setPostID(postId);
                                                modelComment.setComment(comment);
                                                modelComment.setTotlaLikes(likeCount);
                                                modelComment.setIsLike(isLiked);
                                                modelComment.setDate(created_at);
                                                modelComment.setName(name);
                                                commentList.add(modelComment);


                                            }
                                            feedModel.setCommentListSize(jsonArrayComments.length());
                                            feedModel.setCommentsList(commentList);
                                        } else {
                                            feedModel.setCommentListSize(0);
                                        }


                                        FeedListData.add(feedModel);

                                    }


                                }


                            }


                            profileAdapterFeed.notifyDataSetChanged();
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(SAttendeProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void onEvent(AttendeCommentsEvent event) {


        int posPost = event.getPos();
        int postIDz = event.getPostID();

        Log.d("posGetComments", posPost + "  postID=" + postIDz);

        getComments(posPost, postIDz);
    }

    private void getComments(int pos, int id) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();

            String userIDGetComment = GlobalClass.getPref("userID", getApplicationContext());

            WebReq.get(getApplicationContext(), "showcomment?postId=" + id + "&userId=" + userIDGetComment, mParams, new MyTextHttpResponseHandlerGetComments(pos));

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetComments extends JsonHttpResponseHandler {

        int pos;

        MyTextHttpResponseHandlerGetComments(int pos) {
            this.pos = pos;
        }

        @Override
        public void onStart() {
            super.onStart();


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

                        GlobalClass.hideKeyboard(SAttendeProfileActivity.this);


                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {
                            commentList.clear();
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

                            Collections.reverse(commentList);

                            FeedListData.get(pos).setPostCommentsCount(String.valueOf(jsonArray.length()));
                            FeedListData.get(pos).setCommentListSize(Integer.valueOf(jsonArray.length()));

                            FeedListData.get(pos).setCommentsList(commentList);


                            profileAdapterFeed.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_comment_found), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
