package com.jobesk.yea.SponsorArea.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jobesk.yea.AttendeArea.Activities.AttendeProfileActivity;
import com.jobesk.yea.AttendeArea.Activities.CommentActivity;
import com.jobesk.yea.AttendeArea.Activities.EditProfileActivity;
import com.jobesk.yea.AttendeArea.Activities.ImageViewMultiActivity;
import com.jobesk.yea.AttendeArea.Activities.LikesActivity;
import com.jobesk.yea.AttendeArea.Activities.ShowVideoActivity;
import com.jobesk.yea.AttendeArea.EventBuses.AttendeCommentsEvent;
import com.jobesk.yea.AttendeArea.EventBuses.FeedGetCommentsEvent;
import com.jobesk.yea.AttendeArea.Models.FeedModel;
import com.jobesk.yea.AttendeArea.Models.HeaderModel;
import com.jobesk.yea.R;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;


public class SFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<FeedModel> list;

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
    private int statusMyProfile;
    private String isPined, isLiked;
    private String description;
    private int commentListSize;
    private String nature;
    private String statusBarColor, appMainColor, btnColor, appLogo;
    private ArrayList<HeaderModel> HeaderList;
    private int statusBarColor_int, appMainColor_int, greyPin, btnColor_int;


    public SFeedAdapter(Activity activity, ArrayList<FeedModel> list, int statusMyProfile, ArrayList<HeaderModel> HeaderList, String nature) {

        this.activity = activity;
        this.list = list;
        this.statusMyProfile = statusMyProfile;
        this.HeaderList = HeaderList;
        this.nature = nature;


        statusBarColor = GlobalClass.getPref("statusBarColor", activity);
        appMainColor = GlobalClass.getPref("appMainColor", activity);
        btnColor = GlobalClass.getPref("btnColor", activity);
        appLogo = GlobalClass.getPref("appLogo", activity);

        if (!appMainColor.equalsIgnoreCase("")) {
            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);
            greyPin = Color.parseColor("#AAAAAA");
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case FeedModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_text_type, parent, false);
                return new TextViewHolder(view);
            case FeedModel.USER_INFO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_s_user_info_type, parent, false);
                return new UserInfoViewHolderType(view);
            case FeedModel.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_video_type, parent, false);
                return new VideoViewHolderType(view);
            case FeedModel.EVENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_event_type, parent, false);
                return new EventTypeViewHolder(view);
            case FeedModel.SINGLE_IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_single_type, parent, false);
                return new SingleImageViewHolder(view);
            case FeedModel.DOUBLE_IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_double_type, parent, false);
                return new DoubleImageType(view);
            case FeedModel.TRIPLE_IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_triple_type, parent, false);
                return new TripleImageType(view);
            case FeedModel.FOUR_IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_four_type, parent, false);
                return new FourImageType(view);
            case FeedModel.MULTI_IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_image_multi_type, parent, false);
                return new MultiImageType(view);


        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {


        switch (list.get(position).getType()) {
//            case 0:
//                return FeedModel.HEADER_TYPE;
            case 1:
                return FeedModel.USER_INFO_TYPE;
            case 2:
                return FeedModel.VIDEO_TYPE;
            case 3:
                return FeedModel.TEXT_TYPE;
            case 4:
                return FeedModel.EVENT_TYPE;
            case 5:
                return FeedModel.SINGLE_IMAGE_TYPE;
            case 6:
                return FeedModel.DOUBLE_IMAGE_TYPE;
            case 7:
                return FeedModel.TRIPLE_IMAGE_TYPE;
            case 8:
                return FeedModel.FOUR_IMAGE_TYPE;
            case 9:
                return FeedModel.MULTI_IMAGE_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final FeedModel feedModel = list.get(position);

        Log.d("adapterFeedtype", feedModel.getType() + "");

        if (feedModel != null) {
            switch (feedModel.getType()) {

                case FeedModel.USER_INFO_TYPE:



                    String jobTitle = HeaderList.get(0).getJobTitle();
                    if (!jobTitle.equalsIgnoreCase("null")) {

                        ((UserInfoViewHolderType) holder).job_title_tv.setText(jobTitle);

                    }


                    ((UserInfoViewHolderType) holder).userName_tv.setText(HeaderList.get(0).getUserName());
                    ((UserInfoViewHolderType) holder).followers_count_tv.setText(HeaderList.get(0).getFollowersCount());
                    ((UserInfoViewHolderType) holder).following_count_tv.setText(HeaderList.get(0).getFollowingCount());
                    ((UserInfoViewHolderType) holder).total_posts_tv.setText(HeaderList.get(0).getPostCount());


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + HeaderList.get(0).getProfileImage())
                            .fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((UserInfoViewHolderType) holder).profile_image);

                    ((UserInfoViewHolderType) holder).edit_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent editIntent = new Intent(activity, EditProfileActivity.class);
                            activity.startActivity(editIntent);


                        }
                    });





                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((UserInfoViewHolderType) holder).edit_container.setBackgroundColor(btnColor_int);

                        ((UserInfoViewHolderType) holder).following_bar_ln.setBackgroundColor(appMainColor_int);


                        ((UserInfoViewHolderType) holder).profile_image.setBorderColor(appMainColor_int);
                        ((UserInfoViewHolderType) holder).userName_tv.setTextColor(appMainColor_int);


                        ((UserInfoViewHolderType) holder).follower_txt_tv.setTextColor(btnColor_int);
                        ((UserInfoViewHolderType) holder).following_txt_tv.setTextColor(btnColor_int);
                        ((UserInfoViewHolderType) holder).posts_txt_tv.setTextColor(btnColor_int);

                    }

                    ((UserInfoViewHolderType) holder).edit_container.setVisibility(View.GONE);
                    break;
                case FeedModel.SINGLE_IMAGE_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((SingleImageViewHolder) holder).send_tv.setBackgroundColor(btnColor_int);

                    }

                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((SingleImageViewHolder) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((SingleImageViewHolder) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((SingleImageViewHolder) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((SingleImageViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((SingleImageViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }


                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((SingleImageViewHolder) holder).pinImage.setImageResource(0);
                            ((SingleImageViewHolder) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((SingleImageViewHolder) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((SingleImageViewHolder) holder).pinImage.setImageResource(0);
                            ((SingleImageViewHolder) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((SingleImageViewHolder) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {
                        ((SingleImageViewHolder) holder).pinImage.setVisibility(View.GONE);

                    }


                    ((SingleImageViewHolder) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            likesIntent = new Intent(activity, LikesActivity.class);
                            activity.startActivity(likesIntent);

                        }
                    });


                    ((SingleImageViewHolder) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareAlert(feedModel, position);
//                            EventBus.getDefault().post(new ShareBus(position));
                        }
                    });

                    ((SingleImageViewHolder) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((SingleImageViewHolder) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((SingleImageViewHolder) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((SingleImageViewHolder) holder).rc_feed_username.setText(feedModel.getName());


                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((SingleImageViewHolder) holder).timeTV.setText(modifiedTime + "");

                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((SingleImageViewHolder) holder).rc_profile_iv);

                    ((SingleImageViewHolder) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });
                    ((SingleImageViewHolder) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });


                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(0)).fit().centerCrop()
                            .into(((SingleImageViewHolder) holder).single_image_one);


                    ((SingleImageViewHolder) holder).single_image_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 0);

                        }
                    });
                    ((SingleImageViewHolder) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    ((SingleImageViewHolder) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);

                        }
                    });
                    ((SingleImageViewHolder) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });
                    ((SingleImageViewHolder) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((SingleImageViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((SingleImageViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });

                    ((SingleImageViewHolder) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });

                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((SingleImageViewHolder) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((SingleImageViewHolder) holder).commnet_one_con.setVisibility(View.GONE);
                        ((SingleImageViewHolder) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;


                            ((SingleImageViewHolder) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((SingleImageViewHolder) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((SingleImageViewHolder) holder).username1.setText(name1);
                            ((SingleImageViewHolder) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());
                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((SingleImageViewHolder) holder).time1.setText(modifiedTime);
                            ((SingleImageViewHolder) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((SingleImageViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((SingleImageViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((SingleImageViewHolder) holder).likeCount1.setText(commentCount1);


                            ((SingleImageViewHolder) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((SingleImageViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((SingleImageViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((SingleImageViewHolder) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((SingleImageViewHolder) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((SingleImageViewHolder) holder).username2.setText(name2);
                            ((SingleImageViewHolder) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((SingleImageViewHolder) holder).time2.setText(modifiedTime);
                            ((SingleImageViewHolder) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((SingleImageViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((SingleImageViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((SingleImageViewHolder) holder).likeCount2.setText(commentCount2);


                            ((SingleImageViewHolder) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((SingleImageViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((SingleImageViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }


                    } else {
                        ((SingleImageViewHolder) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((SingleImageViewHolder) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((SingleImageViewHolder) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((SingleImageViewHolder) holder).comment_et.setText("");


                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);

                        }
                    });


                    break;
                case FeedModel.EVENT_TYPE:

                    break;
                case FeedModel.VIDEO_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((VideoViewHolderType) holder).send_tv.setBackgroundColor(btnColor_int);

                    }


                    ((VideoViewHolderType) holder).rc_feed_username.setText(feedModel.getName());
                    ((VideoViewHolderType) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((VideoViewHolderType) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((VideoViewHolderType) holder).share_counter_tv.setText(feedModel.getPostSharesCount());

                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((VideoViewHolderType) holder).rc_profile_iv);

                    ((VideoViewHolderType) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });
                    ((VideoViewHolderType) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });

                    String videoThumb = feedModel.getPostattachments().get(0).toString();
                    Log.d("videourls", videoThumb + "");
                    Glide.with(activity)
                            .asBitmap()
                            .load(videoThumb)
//                            .apply(requestOptions)
                            .into(((VideoViewHolderType) holder).single_image_one);


                    ((VideoViewHolderType) holder).single_image_one.setVisibility(View.VISIBLE);

                    ((VideoViewHolderType) holder).single_image_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent videoIntent = new Intent(activity, ShowVideoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("videoLink", feedModel.getPostattachments().get(0).toString());
                            videoIntent.putExtras(bundle);
                            activity.startActivity(videoIntent);

                        }
                    });


                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((VideoViewHolderType) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((VideoViewHolderType) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((VideoViewHolderType) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();

                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((VideoViewHolderType) holder).timeTV.setText(modifiedTime + "");


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((VideoViewHolderType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((VideoViewHolderType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }


                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((VideoViewHolderType) holder).pinImage.setImageResource(0);
                            ((VideoViewHolderType) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((VideoViewHolderType) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((VideoViewHolderType) holder).pinImage.setImageResource(0);
                            ((VideoViewHolderType) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((VideoViewHolderType) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {
                        ((VideoViewHolderType) holder).pinImage.setVisibility(View.GONE);


                    }


                    ((VideoViewHolderType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);
                        }
                    });

                    ((VideoViewHolderType) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareAlert(feedModel, position);
//                            EventBus.getDefault().post(new ShareBus(position));
                        }
                    });

                    ((VideoViewHolderType) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });

                    ((VideoViewHolderType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);
                        }
                    });

                    ((VideoViewHolderType) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });
                    ((VideoViewHolderType) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((VideoViewHolderType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((VideoViewHolderType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });
                    ((VideoViewHolderType) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });

                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((VideoViewHolderType) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((VideoViewHolderType) holder).commnet_one_con.setVisibility(View.GONE);
                        ((VideoViewHolderType) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;
                            ((VideoViewHolderType) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((VideoViewHolderType) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((VideoViewHolderType) holder).username1.setText(name1);
                            ((VideoViewHolderType) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((VideoViewHolderType) holder).time1.setText(modifiedTime);
                            ((VideoViewHolderType) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((VideoViewHolderType) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((VideoViewHolderType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((VideoViewHolderType) holder).likeCount1.setText(commentCount1);


                            ((VideoViewHolderType) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((VideoViewHolderType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((VideoViewHolderType) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((VideoViewHolderType) holder).userImage2);


                            ((VideoViewHolderType) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((VideoViewHolderType) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((VideoViewHolderType) holder).username2.setText(name2);
                            ((VideoViewHolderType) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());
                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((VideoViewHolderType) holder).time2.setText(modifiedTime);
                            ((VideoViewHolderType) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((VideoViewHolderType) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((VideoViewHolderType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((VideoViewHolderType) holder).likeCount2.setText(commentCount2);


                            ((VideoViewHolderType) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((VideoViewHolderType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((VideoViewHolderType) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });

                        }


                    } else {
                        ((VideoViewHolderType) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((VideoViewHolderType) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((VideoViewHolderType) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((VideoViewHolderType) holder).comment_et.setText("");
                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);


                        }
                    });


                    break;
                case FeedModel.DOUBLE_IMAGE_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((DoubleImageType) holder).send_tv.setBackgroundColor(btnColor_int);

                    }

                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((DoubleImageType) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((DoubleImageType) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((DoubleImageType) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();
                    if (isLiked.equalsIgnoreCase("1")) {
                        ((DoubleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((DoubleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }

                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((DoubleImageType) holder).pinImage.setImageResource(0);
                            ((DoubleImageType) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((DoubleImageType) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((DoubleImageType) holder).pinImage.setImageResource(0);
                            ((DoubleImageType) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((DoubleImageType) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {
                        ((DoubleImageType) holder).pinImage.setVisibility(View.GONE);

                    }


                    ((DoubleImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);
                        }
                    });

                    ((DoubleImageType) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareAlert(feedModel, position);
//                            EventBus.getDefault().post(new ShareBus(position));
                        }
                    });


                    ((DoubleImageType) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((DoubleImageType) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((DoubleImageType) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((DoubleImageType) holder).rc_feed_username.setText(feedModel.getName());

                    isLiked = feedModel.getIsLiked();
                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((DoubleImageType) holder).timeTV.setText(modifiedTime + "");


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((DoubleImageType) holder).rc_profile_iv);


                    ((DoubleImageType) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });
                    ((DoubleImageType) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });

                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                            .into(((DoubleImageType) holder).container_two_imageone);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                            .into(((DoubleImageType) holder).container_two_imagetwo);


                    ((DoubleImageType) holder).container_two_imageone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 0);

                        }
                    });
                    ((DoubleImageType) holder).container_two_imagetwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 1);

                        }
                    });
                    ((DoubleImageType) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    ((DoubleImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);
                        }
                    });
                    ((DoubleImageType) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });

                    ((DoubleImageType) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((DoubleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((DoubleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });


                    ((DoubleImageType) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });

                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((DoubleImageType) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((DoubleImageType) holder).commnet_one_con.setVisibility(View.GONE);
                        ((DoubleImageType) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;
                            ((DoubleImageType) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((DoubleImageType) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((DoubleImageType) holder).username1.setText(name1);
                            ((DoubleImageType) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((DoubleImageType) holder).time1.setText(modifiedTime);
                            ((DoubleImageType) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((DoubleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((DoubleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((DoubleImageType) holder).likeCount1.setText(commentCount1);


                            ((DoubleImageType) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((DoubleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((DoubleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((DoubleImageType) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((DoubleImageType) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((DoubleImageType) holder).username2.setText(name2);
                            ((DoubleImageType) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((DoubleImageType) holder).time2.setText(modifiedTime);
                            ((DoubleImageType) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((DoubleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((DoubleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((DoubleImageType) holder).likeCount2.setText(commentCount2);


                            ((DoubleImageType) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((DoubleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((DoubleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });

                        }


                    } else {
                        ((DoubleImageType) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((DoubleImageType) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((DoubleImageType) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((DoubleImageType) holder).comment_et.setText("");
                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);


                        }
                    });


                    break;
                case FeedModel.TRIPLE_IMAGE_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((TripleImageType) holder).send_tv.setBackgroundColor(btnColor_int);

                    }

                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((TripleImageType) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((TripleImageType) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((TripleImageType) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((TripleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((TripleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }
                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((TripleImageType) holder).pinImage.setImageResource(0);
                            ((TripleImageType) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((TripleImageType) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((TripleImageType) holder).pinImage.setImageResource(0);
                            ((TripleImageType) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((TripleImageType) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {

                        ((TripleImageType) holder).pinImage.setVisibility(View.GONE);
                    }


                    ((TripleImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);
                        }
                    });


                    ((TripleImageType) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareAlert(feedModel, position);
//                            EventBus.getDefault().post(new ShareBus(position));
                        }
                    });

                    ((TripleImageType) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((TripleImageType) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((TripleImageType) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((TripleImageType) holder).rc_feed_username.setText(feedModel.getName());

                    isLiked = feedModel.getIsLiked();
                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((TripleImageType) holder).timeTV.setText(modifiedTime + "");


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((TripleImageType) holder).rc_profile_iv);


                    ((TripleImageType) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });

                    ((TripleImageType) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });

                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                            .into(((TripleImageType) holder).container_three_imageone);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                            .into(((TripleImageType) holder).container_three_imagetwo);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(2)).fit().centerCrop()


                            .into(((TripleImageType) holder).container_three_imagethree);


                    ((TripleImageType) holder).container_three_imageone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 0);

                        }
                    });
                    ((TripleImageType) holder).container_three_imagetwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 1);

                        }
                    });
                    ((TripleImageType) holder).container_three_imagethree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 2);

                        }
                    });
                    ((TripleImageType) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    ((TripleImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);
                        }
                    });

                    ((TripleImageType) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });
                    ((TripleImageType) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((TripleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((TripleImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });

                    ((TripleImageType) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });


                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((TripleImageType) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((TripleImageType) holder).commnet_one_con.setVisibility(View.GONE);
                        ((TripleImageType) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;
                            ((TripleImageType) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((TripleImageType) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((TripleImageType) holder).username1.setText(name1);
                            ((TripleImageType) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());
                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((TripleImageType) holder).time1.setText(modifiedTime);
                            ((TripleImageType) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((TripleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((TripleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((TripleImageType) holder).likeCount1.setText(commentCount1);


                            ((TripleImageType) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((TripleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((TripleImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((TripleImageType) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((TripleImageType) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((TripleImageType) holder).username2.setText(name2);
                            ((TripleImageType) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((TripleImageType) holder).time2.setText(modifiedTime);
                            ((TripleImageType) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());

                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((TripleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((TripleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((TripleImageType) holder).likeCount2.setText(commentCount2);


                            ((TripleImageType) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((TripleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((TripleImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }


                    } else {
                        ((TripleImageType) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((TripleImageType) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((TripleImageType) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((TripleImageType) holder).comment_et.setText("");
                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);


                        }
                    });

                    break;

                case FeedModel.FOUR_IMAGE_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((FourImageType) holder).send_tv.setBackgroundColor(btnColor_int);

                    }

                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((FourImageType) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((FourImageType) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((FourImageType) holder).postDescriptionTV.setText(description);

                    }

                    isLiked = feedModel.getIsLiked();


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((FourImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((FourImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }

                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((FourImageType) holder).pinImage.setImageResource(0);
                            ((FourImageType) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((FourImageType) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((FourImageType) holder).pinImage.setImageResource(0);
                            ((FourImageType) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((FourImageType) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {
                        ((FourImageType) holder).pinImage.setVisibility(View.GONE);

                    }


                    ((FourImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);
                        }
                    });


                    ((FourImageType) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            EventBus.getDefault().post(new ShareBus(position));
                            shareAlert(feedModel, position);

                        }
                    });

                    ((FourImageType) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((FourImageType) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((FourImageType) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((FourImageType) holder).rc_feed_username.setText(feedModel.getName());

                    isLiked = feedModel.getIsLiked();
                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((FourImageType) holder).timeTV.setText(modifiedTime + "");


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((FourImageType) holder).rc_profile_iv);


                    ((FourImageType) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });
                    ((FourImageType) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });


                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(0)).fit().centerCrop()

                            .into(((FourImageType) holder).container_four_imageone);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(1)).fit().centerCrop()

                            .into(((FourImageType) holder).container_four_imagetwo);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(2)).fit().centerCrop()

                            .into(((FourImageType) holder).container_four_imagethree);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(3)).fit().centerCrop()

                            .into(((FourImageType) holder).container_four_imagefour);
                    ((FourImageType) holder).container_four_imageone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 0);

                        }
                    });
                    ((FourImageType) holder).container_four_imagetwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 1);

                        }
                    });

                    ((FourImageType) holder).container_four_imagethree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 2);

                        }
                    });

                    ((FourImageType) holder).container_four_imagefour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 3);

                        }
                    });

                    ((FourImageType) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    ((FourImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);

                        }
                    });

                    ((FourImageType) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });

                    ((FourImageType) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((FourImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((FourImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });


                    ((FourImageType) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });


                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((FourImageType) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((FourImageType) holder).commnet_one_con.setVisibility(View.GONE);
                        ((FourImageType) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;
                            ((FourImageType) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((FourImageType) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((FourImageType) holder).username1.setText(name1);
                            ((FourImageType) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);

                            ((FourImageType) holder).time1.setText(modifiedTime);
                            ((FourImageType) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((FourImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((FourImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((FourImageType) holder).likeCount1.setText(commentCount1);


                            ((FourImageType) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((FourImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((FourImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((FourImageType) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((FourImageType) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((FourImageType) holder).username2.setText(name2);
                            ((FourImageType) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((FourImageType) holder).time2.setText(modifiedTime);
                            ((FourImageType) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());

                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((FourImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((FourImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((FourImageType) holder).likeCount2.setText(commentCount2);


                            ((FourImageType) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((FourImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((FourImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });

                        }


                    } else {
                        ((FourImageType) holder).commentContainerFeed.setVisibility(View.GONE);
                    }

                    ((FourImageType) holder).send_tv.setEnabled(true);
                    ((FourImageType) holder).send_tv.setVisibility(View.VISIBLE);
                    ((FourImageType) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((FourImageType) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((FourImageType) holder).comment_et.setText("");
                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);


                        }
                    });


                    break;

                case FeedModel.MULTI_IMAGE_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((MultiImageType) holder).send_tv.setBackgroundColor(btnColor_int);

                    }


                    int counterSizeImage = Integer.valueOf(feedModel.getPostTotalImages()) - 4;

                    ((MultiImageType) holder).imageCounterTV.setText(String.valueOf(counterSizeImage) + "+");

                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((MultiImageType) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((MultiImageType) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((MultiImageType) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((MultiImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((MultiImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }


                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((MultiImageType) holder).pinImage.setImageResource(0);
                            ((MultiImageType) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((MultiImageType) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((MultiImageType) holder).pinImage.setImageResource(0);
                            ((MultiImageType) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((MultiImageType) holder).pinImage.setColorFilter(greyPin);

                        }
                    } else {
                        ((MultiImageType) holder).pinImage.setVisibility(View.GONE);
                    }


                    ((MultiImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);
                        }
                    });


                    ((MultiImageType) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            EventBus.getDefault().post(new ShareBus(position));

                            shareAlert(feedModel, position);
                        }
                    });


                    ((MultiImageType) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((MultiImageType) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((MultiImageType) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((MultiImageType) holder).rc_feed_username.setText(feedModel.getName());

                    isLiked = feedModel.getIsLiked();
                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((MultiImageType) holder).timeTV.setText(modifiedTime + "");


                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((MultiImageType) holder).rc_profile_iv);

                    ((MultiImageType) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });

                    ((MultiImageType) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });

                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(0)).fit().centerCrop()


                            .into(((MultiImageType) holder).container_multi_imageone);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(1)).fit().centerCrop()


                            .into(((MultiImageType) holder).container_multi_imagetwo);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(2)).fit().centerCrop()


                            .into(((MultiImageType) holder).container_multi_imagethree);
                    Picasso.with(activity)
                            .load(feedModel.getPostattachments().get(3)).fit().centerCrop()

                            .into(((MultiImageType) holder).container_multi_imagefour);


                    ((MultiImageType) holder).container_multi_imageone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 0);

                        }
                    });

                    ((MultiImageType) holder).container_multi_imagetwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 1);

                        }
                    });

                    ((MultiImageType) holder).container_multi_imagethree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 2);

                        }
                    });

                    ((MultiImageType) holder).container_multi_imagefour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            setImageIntent(feedModel.getPostattachments(), 3);

                        }
                    });


                    ((MultiImageType) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);


                        }
                    });
                    ((MultiImageType) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });
                    ((MultiImageType) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((MultiImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((MultiImageType) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });


                    ((MultiImageType) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String isPinned = feedModel.getIsPined();


                            if (isPinned.equalsIgnoreCase("1")) {

                                // make it unPin

                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }


                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());

                            } else {
                                // make it Pin

                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }


                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }


                            notifyDataSetChanged();
                        }
                    });
                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((MultiImageType) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((MultiImageType) holder).commnet_one_con.setVisibility(View.GONE);
                        ((MultiImageType) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;
                            ((MultiImageType) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((MultiImageType) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((MultiImageType) holder).username1.setText(name1);
                            ((MultiImageType) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());


                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((MultiImageType) holder).time1.setText(modifiedTime);
                            ((MultiImageType) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((MultiImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((MultiImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((MultiImageType) holder).likeCount1.setText(commentCount1);


                            ((MultiImageType) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((MultiImageType) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((MultiImageType) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((MultiImageType) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((MultiImageType) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((MultiImageType) holder).username2.setText(name2);
                            ((MultiImageType) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());
                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);

                            ((MultiImageType) holder).time2.setText(modifiedTime);
                            ((MultiImageType) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((MultiImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((MultiImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((MultiImageType) holder).likeCount2.setText(commentCount2);


                            ((MultiImageType) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((MultiImageType) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((MultiImageType) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });

                        }


                    } else {
                        ((MultiImageType) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((MultiImageType) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((MultiImageType) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }


                            ((MultiImageType) holder).comment_et.setText("");
                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);


                        }
                    });


                    break;


                case FeedModel.TEXT_TYPE:


                    if (!appMainColor.equalsIgnoreCase("")) {

                        ((TextViewHolder) holder).send_tv.setBackgroundColor(btnColor_int);


                    }


                    description = feedModel.getPostDescription();
                    if (description.equalsIgnoreCase("")) {
                        ((TextViewHolder) holder).image_bodyContainer_ln.setVisibility(View.GONE);
                    } else {
                        ((TextViewHolder) holder).image_bodyContainer_ln.setVisibility(View.VISIBLE);
                        ((TextViewHolder) holder).postDescriptionTV.setText(description);

                    }


                    isLiked = feedModel.getIsLiked();


                    if (isLiked.equalsIgnoreCase("1")) {
                        ((TextViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                    } else {
                        ((TextViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);
                    }
                    if (statusMyProfile == 1) {
                        isPined = feedModel.getIsPined();
                        if (isPined.equalsIgnoreCase("1")) {
                            ((TextViewHolder) holder).pinImage.setImageResource(0);
                            ((TextViewHolder) holder).pinImage.setImageResource(R.drawable.ic_pined);
                            ((TextViewHolder) holder).pinImage.setColorFilter(btnColor_int);
                        } else {
                            ((TextViewHolder) holder).pinImage.setImageResource(0);
                            ((TextViewHolder) holder).pinImage.setImageResource(R.drawable.ic_un_pinned);
                            ((TextViewHolder) holder).pinImage.setColorFilter(greyPin);
                        }
                    } else {
                        ((TextViewHolder) holder).pinImage.setVisibility(View.GONE);
                    }


                    ((TextViewHolder) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//
//                            likesIntent = new Intent(activity, LikesActivity.class);
//                            activity.startActivity(likesIntent);

                        }
                    });


                    ((TextViewHolder) holder).shareCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareAlert(feedModel, position);
//                            EventBus.getDefault().post(new ShareBus(position));
                        }
                    });

                    ((TextViewHolder) holder).rc_feed_comments_tv.setText(feedModel.getPostCommentsCount());
                    ((TextViewHolder) holder).rc_feed_likes_tv.setText(feedModel.getPostLikesCount());
                    ((TextViewHolder) holder).share_counter_tv.setText(feedModel.getPostSharesCount());
                    ((TextViewHolder) holder).rc_feed_username.setText(feedModel.getName());


                    modifiedTime = GlobalClass.parseDate(feedModel.getDate(), activity);
                    ((TextViewHolder) holder).timeTV.setText(modifiedTime + "");

                    Picasso.with(activity)
                            .load(Urls.BASE_URL_IMAGE + feedModel.getImage()).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform())
                            .into(((TextViewHolder) holder).rc_profile_iv);


                    ((TextViewHolder) holder).rc_profile_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gotToProfile(feedModel.getUserId());

                        }
                    });

                    ((TextViewHolder) holder).rc_feed_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            gotToProfile(feedModel.getUserId());
                        }
                    });

                    ((TextViewHolder) holder).rc_feed_comments_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    });
                    ((TextViewHolder) holder).rc_feed_likes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            String postID = feedModel.getPostID();
//
//
//                            Intent likesIntent = new Intent(activity, LikesActivity.class);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("postID", postID);
//                            likesIntent.putExtras(bundle);
//                            activity.startActivity(likesIntent);

                        }
                    });
                    ((TextViewHolder) holder).chatCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postID = feedModel.getPostID();


                            Intent commentintent = new Intent(activity, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("postID", postID);
                            bundle.putString("position", String.valueOf(position));
                            commentintent.putExtras(bundle);
                            activity.startActivity(commentintent);

                        }
                    });
                    ((TextViewHolder) holder).rc_feed_likes_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String postLiked = feedModel.getIsLiked();
                            String likeCount = feedModel.getPostLikesCount();

                            if (postLiked.equalsIgnoreCase("1")) {
                                //unlike
                                ((TextViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_unlike);


                                int likeCountValue = Integer.valueOf(likeCount) - 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("0");
                                PostLike(feedModel.getPostID());

                            } else {
                                //like
                                ((TextViewHolder) holder).rc_feed_likes_iv.setImageResource(R.drawable.ic_like);
                                int likeCountValue = Integer.valueOf(likeCount) + 1;
                                feedModel.setPostLikesCount(String.valueOf(likeCountValue));
                                feedModel.setIsLiked("1");
                                PostLike(feedModel.getPostID());
                            }

                            notifyDataSetChanged();
                        }
                    });

                    ((TextViewHolder) holder).pinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String isPinned = feedModel.getIsPined();
                            if (isPinned.equalsIgnoreCase("1")) {
                                // make it unPin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }

                                feedModel.setIsPined("0");
                                postPin(feedModel.getPostID());
                            } else {
                                // make it Pin
                                for (int i = 0; i < list.size(); i++) {
                                    list.get(i).setIsPined("0");
                                }
                                feedModel.setIsPined("1");
                                postPin(feedModel.getPostID());
                            }
                            notifyDataSetChanged();
                        }
                    });

                    commentListSize = feedModel.getCommentListSize();
                    Log.d("commentSizes", commentListSize + "");
                    if (commentListSize > 0) {
                        ((TextViewHolder) holder).commentContainerFeed.setVisibility(View.VISIBLE);
                        ((TextViewHolder) holder).commnet_one_con.setVisibility(View.GONE);
                        ((TextViewHolder) holder).comment_two_con.setVisibility(View.GONE);


                        if (commentListSize > 0) {

                            final int index1 = 0;


                            ((TextViewHolder) holder).commnet_one_con.setVisibility(View.VISIBLE);


                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index1).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((TextViewHolder) holder).userImage1);

                            String name1 = feedModel.getCommentsList().get(index1).getName();
                            ((TextViewHolder) holder).username1.setText(name1);
                            ((TextViewHolder) holder).comment1.setText(feedModel.getCommentsList().get(index1).getComment());
                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index1).getDate(), activity);
                            ((TextViewHolder) holder).time1.setText(modifiedTime);
                            ((TextViewHolder) holder).likeCount1.setText(feedModel.getCommentsList().get(index1).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();
                            String commentCount1 = feedModel.getCommentsList().get(index1).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((TextViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_like);
                            } else {
                                ((TextViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);
                            }
                            ((TextViewHolder) holder).likeCount1.setText(commentCount1);


                            ((TextViewHolder) holder).imageHeart1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index1).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((TextViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());
                                    } else {
                                        //UnLike

                                        ((TextViewHolder) holder).imageHeart1.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index1).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index1).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index1).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index1).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }
                        if (commentListSize > 1) {

                            final int index2 = 1;

                            ((TextViewHolder) holder).comment_two_con.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(Urls.BASE_URL_IMAGE + "" + feedModel.getCommentsList().get(index2).getImage())

                                    .fit().centerCrop()
                                    .placeholder(R.drawable.profile_placeholder)
                                    .transform(new CircleTransform())
                                    .into(((TextViewHolder) holder).userImage2);

                            String name2 = feedModel.getCommentsList().get(index2).getName();
                            ((TextViewHolder) holder).username2.setText(name2);
                            ((TextViewHolder) holder).comment2.setText(feedModel.getCommentsList().get(index2).getComment());

                            modifiedTime = GlobalClass.parseDate(feedModel.getCommentsList().get(index2).getDate(), activity);
                            ((TextViewHolder) holder).time2.setText(modifiedTime);
                            ((TextViewHolder) holder).likeCount2.setText(feedModel.getCommentsList().get(index2).getTotlaLikes());


                            String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();
                            String commentCount2 = feedModel.getCommentsList().get(index2).getTotlaLikes();
                            if (heartImgValue.equalsIgnoreCase("1")) {
                                ((TextViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_like);
                            } else {
                                ((TextViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);
                            }
                            ((TextViewHolder) holder).likeCount2.setText(commentCount2);


                            ((TextViewHolder) holder).imageHeart2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String heartImgValue = feedModel.getCommentsList().get(index2).getIsLike();

                                    if (heartImgValue.equalsIgnoreCase("1")) {
                                        //UnLike
                                        ((TextViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_unlike);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) - 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("0");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());
                                    } else {
                                        //UnLike

                                        ((TextViewHolder) holder).imageHeart2.setImageResource(R.drawable.ic_like);


                                        String totalCommentLike = feedModel.getCommentsList().get(index2).getTotlaLikes();

                                        int valueTotal = Integer.valueOf(totalCommentLike) + 1;

                                        feedModel.getCommentsList().get(index2).setTotlaLikes(String.valueOf(valueTotal));
                                        feedModel.getCommentsList().get(index2).setIsLike("1");

                                        PostCommentLike(feedModel.getCommentsList().get(index2).getCommentID());

                                    }

                                    notifyDataSetChanged();

                                }
                            });


                        }


                    } else {
                        ((TextViewHolder) holder).commentContainerFeed.setVisibility(View.GONE);
                    }


                    ((TextViewHolder) holder).send_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String commentValue = ((TextViewHolder) holder).comment_et.getText().toString().trim();
                            if (commentValue.equalsIgnoreCase("")) {

                                Toast.makeText(activity, activity.getResources().getString(R.string.enter_comment), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ((TextViewHolder) holder).comment_et.setText("");


                            PostComment(position, Integer.valueOf(feedModel.getPostID()), commentValue);

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


    private void setImageIntent(ArrayList<String> photoArray, int pos) {

        Intent fullimageIntnent = new Intent(activity, ImageViewMultiActivity.class);
        fullimageIntnent.putExtra("imageList", photoArray);
        fullimageIntnent.putExtra("selectedPos", String.valueOf(pos));
        activity.startActivity(fullimageIntnent);

    }

    public static class EventTypeViewHolder extends RecyclerView.ViewHolder {


        public EventTypeViewHolder(View itemView) {
            super(itemView);


        }

    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        ImageView rc_profile_iv;
        TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;

        ImageView rc_feed_likes_iv;
        LinearLayout chatCon;
        ImageView pinImage;
        TextView postDescriptionTV;
        LinearLayout image_bodyContainer_ln;
        ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        LinearLayout commentContainerFeed;
        RelativeLayout commnet_one_con, comment_two_con;
        EditText comment_et;
        TextView send_tv;

        public TextViewHolder(View itemView) {
            super(itemView);


            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);
            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);


            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);

            pinImage = itemView.findViewById(R.id.pinImage);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);

            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);

        }

    }

    public static class SingleImageViewHolder extends RecyclerView.ViewHolder {
        private TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        ImageView rc_profile_iv;
        TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        ImageView single_image_one;
        ImageView rc_feed_likes_iv;
        LinearLayout chatCon;
        ImageView pinImage;
        TextView postDescriptionTV;
        LinearLayout image_bodyContainer_ln;
        ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        LinearLayout commentContainerFeed;
        RelativeLayout commnet_one_con, comment_two_con;
        EditText comment_et;
        TextView send_tv;

        public SingleImageViewHolder(View itemView) {
            super(itemView);


            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);
            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);


            single_image_one = itemView.findViewById(R.id.single_image_one);


            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);

            pinImage = itemView.findViewById(R.id.pinImage);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);

            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);

        }

    }

    public static class DoubleImageType extends RecyclerView.ViewHolder {
        TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        ImageView rc_profile_iv;

        TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        ImageView container_two_imageone, container_two_imagetwo;
        ImageView rc_feed_likes_iv;
        LinearLayout chatCon;
        ImageView pinImage;
        TextView postDescriptionTV;
        LinearLayout image_bodyContainer_ln;
        LinearLayout commentContainerFeed;
        RelativeLayout commnet_one_con, comment_two_con;
        ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        EditText comment_et;
        TextView send_tv;

        public DoubleImageType(View itemView) {
            super(itemView);

            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);


            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);


            container_two_imageone = itemView.findViewById(R.id.container_two_imageone);
            container_two_imagetwo = itemView.findViewById(R.id.container_two_imagetwo);


            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);
            pinImage = itemView.findViewById(R.id.pinImage);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);

            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);
        }

    }

    public static class TripleImageType extends RecyclerView.ViewHolder {
        EditText comment_et;
        TextView send_tv;
        TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        ImageView rc_profile_iv;

        TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        ImageView container_three_imageone, container_three_imagetwo, container_three_imagethree;
        ImageView rc_feed_likes_iv;
        LinearLayout chatCon;
        ImageView pinImage;
        TextView postDescriptionTV;
        LinearLayout image_bodyContainer_ln;
        ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        LinearLayout commentContainerFeed;
        RelativeLayout commnet_one_con, comment_two_con;

        public TripleImageType(View itemView) {
            super(itemView);
            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);


            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);


            container_three_imageone = itemView.findViewById(R.id.container_three_imageone);
            container_three_imagetwo = itemView.findViewById(R.id.container_three_imagetwo);
            container_three_imagethree = itemView.findViewById(R.id.container_three_imagethree);

            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);
            pinImage = itemView.findViewById(R.id.pinImage);


            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);
            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);
        }

    }

    public static class FourImageType extends RecyclerView.ViewHolder {
        private ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        private TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        private TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        private ImageView rc_profile_iv;

        private TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        private ImageView container_four_imageone, container_four_imagetwo, container_four_imagethree, container_four_imagefour;
        private ImageView rc_feed_likes_iv;
        private LinearLayout chatCon;
        private ImageView pinImage;
        private TextView postDescriptionTV;
        private LinearLayout image_bodyContainer_ln;
        private LinearLayout commentContainerFeed;
        private RelativeLayout commnet_one_con, comment_two_con;
        private EditText comment_et;
        private TextView send_tv;

        public FourImageType(View itemView) {
            super(itemView);


            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);


            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);

            container_four_imageone = itemView.findViewById(R.id.container_four_imageone);
            container_four_imagetwo = itemView.findViewById(R.id.container_four_imagetwo);
            container_four_imagethree = itemView.findViewById(R.id.container_four_imagethree);
            container_four_imagefour = itemView.findViewById(R.id.container_four_imagefour);


            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);
            pinImage = itemView.findViewById(R.id.pinImage);

            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);

            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);
            comment_et = itemView.findViewById(R.id.comment_et);

            send_tv = itemView.findViewById(R.id.send_tv);

        }

    }

    public static class MultiImageType extends RecyclerView.ViewHolder {
        private TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        private ImageView rc_profile_iv;
        private ImageView pinImage;
        private TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        private TextView imageCounterTV;
        private ImageView container_multi_imageone, container_multi_imagetwo, container_multi_imagethree, container_multi_imagefour;
        private ImageView rc_feed_likes_iv;
        private LinearLayout chatCon;
        private TextView postDescriptionTV;
        private LinearLayout image_bodyContainer_ln;
        private ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        private TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        private LinearLayout commentContainerFeed;
        private RelativeLayout commnet_one_con, comment_two_con;
        private EditText comment_et;
        private TextView send_tv;

        public MultiImageType(View itemView) {
            super(itemView);
            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);


            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);

            container_multi_imageone = itemView.findViewById(R.id.container_multi_imageone);
            container_multi_imagetwo = itemView.findViewById(R.id.container_multi_imagetwo);
            container_multi_imagethree = itemView.findViewById(R.id.container_multi_imagethree);
            container_multi_imagefour = itemView.findViewById(R.id.container_multi_imagefour);
            imageCounterTV = itemView.findViewById(R.id.imageCounterTV);

            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);
            pinImage = itemView.findViewById(R.id.pinImage);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);
            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);

        }

    }

    public static class VideoViewHolderType extends RecyclerView.ViewHolder {
        TextView rc_feed_likes_tv;
        private LinearLayout shareCon;
        ImageView pinImage;

        ImageView rc_profile_iv;

        TextView rc_feed_username, timeTV, rc_feed_comments_tv, share_counter_tv;
        ImageView rc_feed_likes_iv;
        LinearLayout chatCon;

        TextView postDescriptionTV;
        LinearLayout image_bodyContainer_ln;
        ImageView userImage1, userImage2, imageHeart1, imageHeart2;
        TextView username1, username2, comment1, comment2, time1, time2, likeCount1, likeCount2;
        LinearLayout commentContainerFeed;
        RelativeLayout commnet_one_con, comment_two_con;
        EditText comment_et;
        TextView send_tv;
        ImageView single_image_one;

        public VideoViewHolderType(View itemView) {
            super(itemView);
            rc_feed_likes_tv = itemView.findViewById(R.id.rc_feed_likes_tv);
            shareCon = itemView.findViewById(R.id.shareCon);

            rc_profile_iv = itemView.findViewById(R.id.rc_profile_iv);
            rc_feed_username = itemView.findViewById(R.id.rc_feed_username);
            timeTV = itemView.findViewById(R.id.timeTV);

            rc_feed_comments_tv = itemView.findViewById(R.id.rc_feed_comments_tv);
            share_counter_tv = itemView.findViewById(R.id.share_counter_tv);


            rc_feed_likes_iv = itemView.findViewById(R.id.rc_feed_likes_iv);
            chatCon = itemView.findViewById(R.id.chatCon);


            pinImage = itemView.findViewById(R.id.pinImage);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            image_bodyContainer_ln = itemView.findViewById(R.id.image_bodyContainer_ln);


            commentContainerFeed = itemView.findViewById(R.id.commentContainerFeed);
            commnet_one_con = itemView.findViewById(R.id.commnet_one_con);
            comment_two_con = itemView.findViewById(R.id.comment_two_con);


            userImage1 = itemView.findViewById(R.id.userImage1);
            userImage2 = itemView.findViewById(R.id.userImage2);
            username1 = itemView.findViewById(R.id.username1);
            username2 = itemView.findViewById(R.id.username2);
            comment1 = itemView.findViewById(R.id.comment1);
            comment2 = itemView.findViewById(R.id.comment2);

            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

            likeCount1 = itemView.findViewById(R.id.likeCount1);
            likeCount2 = itemView.findViewById(R.id.likeCount2);

            imageHeart1 = itemView.findViewById(R.id.imageHeart1);
            imageHeart2 = itemView.findViewById(R.id.imageHeart2);
            comment_et = itemView.findViewById(R.id.comment_et);
            send_tv = itemView.findViewById(R.id.send_tv);


            single_image_one = itemView.findViewById(R.id.single_image_one);
        }

    }

    public static class UserInfoViewHolderType extends RecyclerView.ViewHolder {


        private LinearLayout edit_container;


        private TextView job_title_tv, userName_tv, followers_count_tv, following_count_tv, total_posts_tv;
        private LinearLayout following_bar_ln;

        private CircleImageView profile_image;
        private TextView follower_txt_tv, following_txt_tv, posts_txt_tv;


        public UserInfoViewHolderType(View itemView) {
            super(itemView);



            profile_image = itemView.findViewById(R.id.profile_image);
            edit_container = itemView.findViewById(R.id.edit_container);


            job_title_tv = itemView.findViewById(R.id.job_title_tv);
            userName_tv = itemView.findViewById(R.id.userName_tv);
            followers_count_tv = itemView.findViewById(R.id.followers_count_tv);
            following_count_tv = itemView.findViewById(R.id.following_count_tv);
            total_posts_tv = itemView.findViewById(R.id.total_posts_tv);
            following_bar_ln = itemView.findViewById(R.id.following_bar_ln);

            follower_txt_tv = itemView.findViewById(R.id.follower_txt_tv);
            following_txt_tv = itemView.findViewById(R.id.following_txt_tv);
            posts_txt_tv = itemView.findViewById(R.id.posts_txt_tv);
        }

    }

    private void PostLike(String postID) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("postId", postID);


            WebReq.post(activity, "like", mParams, new MyTextHttpResponseHandlerPostLike());

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


    private void postPin(String postID) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("postId", postID);


            WebReq.post(activity, "pinPost", mParams, new MyTextHttpResponseHandlerPostPin());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
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


    private void PostComment(int pos, int id, String commentValue) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();
            String userIDCurrent = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userIDCurrent);
            mParams.put("postId", id);
            mParams.put("comment", commentValue);


            Log.d("postCommentParams", mParams + "");
            WebReq.post(activity, "comment", mParams, new MyTextHttpResponseHandlerPostComment(pos, id));

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
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


                        if (nature.equalsIgnoreCase("mainFeed")) {
                            EventBus.getDefault().post(new FeedGetCommentsEvent(pos, id));
                        } else {
                            EventBus.getDefault().post(new AttendeCommentsEvent(pos, id));
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


    private void PostCommentLike(String commentID) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();

            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("commentId", commentID);

            WebReq.post(activity, "likeToComment", mParams, new MyTextHttpResponseHandlerPostCommentLike());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerPostCommentLike extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerPostCommentLike() {


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

    private void followUNfollow(String followerId) {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();


            String userID = GlobalClass.getPref("userID", activity);
            mParams.put("userId", userID);
            mParams.put("followerId", followerId);

            WebReq.post(activity, "follow", mParams, new MyTextHttpResponseHandlerFollow());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerFollow extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerFollow() {


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
            Log.d("postFollow", mResponse.toString() + "Respo");
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


    public void shareAlert(FeedModel feedModel, int pos) {


        String postType = feedModel.getPostMediaType();
        if (postType.equalsIgnoreCase("text")) {

            String shareBody = feedModel.getPostDescription();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_using)));


        } else if (postType.equalsIgnoreCase("image")) {
            ArrayList<String> videoLink = new ArrayList<>();
            videoLink = feedModel.getPostattachments();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < videoLink.size(); i++) {

                String link = videoLink.get(0);

                sb.append(link + "\n");

            }


            String shareBody = feedModel.getPostDescription() + " \n " + sb;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_using)));


        } else if (postType.equalsIgnoreCase("video")) {


            String videoLink = feedModel.getPostattachments().get(0).toString();
            String shareBody = feedModel.getPostDescription() + " \n " + Urls.BASEURL + videoLink;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_using)));

        }


//        ImageView fb_img, twitter_img, insta_img, linked_in_img;
//        Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_share);
//
//        fb_img = dialog.findViewById(R.id.fb_img);
//        twitter_img = dialog.findViewById(R.id.twitter_img);
//        insta_img = dialog.findViewById(R.id.insta_img);
//        linked_in_img = dialog.findViewById(R.id.linked_in_img);
//
//
//        fb_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(activity, "fb", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//        twitter_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(activity, "twitter", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        insta_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//
//        });
//        linked_in_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        dialog.show();dr

    }

    private void gotToProfile(String userID) {

        String currentUserID = GlobalClass.getPref("userID", activity);

        if (currentUserID.equals(userID)) {

            if (nature.equals("attendee")) {

            } else {
                Intent i = new Intent(activity, AttendeProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isMyyProfile", "1");
                bundle.putString("nature", "attendee");
                bundle.putString("userID", userID);
                i.putExtras(bundle);
                activity.startActivity(i);

            }


        } else {


            if (nature.equals("attendee")) {

            } else {
                Intent i = new Intent(activity, AttendeProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isMyyProfile", "0");
                bundle.putString("nature", "attendee");
                bundle.putString("userID", userID);
                i.putExtras(bundle);
                activity.startActivity(i);
            }


        }


    }
}