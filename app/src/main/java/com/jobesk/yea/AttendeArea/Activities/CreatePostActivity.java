package com.jobesk.yea.AttendeArea.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jobesk.yea.AttendeArea.Adapter.MultiImageAdapter;
import com.jobesk.yea.AttendeArea.EventBus.FeedRefreshEvent;
import com.jobesk.yea.R;
import com.jobesk.yea.AttendeArea.Srevices.ImageUploadService;
import com.jobesk.yea.AttendeArea.Srevices.VideoUploadService;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePostActivity extends AppCompatActivity {
    private ImageView back_img;
    private CircleImageView video_imgview;
    private TextView toolbar_title_tv;
    private TextView add_photos_tv;
    private static final int REQUEST_CODE = 732;
    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<String> listOfImages = new ArrayList<>();
    private ArrayList<String> selectedimagesArray = new ArrayList<>();
    private final int REQUEST_WRITE_PERMISSION = 99;
    private RecyclerView recyclerView;
    private MultiImageAdapter mAdapter;
    private TextView post_tv;
    private EditText body_et;
    private String encodedImages;
    private TextView add_video_tv;

    private String bodyValue = "";
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 14;
    private String videoPath;
    // pick image=1
    // pick video=2
    // pick Text=0
    private int pickerCheck = 0;
    //    private File VideoFile;
    RelativeLayout videoCon;
    private boolean videoCheck = false;


    private String TAG = "CreatePostActivity";
    private ImageView userImage;
    private TextView userName_tv;
    private ImageView logo_toolbar;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        Fresco.initialize(getApplicationContext());
        userImage = findViewById(R.id.userImage);
        userName_tv = findViewById(R.id.userName_tv);
        String userName = GlobalClass.getPref("userName", getApplicationContext());
        String userImageLink = GlobalClass.getPref("userImage", getApplicationContext());
        userName_tv.setText(userName);
        userImage.setVisibility(View.GONE);
        userName_tv.setVisibility(View.GONE);

        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + userImageLink)
                .fit().centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .transform(new CircleTransform())
                .into(userImage);


        back_img = findViewById(R.id.back_img);
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.create_post));
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        add_photos_tv = findViewById(R.id.add_photos_tv);

        add_photos_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickerCheck = 1;
                requestPermission();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MultiImageAdapter(CreatePostActivity.this, listOfImages);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        video_imgview = findViewById(R.id.video_imgview);
        videoCon = findViewById(R.id.videoCon);

        post_tv = findViewById(R.id.post_tv);
        post_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bodyValue = body_et.getText().toString().trim();


                if (pickerCheck == 1) {
                    // pick image
                    if (listOfImages.size() > 0) {


                        if (selectedimagesArray.size() > 0) {
                            selectedimagesArray.clear();
                        }

                        for (int i = 0; i < listOfImages.size(); i++) {


                            String pathLink = listOfImages.get(i);

                            selectedimagesArray.add(pathLink);
                        }


                        invokeSerice();

                    } else {
                        Toast.makeText(CreatePostActivity.this, getApplicationContext().getResources().getString(R.string.select_images), Toast.LENGTH_SHORT).show();
                    }
                } else if (pickerCheck == 2) {
                    //upload Video Video

                    invokeVideoSerice();
                } else {

                    if (bodyValue.equalsIgnoreCase("")) {
                        Toast.makeText(CreatePostActivity.this, getApplicationContext().getResources().getString(R.string.enter_description), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PostFeed();
                }


            }
        });

        body_et = findViewById(R.id.body_et);


        add_video_tv = findViewById(R.id.add_video_tv);

        add_video_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCheck = 2;
                requestPermission();
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

            add_video_tv.setBackgroundColor(btnColor_int);
            add_photos_tv.setBackgroundColor(btnColor_int);
            userName_tv.setTextColor(btnColor_int);

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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

            if (pickerCheck == 1) {
                // pick image
                pickImageIntent();
            } else {
                //pick Video
                getVideo();
            }


        }
    }

    private void getVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pickerCheck == 1) {
                    // pick image
                    pickImageIntent();
                } else {
                    //pick Video
                    getVideo();
                }

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
        }


    }

    private void pickImageIntent() {


        Intent intent = new Intent(CreatePostActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (listOfImages.size() > 0) {
                    listOfImages.clear();
                }


                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
//                StringBuffer sb = new StringBuffer();
//                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for (String result : mResults) {
//                    sb.append(result).append(",");
                    listOfImages.add(result);
                    Log.d("singleLink", result + "");
                }
                mAdapter.notifyDataSetChanged();

                recyclerView.setVisibility(View.VISIBLE);
                videoCon.setVisibility(View.GONE);
                post_tv.setVisibility(View.VISIBLE);
//                Log.d("ImagesLink", sb + "");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (resultCode == RESULT_OK) {


                Uri selectedImageUri = data.getData();

                videoPath = getPath(selectedImageUri);
//                VideoFile = new File(videoPath);

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(selectedImageUri)
                        .into(video_imgview);

                videoCheck = true;


                recyclerView.setVisibility(View.GONE);
                videoCon.setVisibility(View.VISIBLE);
                post_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void invokeSerice() {
        Intent intent = new Intent(getApplicationContext(), ImageUploadService.class);
        intent.putExtra("ImageList", selectedimagesArray);
        intent.putExtra("bodyValue", bodyValue + "");
        Log.d("ImageList", selectedimagesArray + "/n" + String.valueOf(selectedimagesArray.size()));
        startService(intent);
        selectedimagesArray.clear();
        finish();

    }

    private void invokeVideoSerice() {
        Intent intent = new Intent(getApplicationContext(), VideoUploadService.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("bodyValue", bodyValue + "");

        startService(intent);
        selectedimagesArray.clear();
        finish();

    }


    private void PostFeed() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            String userID = GlobalClass.getPref("userID", getApplicationContext());


            RequestParams mParams = new RequestParams();
            mParams.put("userId", userID);
            mParams.put("postDescription", bodyValue);
            mParams.put("postMediaType", "text");


            Log.d("parmsTestPost", mParams + "");
            WebReq.post(getApplicationContext(), "/post", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            GlobalClass.showLoading(CreatePostActivity.this);
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


                        EventBus.getDefault().post(new FeedRefreshEvent());
                        finish();
                    } else {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {

            }
        }
    }


}
