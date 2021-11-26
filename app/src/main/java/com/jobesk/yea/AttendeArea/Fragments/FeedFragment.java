package com.jobesk.yea.AttendeArea.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Activities.AttendeProfileActivity;
import com.jobesk.yea.AttendeArea.Activities.CreatePostActivity;
import com.jobesk.yea.AttendeArea.Activities.DrawerActivity;
import com.jobesk.yea.AttendeArea.Activities.NotificationActivity;
import com.jobesk.yea.AttendeArea.Adapter.FeedAdapter;
import com.jobesk.yea.AttendeArea.EventBus.FeedRefreshEvent;
import com.jobesk.yea.AttendeArea.EventBuses.FeedGetCommentsEvent;
import com.jobesk.yea.AttendeArea.EventBuses.ShareBus;
import com.jobesk.yea.AttendeArea.Models.CommentsModel;
import com.jobesk.yea.AttendeArea.Models.FeedModel;
import com.jobesk.yea.AttendeArea.Models.HeaderModel;
import com.jobesk.yea.R;

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

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String accessToken;
    private SwipeRefreshLayout refreshLayout;
    private DrawerActivity activity;
    private int currentPage;
    private CardView no_list, no_internet_container;
    private View view;

    private final int REQUEST_WRITE_PERMISSION = 92;
    private int totalPage;
    private int previousTotal = 0;
    private RecyclerView recyclerView;

    private FeedAdapter feedFragmentRecycler;
    private LinearLayoutManager layoutManager;
    private boolean loading = true;
    private int visibleThreshold = 10;
    private boolean refresh = false;

    private String title, postId, is_like, comment_count, like_count, body, post_type, privacy, created_at, userId, name, description, is_owner, location_name, userImage;
    private FeedModel feedModel;

    private ArrayList<FeedModel> FeedListData = new ArrayList<>();
    private ImageView meun_img;
    private FeedModel model;
    private TextView toolbar_title_tv;
    private ImageView noti_img;
    private AlertDialog shareAlert;
    private ImageView create_post_img, home_img, my_profile_img;

    private String TAG = "FeedFragment";
    private String postID, userID, descriptionPost, postLikeCount, postCommentCount, postsharecount, postType, userName, isLiked, postTime;
    private ArrayList<String> attachments;
    private ArrayList<CommentsModel> commentList;
    private ArrayList<HeaderModel> headerList = new ArrayList<>();
    private int PosShare;
    private Handler handler;
    private int delay = 3000;
    private Runnable runnable;
    private TextView notiCounter_tv;
    private int threadCheck = 0;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private RelativeLayout bottom_bar;
    private ImageView logo_toolbar;


    private int hasPined = 0;
    private int hasScrolled = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            EventBus.getDefault().register(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notiCounterThread();


    }

    private void notiCounterThread() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                try {
                    String notiCounter = GlobalClass.getPref("notiCounter", getActivity());
                    if (notiCounter.equalsIgnoreCase("")) {
                        notiCounter_tv.setVisibility(View.GONE);
                    } else {


                        if (Integer.valueOf(notiCounter) > 9) {

                            String NotiValue = "9+";
                            notiCounter_tv.setText(NotiValue);
                        } else {
                            notiCounter_tv.setText(notiCounter);
                        }


                        notiCounter_tv.setVisibility(View.VISIBLE);


                    }

                } catch (Exception e) {

                }


                runnable = this;
                handler.postDelayed(runnable, delay);
            }
        }, delay);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        activity = (DrawerActivity) view.getContext();
        GlobalClass.hideKeyboard(activity);


//        String notivalue = GlobalClass.getPref("notivalue", getActivity());
//        Log.d("notivalue", notivalue);


        toolbar_title_tv = view.findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getActivity().getResources().getString(R.string.activity_feed));

        refreshLayout = view.findViewById(R.id.feed_swipe);
        recyclerView = view.findViewById(R.id.recycler_feed);


        String statusBarColor = GlobalClass.getPref("statusBarColor", getActivity());
        String appMainColor = GlobalClass.getPref("appMainColor", getActivity());
        String btnColor = GlobalClass.getPref("btnColor", getActivity());
        String appLogo = GlobalClass.getPref("appLogo", getActivity());


        toolbar_header = view.findViewById(R.id.toolbar_header);
        meun_img = view.findViewById(R.id.meun_img);
        notiCounter_tv = view.findViewById(R.id.notiCounter_tv);
        noti_img = view.findViewById(R.id.noti_img);
        noti_img.setVisibility(View.VISIBLE);

        bottom_bar = view.findViewById(R.id.bottom_bar);

        logo_toolbar = view.findViewById(R.id.logo_toolbar);


        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            meun_img.setColorFilter(btnColor_int);
            noti_img.setColorFilter(btnColor_int);
            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);

            bottom_bar.setBackgroundResource(0);
            bottom_bar.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(activity)
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }


        meun_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerActivity.openDrawer();


            }
        });


        noti_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notiIntent = new Intent(activity, NotificationActivity.class);
                startActivity(notiIntent);
            }
        });


        no_list = view.findViewById(R.id.no_list_feed);
        no_internet_container = view.findViewById(R.id.no_internet_container);


        currentPage = 1;
        totalPage = 1;
        previousTotal = 0;
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setVisibility(View.VISIBLE);
        feedFragmentRecycler = new FeedAdapter(activity, FeedListData, 1, headerList, "mainFeed");
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(activity, R.drawable.line_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(feedFragmentRecycler);


        if (GlobalClass.isOnline(getActivity()) == true) {
            try {
                GlobalClass.showLoading(activity);
                getFeed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//            if (appInit == 0) {
//                no_internet_container.setVisibility(View.VISIBLE);
//            } else {
//                no_internet_container.setVisibility(View.GONE);
//            }
        }


        create_post_img = view.findViewById(R.id.create_post_img);
        my_profile_img = view.findViewById(R.id.my_profile_img);
        home_img = view.findViewById(R.id.home_tv);
        create_post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(i);

            }
        });

        my_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AttendeProfileActivity.class);
                String userIDCurent = GlobalClass.getPref("userID", getActivity());
                Bundle bundle = new Bundle();
                bundle.putString("isMyyProfile", "1");
                bundle.putString("nature", "attendee");
                bundle.putString("userID", userIDCurent);
                i.putExtras(bundle);
                startActivity(i);

            }
        });

        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });


        return view;
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);

        currentPage = 1;
        totalPage = 1;
        previousTotal = 0;
        refresh = true;
        hasPined = 0;
        if (refresh == true) {

        } else {
            GlobalClass.showLoading(activity);
            refresh = false;
        }


        if (FeedListData.size() > 0) {
            FeedListData.clear();
        }
        feedFragmentRecycler.notifyDataSetChanged();


        getFeed();
    }


    @Override
    public void onResume() {
        GlobalClass.hideKeyboard(activity);
        super.onResume();
    }

    @Override
    public void onStop() {
        GlobalClass.hideKeyboard(activity);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        GlobalClass.hideKeyboard(activity);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(ShareBus event) {
//
//
//
//    }
    @Subscribe
    public void onEvent(ShareBus event) {


        PosShare = event.getPos();
        Log.d("posShare", PosShare + "");

//        requestPermission();


    }

    @Subscribe
    public void onEvent(FeedRefreshEvent event) {


        onRefresh();

    }

    private void getFeed() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);

            String userID = GlobalClass.getPref("userID", getActivity());
            WebReq.get(activity, "post?userId=" + userID + "&page=" + currentPage, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(activity, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            if (refresh == true) {
                refreshLayout.setRefreshing(true);
                refresh = false;
            } else {
                GlobalClass.showLoading(activity);
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


                        JSONObject jsonObjectResult = mResponse.getJSONObject("Result");
                        currentPage = Integer.valueOf(jsonObjectResult.getString("current_page"));
                        totalPage = Integer.valueOf(jsonObjectResult.getString("total"));
// pinPosts
                        if (hasPined == 0) {
                            hasPined = 1;
                            JSONArray jsonArrayPin = mResponse.getJSONArray("pinned");


                            if (jsonArrayPin.length() > 0) {

                                for (int i = 0; i < jsonArrayPin.length(); i++) {

                                    JSONObject jsonObject = jsonArrayPin.getJSONObject(i);
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
                                    feedModel.setIsPined("1");

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

                                    Log.d("PostType", postType);
                                    if (postType.equalsIgnoreCase("video")) {
                                        feedModel.setType(FeedModel.VIDEO_TYPE);

                                    } else if (postType.equalsIgnoreCase("text")) {
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
                                    JSONArray jsonArrayComments = jsonObject.getJSONArray("comments");

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

                                }
                            }

                        }

////////////////////////
                        JSONArray jsonArray = jsonObjectResult.getJSONArray("data");

                        if (jsonArray.length() > 0) {


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
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


                                Log.d("PostType", postType);
                                if (postType.equalsIgnoreCase("video")) {
                                    feedModel.setType(FeedModel.VIDEO_TYPE);

                                } else if (postType.equalsIgnoreCase("text")) {
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
                                JSONArray jsonArrayComments = jsonObject.getJSONArray("comments");

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

                            }


                        }

                        feedFragmentRecycler.notifyDataSetChanged();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                    if (hasScrolled == 0) {
                        hasScrolled = 1;
                        ScrollViewRecyclerAndroid();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Subscribe
    public void onEvent(FeedGetCommentsEvent event) {


        int posPost = event.getPos();
        int postIDz = event.getPostID();

        Log.d("posGetComments", posPost + "  postID=" + postIDz);

        getComments(posPost, postIDz);
    }

    private void getComments(int pos, int id) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();

            String userIDGetComment = GlobalClass.getPref("userID", getActivity());

            WebReq.get(activity, "showcomment?postId=" + id + "&userId=" + userIDGetComment, mParams, new MyTextHttpResponseHandlerGetComments(pos));

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
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

                        GlobalClass.hideKeyboard(activity);


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


                            feedFragmentRecycler.notifyDataSetChanged();

                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_comment_found), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        handler.removeCallbacks(runnable); //stop handler when activity not visible
        handler.removeCallbacksAndMessages(null);
    }


    private void ScrollViewRecyclerAndroid() {



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                    if (currentPage > totalPage) {


//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(activity, R.string.no_more_advisor, Toast.LENGTH_SHORT).show();
//                                }
//                            }, 2000);
                    } else {
                        try {
                            currentPage++;

                            if (GlobalClass.isOnline(getActivity())) {
//                                    progressBar.setVisibility(View.VISIBLE);
                            } else {
//                                    progressBar.setVisibility(View.GONE);
                            }
                            if (currentPage <= totalPage) {

                                getFeed();
                            } else {
//                                    progressBar.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    loading = true;
                }
            }
        });


    }

}