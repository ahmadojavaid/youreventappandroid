package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.jobesk.yea.AttendeArea.Adapter.CommentAdapter;
import com.jobesk.yea.AttendeArea.Models.CommentsModel;
import com.jobesk.yea.AttendeArea.Models.FeedModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class SinglePostActivity extends AppCompatActivity {
    private String postId;
    private LinearLayout single_image_ln, double_image_ln, triple_image_ln, four_image_ln, multi_image_ln, text_ln, video_ln;

    private String TAG = "SinglePostActivity";
    private String currentUserID;
    private ImageView back_img;
    private String postID, userImage, userID, descriptionPost, postLikeCount, postCommentCount, postsharecount, postType, userName, isLiked, postTime;
    private ArrayList<String> attachments;
    private ArrayList<CommentsModel> commentList = new ArrayList<>();

    private ArrayList<FeedModel> FeedListData = new ArrayList<>();

    private String modifiedTime, description, isPined;
    private Intent likesIntent;


    private JSONArray jsonArrayComments;

    //views 1
    LinearLayout shareCon_1, chatCon_1, image_bodyContainer_ln_1;
    ImageView rc_profile_iv_1, rc_feed_likes_iv_1, pinImage_1, single_image_1;
    TextView rc_feed_likes_tv_1, rc_feed_username_1, timeTV_1, rc_feed_comments_tv_1, postDescriptionTV_1, send_tv_1;
    EditText comment_et_1;

    RecyclerView recycler_view_1;
    CommentAdapter mAdapter1;

    //views 2
    LinearLayout shareCon_2, chatCon_2, image_bodyContainer_ln_2;
    ImageView rc_profile_iv_2, rc_feed_likes_iv_2, pinImage_2, single_image_2;
    TextView rc_feed_likes_tv_2, rc_feed_username_2, timeTV_2, rc_feed_comments_tv_2, postDescriptionTV_2, send_tv_2;
    EditText comment_et_2;
    ImageView
            container_two_imageone_2, container_two_imagetwo_2;
    RecyclerView recycler_view_2;
    CommentAdapter mAdapter2;


    //views 3
    LinearLayout shareCon_3, chatCon_3, image_bodyContainer_ln_3;
    ImageView rc_profile_iv_3, rc_feed_likes_iv_3, pinImage_3, single_image_3;
    TextView rc_feed_likes_tv_3, rc_feed_username_3, timeTV_3, rc_feed_comments_tv_3, postDescriptionTV_3, send_tv_3;
    EditText comment_et_3;
    ImageView container_three_imageone_3, container_three_imagetwo_3, container_three_imagethree_3;
    RecyclerView recycler_view_3;
    CommentAdapter mAdapter3;

    //views 4
    LinearLayout shareCon_4, chatCon_4, image_bodyContainer_ln_4;
    ImageView rc_profile_iv_4, rc_feed_likes_iv_4, pinImage_4, single_image_4;
    TextView rc_feed_likes_tv_4, rc_feed_username_4, timeTV_4, rc_feed_comments_tv_4, postDescriptionTV_4, send_tv_4;
    EditText comment_et_4;
    ImageView container_four_imageone_4, container_four_imagetwo_4, container_four_imagethree_4, container_four_imagefour_4;
    RecyclerView recycler_view_4;
    CommentAdapter mAdapter4;

    //views multi
    LinearLayout shareCon_5, chatCon_5, image_bodyContainer_ln_5;
    ImageView rc_profile_iv_5, rc_feed_likes_iv_5, pinImage_5, single_image_5;
    TextView rc_feed_likes_tv_5, rc_feed_username_5, timeTV_5, rc_feed_comments_tv_5, postDescriptionTV_5, send_tv_5;
    EditText comment_et_5;

    RecyclerView recycler_view_5;
    CommentAdapter mAdapter5;
    ImageView container_multi_imageone_5, container_multi_imagetwo_5, container_multi_imagethree_5, container_multi_imagefour_5;
    TextView imageCounterTV_5;

    // Views Video
    LinearLayout shareCon_v, chatCon_v, image_bodyContainer_ln_v;
    ImageView rc_profile_iv_v, rc_feed_likes_iv_v, pinImage_v, single_image_v;
    TextView rc_feed_likes_tv_v, rc_feed_username_v, timeTV_v, rc_feed_comments_tv_v, postDescriptionTV_v, send_tv_v;
    EditText comment_et_v;

    RecyclerView recycler_view_v;
    CommentAdapter mAdapterv;
    ImageView single_image_one_v;


    // Views Text
    LinearLayout shareCon_t, chatCon_t, image_bodyContainer_ln_t;
    ImageView rc_profile_iv_t, rc_feed_likes_iv_t, pinImage_t, single_image_t;
    TextView rc_feed_likes_tv_t, rc_feed_username_t, timeTV_t, rc_feed_comments_tv_t, postDescriptionTV_t, send_tv_t;
    EditText comment_et_t;
    RecyclerView recycler_view_t;
    CommentAdapter mAdaptert;
    ImageView single_image_one_t;


    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private String statusBarColor, appMainColor, btnColor, appLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        postId = extras.getString("postId");

        currentUserID = GlobalClass.getPref("userID", getApplicationContext());

        single_image_ln = findViewById(R.id.single_image_ln);
        double_image_ln = findViewById(R.id.double_image_ln);
        triple_image_ln = findViewById(R.id.triple_image_ln);
        four_image_ln = findViewById(R.id.four_image_ln);
        multi_image_ln = findViewById(R.id.multi_image_ln);
        video_ln = findViewById(R.id.video_ln);
        text_ln = findViewById(R.id.text_ln);


        getSingleFeed();


        statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


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

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


    }


    private void getSingleFeed() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);


            WebReq.get(getApplicationContext(), "singlePost?userId=" + currentUserID + "&postId=" + postId, mParams, new MyTextHttpResponseHandlerGetSinglePost());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetSinglePost extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerGetSinglePost() {

        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(SinglePostActivity.this);
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

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                postID = jsonObject.getString("id");
                                userID = jsonObject.getString("userId");
                                descriptionPost = jsonObject.getString("postDescription");
                                postLikeCount = jsonObject.getString("postLikesCount");
                                postCommentCount = jsonObject.getString("postCommentsCount");
                                postsharecount = jsonObject.getString("postSharesCount");
                                postType = jsonObject.getString("postMediaType");
                                postTime = jsonObject.getString("created_at");
                                isLiked = jsonObject.getString("isLiked");
                                userName = jsonObject.getString("name");
                                userImage = jsonObject.getString("profileImage");


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

                                JSONArray attachmentArray = jsonObject.getJSONArray("postattachments");
                                int imageArryCount = attachmentArray.length();
                                if (attachmentArray.length() > 0) {
                                    feedModel.setPostTotalImages(String.valueOf(imageArryCount));
                                    attachments = new ArrayList<>();
                                    for (int j = 0; j < attachmentArray.length(); j++) {
                                        JSONObject jsonObject1 = attachmentArray.getJSONObject(j);
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

                                }
                                if (postType.equalsIgnoreCase("text")) {
                                    feedModel.setType(FeedModel.TEXT_TYPE);

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
                                jsonArrayComments = jsonObject.getJSONArray("comments");

                                if (jsonArrayComments.length() > 0) {


                                    if (commentList.size() > 0) {
                                        commentList.clear();
                                    }
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
                                    feedModel.setCommentListSize(jsonArrayComments.length());
                                    feedModel.setCommentsList(commentList);


                                } else {
                                    feedModel.setCommentListSize(0);
                                }


                                FeedListData.add(feedModel);


                                Log.d("done", "responseDone");
                            }


                        }

                        String commentsSize = String.valueOf(jsonArrayComments.length());
//                        rc_feed_comments_tv.setText(commentsSize);
                        int feedSize = FeedListData.get(0).getPostattachments().size();
                        String postType = FeedListData.get(0).getPostMediaType();
                        if (postType.equalsIgnoreCase("video")) {
                            videoSetViews();
                        } else if (postType.equalsIgnoreCase("text")) {
                            textSetViews();
                        } else {


                            switch (feedSize) {
                                case 1:
                                    singleImageSetViews();
                                    break;
                                case 2:
                                    doubleImageSetViews();
                                    break;
                                case 3:
                                    tripleImageSetViews();
                                    break;
                                case 4:
                                    fourImageSetViews();
                                    break;
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                    multiImageSetViews();
                                    break;


                            }


                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(SinglePostActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void getViewsSingleImage() {

        rc_feed_likes_tv_1 = findViewById(R.id.rc_feed_likes_tv_1);
        shareCon_1 = findViewById(R.id.shareCon_1);


        rc_profile_iv_1 = findViewById(R.id.rc_profile_iv_1);
        rc_feed_username_1 = findViewById(R.id.rc_feed_username_1);
        timeTV_1 = findViewById(R.id.timeTV_1);

        rc_feed_comments_tv_1 = findViewById(R.id.rc_feed_comments_tv_1);

        rc_feed_likes_iv_1 = findViewById(R.id.rc_feed_likes_iv_1);


        chatCon_1 = findViewById(R.id.chatCon_1);
        pinImage_1 = findViewById(R.id.pinImage_1);
        pinImage_1.setVisibility(View.GONE);

        postDescriptionTV_1 = findViewById(R.id.postDescriptionTV_1);
        image_bodyContainer_ln_1 = findViewById(R.id.image_bodyContainer_ln_1);


        send_tv_1 = findViewById(R.id.send_tv_1);


        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_1.setBackgroundColor(btnColor_int);

        }


        image_bodyContainer_ln_1 = findViewById(R.id.image_bodyContainer_ln_1);


        single_image_1 = findViewById(R.id.single_image_1);


        comment_et_1 = findViewById(R.id.comment_et_1);


    }

    private void getViewsDoubleImage() {


        rc_feed_likes_tv_2 = findViewById(R.id.rc_feed_likes_tv_2);
        shareCon_2 = findViewById(R.id.shareCon_2);


        rc_profile_iv_2 = findViewById(R.id.rc_profile_iv_2);
        rc_feed_username_2 = findViewById(R.id.rc_feed_username_2);
        timeTV_2 = findViewById(R.id.timeTV_2);

        rc_feed_comments_tv_2 = findViewById(R.id.rc_feed_comments_tv_2);

        rc_feed_likes_iv_2 = findViewById(R.id.rc_feed_likes_iv_2);


        chatCon_2 = findViewById(R.id.chatCon_2);
        pinImage_2 = findViewById(R.id.pinImage_2);
        pinImage_2.setVisibility(View.GONE);

        postDescriptionTV_2 = findViewById(R.id.postDescriptionTV_2);
        image_bodyContainer_ln_2 = findViewById(R.id.image_bodyContainer_ln_2);


        send_tv_2 = findViewById(R.id.send_tv_2);


        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_2.setBackgroundColor(btnColor_int);

        }


        image_bodyContainer_ln_2 = findViewById(R.id.image_bodyContainer_ln_2);


        container_two_imageone_2 = findViewById(R.id.container_two_imageone_2);
        container_two_imagetwo_2 = findViewById(R.id.container_two_imagetwo_2);


        comment_et_2 = findViewById(R.id.comment_et_2);


    }

    private void getViewsTripleImage() {


        rc_feed_likes_tv_3 = findViewById(R.id.rc_feed_likes_tv_3);
        shareCon_3 = findViewById(R.id.shareCon_3);


        rc_profile_iv_3 = findViewById(R.id.rc_profile_iv_3);
        rc_feed_username_3 = findViewById(R.id.rc_feed_username_3);
        timeTV_3 = findViewById(R.id.timeTV_3);

        rc_feed_comments_tv_3 = findViewById(R.id.rc_feed_comments_tv_3);

        rc_feed_likes_iv_3 = findViewById(R.id.rc_feed_likes_iv_3);


        chatCon_3 = findViewById(R.id.chatCon_3);
        pinImage_3 = findViewById(R.id.pinImage_3);
        pinImage_3.setVisibility(View.GONE);

        postDescriptionTV_3 = findViewById(R.id.postDescriptionTV_3);
        image_bodyContainer_ln_3 = findViewById(R.id.image_bodyContainer_ln_3);


        send_tv_3 = findViewById(R.id.send_tv_3);


        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_3.setBackgroundColor(btnColor_int);

        }


        image_bodyContainer_ln_3 = findViewById(R.id.image_bodyContainer_ln_3);


        container_three_imageone_3 = findViewById(R.id.container_three_imageone_3);
        container_three_imagetwo_3 = findViewById(R.id.container_three_imagetwo_3);
        container_three_imagethree_3 = findViewById(R.id.container_three_imagethree_3);


        comment_et_3 = findViewById(R.id.comment_et_3);

        recycler_view_2 = (RecyclerView) findViewById(R.id.recycler_view_2);
        recycler_view_2.setNestedScrollingEnabled(false);
        mAdapter2 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_2.setLayoutManager(mLayoutManager);
        recycler_view_2.setItemAnimator(new DefaultItemAnimator());
        recycler_view_2.setAdapter(mAdapter2);
    }

    private void getViewsFourImage() {


        rc_feed_likes_tv_4 = findViewById(R.id.rc_feed_likes_tv_4);
        shareCon_4 = findViewById(R.id.shareCon_4);


        rc_profile_iv_4 = findViewById(R.id.rc_profile_iv_4);
        rc_feed_username_4 = findViewById(R.id.rc_feed_username_4);
        timeTV_4 = findViewById(R.id.timeTV_4);

        rc_feed_comments_tv_4 = findViewById(R.id.rc_feed_comments_tv_4);

        rc_feed_likes_iv_4 = findViewById(R.id.rc_feed_likes_iv_4);


        chatCon_4 = findViewById(R.id.chatCon_4);
        pinImage_4 = findViewById(R.id.pinImage_4);
        pinImage_4.setVisibility(View.GONE);

        postDescriptionTV_4 = findViewById(R.id.postDescriptionTV_4);
        image_bodyContainer_ln_4 = findViewById(R.id.image_bodyContainer_ln_4);


        send_tv_4 = findViewById(R.id.send_tv_4);


        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_4.setBackgroundColor(btnColor_int);

        }

        image_bodyContainer_ln_4 = findViewById(R.id.image_bodyContainer_ln_4);


        container_four_imageone_4 = findViewById(R.id.container_four_imageone_4);
        container_four_imagetwo_4 = findViewById(R.id.container_four_imagetwo_4);
        container_four_imagethree_4 = findViewById(R.id.container_four_imagethree_4);
        container_four_imagefour_4 = findViewById(R.id.container_four_imagefour_4);


        comment_et_4 = findViewById(R.id.comment_et_4);

        recycler_view_4 = (RecyclerView) findViewById(R.id.recycler_view_4);
        recycler_view_4.setNestedScrollingEnabled(false);
        mAdapter4 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_4.setLayoutManager(mLayoutManager);
        recycler_view_4.setItemAnimator(new DefaultItemAnimator());
        recycler_view_4.setAdapter(mAdapter4);
    }

    private void getViewsMultiImage() {


        rc_feed_likes_tv_5 = findViewById(R.id.rc_feed_likes_tv_5);
        shareCon_5 = findViewById(R.id.shareCon_5);


        rc_profile_iv_5 = findViewById(R.id.rc_profile_iv_5);
        rc_feed_username_5 = findViewById(R.id.rc_feed_username_5);
        timeTV_5 = findViewById(R.id.timeTV_5);

        rc_feed_comments_tv_5 = findViewById(R.id.rc_feed_comments_tv_5);

        rc_feed_likes_iv_5 = findViewById(R.id.rc_feed_likes_iv_5);


        chatCon_5 = findViewById(R.id.chatCon_5);
        pinImage_5 = findViewById(R.id.pinImage_5);
        pinImage_5.setVisibility(View.GONE);

        postDescriptionTV_5 = findViewById(R.id.postDescriptionTV_5);
        image_bodyContainer_ln_5 = findViewById(R.id.image_bodyContainer_ln_5);


        send_tv_5 = findViewById(R.id.send_tv_5);
        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_5.setBackgroundColor(btnColor_int);

        }


        image_bodyContainer_ln_5 = findViewById(R.id.image_bodyContainer_ln_5);


        container_multi_imageone_5 = findViewById(R.id.container_multi_imageone_5);
        container_multi_imagetwo_5 = findViewById(R.id.container_multi_imagetwo_5);
        container_multi_imagethree_5 = findViewById(R.id.container_multi_imagethree_5);
        container_multi_imagefour_5 = findViewById(R.id.container_multi_imagefour_5);

        imageCounterTV_5 = findViewById(R.id.imageCounterTV_5);
        comment_et_5 = findViewById(R.id.comment_et_5);

        recycler_view_5 = (RecyclerView) findViewById(R.id.recycler_view_5);
        recycler_view_5.setNestedScrollingEnabled(false);
        mAdapter5 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_5.setLayoutManager(mLayoutManager);
        recycler_view_5.setItemAnimator(new DefaultItemAnimator());
        recycler_view_5.setAdapter(mAdapter5);
    }

    private void getViewsVideos() {


        rc_feed_likes_tv_v = findViewById(R.id.rc_feed_likes_tv_v);
        shareCon_v = findViewById(R.id.shareCon_v);


        rc_profile_iv_v = findViewById(R.id.rc_profile_iv_v);
        rc_feed_username_v = findViewById(R.id.rc_feed_username_v);
        timeTV_v = findViewById(R.id.timeTV_v);

        rc_feed_comments_tv_v = findViewById(R.id.rc_feed_comments_tv_v);

        rc_feed_likes_iv_v = findViewById(R.id.rc_feed_likes_iv_v);


        chatCon_v = findViewById(R.id.chatCon_v);
        pinImage_v = findViewById(R.id.pinImage_v);
        pinImage_v.setVisibility(View.GONE);

        postDescriptionTV_v = findViewById(R.id.postDescriptionTV_v);
        image_bodyContainer_ln_v = findViewById(R.id.image_bodyContainer_ln_v);


        send_tv_v = findViewById(R.id.send_tv_5);

        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_v.setBackgroundColor(btnColor_int);

        }

        image_bodyContainer_ln_v = findViewById(R.id.image_bodyContainer_ln_v);


        single_image_one_v = findViewById(R.id.single_image_one_v);


        comment_et_v = findViewById(R.id.comment_et_v);

        recycler_view_v = (RecyclerView) findViewById(R.id.recycler_view_v);
        recycler_view_v.setNestedScrollingEnabled(false);
        mAdapterv = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_v.setLayoutManager(mLayoutManager);
        recycler_view_v.setItemAnimator(new DefaultItemAnimator());
        recycler_view_v.setAdapter(mAdapterv);
    }

    private void getViewstext() {


        rc_feed_likes_tv_t = findViewById(R.id.rc_feed_likes_tv_t);
        shareCon_t = findViewById(R.id.shareCon_t);


        rc_profile_iv_t = findViewById(R.id.rc_profile_iv_t);
        rc_feed_username_t = findViewById(R.id.rc_feed_username_t);
        timeTV_t = findViewById(R.id.timeTV_t);

        rc_feed_comments_tv_t = findViewById(R.id.rc_feed_comments_tv_t);

        rc_feed_likes_iv_t = findViewById(R.id.rc_feed_likes_iv_t);


        chatCon_t = findViewById(R.id.chatCon_t);
        pinImage_t = findViewById(R.id.pinImage_t);
        pinImage_t.setVisibility(View.GONE);

        postDescriptionTV_t = findViewById(R.id.postDescriptionTV_t);
        image_bodyContainer_ln_t = findViewById(R.id.image_bodyContainer_ln_t);


        send_tv_t = findViewById(R.id.send_tv_t);

        if (!appMainColor.equalsIgnoreCase("")) {

            send_tv_t.setBackgroundColor(btnColor_int);

        }


        image_bodyContainer_ln_t = findViewById(R.id.image_bodyContainer_ln_t);


//        single_image_one_t = findViewById(R.id.single_image_one_t);
//        single_image_one_t.setVisibility(View.GONE);

        comment_et_t = findViewById(R.id.comment_et_t);

        recycler_view_t = (RecyclerView) findViewById(R.id.recycler_view_t);
        recycler_view_t.setNestedScrollingEnabled(false);
        mAdaptert = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_t.setLayoutManager(mLayoutManager);
        recycler_view_t.setItemAnimator(new DefaultItemAnimator());
        recycler_view_t.setAdapter(mAdaptert);
    }

    ////////////
    private void singleImageSetViews() {
        getViewsSingleImage();

        final FeedModel feedModel = FeedListData.get(0);


        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_1.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_1.setVisibility(View.VISIBLE);
            postDescriptionTV_1.setText(description);

        }


        isLiked = feedModel.getIsLiked();


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_1.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_1.setImageResource(R.drawable.ic_unlike);
        }

        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_1.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_1.setImageResource(R.drawable.ic_un_pinned);
        }


        rc_feed_likes_tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);

            }
        });


        shareCon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();
            }
        });

        rc_feed_comments_tv_1.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_1.setText(feedModel.getPostLikesCount());
//        share_counter_tv_1.setText(feedModel.getPostSharesCount());
        rc_feed_username_1.setText(feedModel.getName());


        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_1.setText(modifiedTime + "");

        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(rc_profile_iv_1);

        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(0)).fit().centerCrop()
                .into(single_image_1);


        single_image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 0);

            }
        });
        rc_feed_comments_tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        rc_feed_likes_tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);

            }
        });
        chatCon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


//                Intent commentintent = new Intent(getApplicationContext(), CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });
        rc_feed_likes_iv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_1.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_1.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_1.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_1.setText(String.valueOf(likeCountValue));
                }
            }
        });

        pinImage_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_1.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_1.setImageResource(R.drawable.ic_pined);
                }

            }
        });


        send_tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_1.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_1.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });

        single_image_ln.setVisibility(View.VISIBLE);


        recycler_view_1 = (RecyclerView) findViewById(R.id.recycler_view_1);
        recycler_view_1.setNestedScrollingEnabled(false);
        mAdapter1 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_1.setLayoutManager(mLayoutManager);
        recycler_view_1.setItemAnimator(new DefaultItemAnimator());
        recycler_view_1.setAdapter(mAdapter1);

    }

    private void doubleImageSetViews() {
        getViewsDoubleImage();
        final FeedModel feedModel = FeedListData.get(0);

        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_2.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_2.setVisibility(View.VISIBLE);
            postDescriptionTV_2.setText(description);

        }


        isLiked = feedModel.getIsLiked();
        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_2.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_2.setImageResource(R.drawable.ic_unlike);
        }
        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_2.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_2.setImageResource(R.drawable.ic_un_pinned);
        }

        rc_feed_likes_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);
            }
        });

        shareCon_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();
            }
        });


        rc_feed_comments_tv_2.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_2.setText(feedModel.getPostLikesCount());
//        share_counter_tv_2.setText(feedModel.getPostSharesCount());
        rc_feed_username_2.setText(feedModel.getName());

        isLiked = feedModel.getIsLiked();
        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_2.setText(modifiedTime + "");


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(rc_profile_iv_2);

        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                .into(container_two_imageone_2);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                .into(container_two_imagetwo_2);


        container_two_imageone_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 0);

            }
        });
        container_two_imagetwo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 1);

            }
        });
        rc_feed_comments_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        rc_feed_likes_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);
            }
        });
        chatCon_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


//                Intent commentintent = new Intent(getApplicationContext(), CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });

        rc_feed_likes_iv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_2.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_2.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_2.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_2.setText(String.valueOf(likeCountValue));
                }
            }
        });


        pinImage_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {
                    feedModel.setIsPined("0");
                    pinImage_2.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {
                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_2.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_2.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_2.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });


        double_image_ln.setVisibility(View.VISIBLE);


        recycler_view_2 = (RecyclerView) findViewById(R.id.recycler_view_2);
        recycler_view_2.setNestedScrollingEnabled(false);
        mAdapter2 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_2.setLayoutManager(mLayoutManager);
        recycler_view_2.setItemAnimator(new DefaultItemAnimator());
        recycler_view_2.setAdapter(mAdapter2);


    }

    private void tripleImageSetViews() {
        getViewsTripleImage();
        final FeedModel feedModel = FeedListData.get(0);

        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_3.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_3.setVisibility(View.VISIBLE);
            postDescriptionTV_3.setText(description);

        }


        isLiked = feedModel.getIsLiked();


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_3.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_3.setImageResource(R.drawable.ic_unlike);
        }
        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_3.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_3.setImageResource(R.drawable.ic_un_pinned);
        }


        rc_feed_likes_tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);
            }
        });


        shareCon_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareAlert();
            }
        });

        rc_feed_comments_tv_3.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_3.setText(feedModel.getPostLikesCount());
//        share_counter_tv_3.setText(feedModel.getPostSharesCount());
        rc_feed_username_3.setText(feedModel.getName());

        isLiked = feedModel.getIsLiked();
        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_3.setText(modifiedTime + "");


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(rc_profile_iv_3);

        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                .into(container_three_imageone_3);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                .into(container_three_imagetwo_3);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(2)).fit().centerCrop()


                .into(container_three_imagethree_3);


        container_three_imageone_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 0);

            }
        });
        container_three_imagetwo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 1);

            }
        });
        container_three_imagethree_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 2);

            }
        });
        rc_feed_comments_tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        rc_feed_likes_tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);
            }
        });

        chatCon_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


//                Intent commentintent = new Intent(activity, CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });
        rc_feed_likes_iv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_3.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_3.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_3.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_3.setText(String.valueOf(likeCountValue));
                }
            }
        });

        pinImage_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_3.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_3.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_3.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_3.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });


        triple_image_ln.setVisibility(View.VISIBLE);


        recycler_view_3 = (RecyclerView) findViewById(R.id.recycler_view_3);
        recycler_view_3.setNestedScrollingEnabled(false);
        mAdapter3 = new CommentAdapter(SinglePostActivity.this, commentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_3.setLayoutManager(mLayoutManager);
        recycler_view_3.setItemAnimator(new DefaultItemAnimator());
        recycler_view_3.setAdapter(mAdapter3);

    }

    private void fourImageSetViews() {

        getViewsFourImage();
        final FeedModel feedModel = FeedListData.get(0);

        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_4.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_4.setVisibility(View.VISIBLE);
            postDescriptionTV_4.setText(description);

        }

        isLiked = feedModel.getIsLiked();


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_4.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_4.setImageResource(R.drawable.ic_unlike);
        }
        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_4.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_4.setImageResource(R.drawable.ic_un_pinned);
        }
        rc_feed_likes_tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);
            }
        });


        shareCon_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();

            }
        });

        rc_feed_comments_tv_4.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_4.setText(feedModel.getPostLikesCount());
//        share_counter_tv_4.setText(feedModel.getPostSharesCount());
        rc_feed_username_4.setText(feedModel.getName());

        isLiked = feedModel.getIsLiked();
        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_4.setText(modifiedTime + "");


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage())
                .fit().centerCrop()
                .transform(new CircleTransform())
                .placeholder(R.drawable.profile_placeholder)
                .into(rc_profile_iv_4);

        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(0)).fit().centerCrop()

                .into(container_four_imageone_4);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(1)).fit().centerCrop()

                .into(container_four_imagetwo_4);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(2)).fit().centerCrop()

                .into(container_four_imagethree_4);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(3)).fit().centerCrop()

                .into(container_four_imagefour_4);
        container_four_imageone_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 0);

            }
        });
        container_four_imagetwo_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 1);

            }
        });

        container_four_imagethree_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 2);

            }
        });

        container_four_imagefour_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 3);

            }
        });

        rc_feed_comments_tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        rc_feed_likes_tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);

            }
        });

        chatCon_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();

//
//                Intent commentintent = new Intent(getApplicationContext(), CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });

        rc_feed_likes_iv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_4.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_4.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_4.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_4.setText(String.valueOf(likeCountValue));
                }
            }
        });


        pinImage_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_4.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_4.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_4.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_4.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });
        four_image_ln.setVisibility(View.VISIBLE);
        mAdapter4.notifyDataSetChanged();
    }

    private void multiImageSetViews() {

        getViewsMultiImage();

        final FeedModel feedModel = FeedListData.get(0);

        int counterSizeImage = Integer.valueOf(feedModel.getPostTotalImages()) - 4;

        imageCounterTV_5.setText(String.valueOf(counterSizeImage) + "+");

        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_5.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_5.setVisibility(View.VISIBLE);
            postDescriptionTV_5.setText(description);

        }


        isLiked = feedModel.getIsLiked();


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_5.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_5.setImageResource(R.drawable.ic_unlike);
        }
        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_5.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_5.setImageResource(R.drawable.ic_un_pinned);
        }
        rc_feed_likes_tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);
            }
        });


        shareCon_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();

            }
        });


        rc_feed_comments_tv_5.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_5.setText(feedModel.getPostLikesCount());
//        share_counter_tv_5.setText(feedModel.getPostSharesCount());
        rc_feed_username_5.setText(feedModel.getName());

        isLiked = feedModel.getIsLiked();
        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_5.setText(modifiedTime + "");


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(rc_profile_iv_5);

        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                .into(container_multi_imageone_5);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                .into(container_multi_imagetwo_5);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(2)).fit().centerCrop()


                .into(container_multi_imagethree_5);
        Picasso.with(getApplicationContext())
                .load(feedModel.getPostattachments().get(3)).fit().centerCrop()

                .into(container_multi_imagefour_5);


        container_multi_imageone_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 0);

            }
        });

        container_multi_imagetwo_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 1);

            }
        });

        container_multi_imagethree_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 2);

            }
        });

        container_multi_imagefour_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setImageIntent(feedModel.getPostattachments(), 3);

            }
        });


        rc_feed_likes_tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);


            }
        });
        chatCon_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


//                Intent commentintent = new Intent(activity, CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                activity.startActivity(commentintent);

            }
        });
        rc_feed_likes_iv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_5.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_5.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_5.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_5.setText(String.valueOf(likeCountValue));

                }
            }
        });

//
        pinImage_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_5.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_5.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_5.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }


                comment_et_5.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });


        multi_image_ln.setVisibility(View.VISIBLE);
        mAdapter5.notifyDataSetChanged();
    }

    private void videoSetViews() {
        getViewsVideos();
        final FeedModel feedModel = FeedListData.get(0);

        rc_feed_likes_tv_v.setText(feedModel.getPostLikesCount());
        rc_feed_comments_tv_v.setText(String.valueOf(commentList.size()));
//        share_counter_tv_v.setText(feedModel.getPostSharesCount());


        String videoThumb = feedModel.getPostattachments().get(0).toString();
        Log.d("videourls", videoThumb + "");
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(videoThumb)
//                            .apply(requestOptions)
                .into(single_image_one_v);


        single_image_one_v.setVisibility(View.VISIBLE);

        single_image_one_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoIntent = new Intent(getApplicationContext(), ShowVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoLink", feedModel.getPostattachments().get(0).toString());
                videoIntent.putExtras(bundle);
                startActivity(videoIntent);

            }
        });


        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_v.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_v.setVisibility(View.VISIBLE);
            postDescriptionTV_v.setText(description);

        }


        isLiked = feedModel.getIsLiked();

        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_v.setText(modifiedTime + "");


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_v.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_v.setImageResource(R.drawable.ic_unlike);
        }
        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_v.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_v.setImageResource(R.drawable.ic_un_pinned);
        }

        rc_feed_likes_tv_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);
            }
        });

        shareCon_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();

            }
        });

        rc_feed_comments_tv_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        rc_feed_likes_tv_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);
            }
        });

        chatCon_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();

//
//                Intent commentintent = new Intent(getApplicationContext(), CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });
        rc_feed_likes_iv_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_v.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_v.setText(String.valueOf(likeCountValue));
                } else {
                    //like
                    rc_feed_likes_iv_v.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_v.setText(String.valueOf(likeCountValue));
                }
            }
        });
        pinImage_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_v.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_v.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_v.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_v.setText("");
                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);


            }
        });

        video_ln.setVisibility(View.VISIBLE);

    }


    private void textSetViews() {
        getViewstext();
        final FeedModel feedModel = FeedListData.get(0);

        description = feedModel.getPostDescription();
        if (description.equalsIgnoreCase("")) {
            image_bodyContainer_ln_t.setVisibility(View.GONE);
        } else {
            image_bodyContainer_ln_t.setVisibility(View.VISIBLE);
            postDescriptionTV_t.setText(description);

        }


        isLiked = feedModel.getIsLiked();


        if (isLiked.equalsIgnoreCase("1")) {
            rc_feed_likes_iv_t.setImageResource(R.drawable.ic_like);
        } else {
            rc_feed_likes_iv_t.setImageResource(R.drawable.ic_unlike);
        }

        isPined = feedModel.getIsPined();
        if (isPined.equalsIgnoreCase("1")) {
            pinImage_t.setImageResource(R.drawable.ic_pined);
        } else {
            pinImage_t.setImageResource(R.drawable.ic_un_pinned);
        }


        rc_feed_likes_tv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                likesIntent = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(likesIntent);

            }
        });


        shareCon_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlert();
            }
        });

        rc_feed_comments_tv_t.setText(String.valueOf(commentList.size()));
        rc_feed_likes_tv_t.setText(feedModel.getPostLikesCount());
//         share_counter_tv_t.setText(feedModel.getPostSharesCount());
        rc_feed_username_t.setText(feedModel.getName());


        modifiedTime = GlobalClass.parseDate(feedModel.getDate(), getApplicationContext());
        timeTV_t.setText(modifiedTime + "");

        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(rc_profile_iv_t);


        rc_feed_comments_tv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        rc_feed_likes_tv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


                Intent likesIntent = new Intent(getApplicationContext(), LikesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("postID", postID);
                likesIntent.putExtras(bundle);
                startActivity(likesIntent);

            }
        });
        chatCon_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID = feedModel.getPostID();


//                Intent commentintent = new Intent(getApplicationContext(), CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("postID", postID);
//                bundle.putString("position", String.valueOf(position));
//                commentintent.putExtras(bundle);
//                startActivity(commentintent);

            }
        });
        rc_feed_likes_iv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postLiked = FeedListData.get(0).getIsLiked();
                String likeCount = FeedListData.get(0).getPostLikesCount();

                if (postLiked.equalsIgnoreCase("1")) {
                    //unlike
                    rc_feed_likes_iv_t.setImageResource(R.drawable.ic_unlike);


                    int likeCountValue = Integer.valueOf(likeCount) - 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("0");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_t.setText(String.valueOf(likeCountValue));

                } else {
                    //like
                    rc_feed_likes_iv_t.setImageResource(R.drawable.ic_like);
                    int likeCountValue = Integer.valueOf(likeCount) + 1;
                    FeedListData.get(0).setPostLikesCount(String.valueOf(likeCountValue));
                    FeedListData.get(0).setIsLiked("1");
                    PostLike(FeedListData.get(0).getPostID());
                    rc_feed_likes_tv_t.setText(String.valueOf(likeCountValue));
                }


            }
        });

        pinImage_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isPinned = feedModel.getIsPined();
                if (isPinned.equalsIgnoreCase("1")) {

                    feedModel.setIsPined("0");
                    pinImage_t.setImageResource(R.drawable.ic_un_pinned);
                    postPin(feedModel.getPostID());
                } else {

                    feedModel.setIsPined("1");
                    postPin(feedModel.getPostID());
                    pinImage_t.setImageResource(R.drawable.ic_pined);
                }
            }
        });


        send_tv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentValue = comment_et_t.getText().toString().trim();
                if (commentValue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comment_et_t.setText("");

                PostComment(0, Integer.valueOf(feedModel.getPostID()), commentValue);
            }
        });
        text_ln.setVisibility(View.VISIBLE);

    }

    private void setImageIntent(ArrayList<String> photoArray, int pos) {


        Intent fullimageIntnent = new Intent(getApplicationContext(), ImageViewMultiActivity.class);
        fullimageIntnent.putExtra("imageList", photoArray);
        fullimageIntnent.putExtra("selectedPos", String.valueOf(pos));
        startActivity(fullimageIntnent);


    }


    public void shareAlert() {


        String postType = FeedListData.get(0).getPostMediaType();
        if (postType.equalsIgnoreCase("text")) {

            String shareBody = FeedListData.get(0).getPostDescription();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getApplicationContext().getResources().getString(R.string.share_using)));


        } else if (postType.equalsIgnoreCase("image")) {
            ArrayList<String> videoLink = new ArrayList<>();
            videoLink = FeedListData.get(0).getPostattachments();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < videoLink.size(); i++) {

                String link = videoLink.get(0);

                sb.append(link + "\n");

            }


            String shareBody = FeedListData.get(0).getPostDescription() + " \n " + sb;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getApplicationContext().getResources().getString(R.string.share_using)));


        } else if (postType.equalsIgnoreCase("video")) {


            String videoLink = FeedListData.get(0).getPostattachments().get(0).toString();
            String shareBody = FeedListData.get(0).getPostDescription() + " \n " + Urls.BASEURL + videoLink;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getApplicationContext().getResources().getString(R.string.share_using)));

        }


    }


    private void PostLike(String postID) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", getApplicationContext());
            mParams.put("userId", userID);
            mParams.put("postId", postID);


            WebReq.post(getApplicationContext(), "like", mParams, new MyTextHttpResponseHandlerPostLike());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
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


    private void PostComment(int pos, int id, String commentValue) {

        GlobalClass.hideKeyboard(SinglePostActivity.this);

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
            String userIDCurrent = GlobalClass.getPref("userID", getApplicationContext());
            mParams.put("userId", userIDCurrent);
            mParams.put("postId", id);
            mParams.put("comment", commentValue);


            Log.d("postCommentParams", mParams + "");
            WebReq.post(getApplicationContext(), "comment", mParams, new MyTextHttpResponseHandlerPostComment(pos, id));

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerPostComment extends JsonHttpResponseHandler {

        int pos, id;

        MyTextHttpResponseHandlerPostComment(int pos, int id) {

            this.pos = pos;
            this.id = id;
        }

        @Override
        public void onStart() {
            super.onStart();

            Log.d("postComment", "onStart");


        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d("postComment", "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d("postComment", "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("postComment", responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d("postComment", mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        getComments(0, id);

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

//                        GlobalClass.hideKeyboard(getApplicationContext());


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


                            FeedListData.get(0).setPostCommentsCount(String.valueOf(jsonArray.length()));
                            FeedListData.get(0).setCommentListSize(Integer.valueOf(jsonArray.length()));

                            Collections.reverse(commentList);
                            FeedListData.get(0).setCommentsList(commentList);


                            if (postType.equalsIgnoreCase("video")) {

                                mAdapterv.notifyDataSetChanged();


                                rc_feed_comments_tv_v.setText(String.valueOf(jsonArray.length()));
                            }
                            if (postType.equalsIgnoreCase("text")) {
                                rc_feed_comments_tv_t.setText(String.valueOf(jsonArray.length()));
                                mAdaptert.notifyDataSetChanged();

                            } else {

                                int feedSize = FeedListData.get(0).getPostattachments().size();
                                switch (feedSize) {
                                    case 1:
                                        rc_feed_comments_tv_1.setText(String.valueOf(jsonArray.length()));
                                        mAdapter1.notifyDataSetChanged();
                                        break;
                                    case 2:
                                        rc_feed_comments_tv_2.setText(String.valueOf(jsonArray.length()));
                                        mAdapter2.notifyDataSetChanged();
                                        break;
                                    case 3:
                                        rc_feed_comments_tv_3.setText(String.valueOf(jsonArray.length()));
                                        mAdapter3.notifyDataSetChanged();
                                        break;
                                    case 4:
                                        rc_feed_comments_tv_4.setText(String.valueOf(jsonArray.length()));
                                        mAdapter4.notifyDataSetChanged();
                                        break;
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10:
                                    case 11:
                                    case 12:
                                        rc_feed_comments_tv_5.setText(String.valueOf(jsonArray.length()));
                                        mAdapter5.notifyDataSetChanged();
                                        break;


                                }


                            }


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


    private void postPin(String postID) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", getApplicationContext());
            mParams.put("userId", userID);
            mParams.put("postId", postID);


            WebReq.post(getApplicationContext(), "pinPost", mParams, new MyTextHttpResponseHandlerPostPin());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerPostPin extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerPostPin() {


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
            Log.d("postPin", mResponse.toString() + "Respo");
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
