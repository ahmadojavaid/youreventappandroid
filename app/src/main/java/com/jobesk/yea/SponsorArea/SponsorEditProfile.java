package com.jobesk.yea.SponsorArea;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.Fragments.SponsorHomeFrag;
import com.jobesk.yea.Utils.CircleTransform;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import customfonts.TextView_fira_sans_medium;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SponsorEditProfile extends AppCompatActivity {
    TextView toolbar_title_tv;
    ImageView back_img;
    private ImageView logo_toolbar;
    private LinearLayout toolbar_header;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private String image, name, description, website;
    private EditText username_tv, weblink_tv, description_tv;
    private TextView save_tv;
    private CircleImageView useriamge;
    private String nameNEw, descNew, welinkNew;
    private String TAG = "SponsorEditProfile";
    private FrameLayout imageSelect;
    private String userID;
    private String encodedImage = "";
    boolean imageUploaded = false;

    private String sponsorName, sponsorImage, sponsorDescription, sponsorTitle, sponsorshipLevel, sponsorwebLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_edit);

        userID = GlobalClass.getPref("userID", getApplicationContext());
        Intent i = getIntent();
        image = i.getStringExtra("image");
        name = i.getStringExtra("name");
        description = i.getStringExtra("description");
        website = i.getStringExtra("website");


        imageSelect = findViewById(R.id.imageSelect);

        description_tv = findViewById(R.id.description_tv);
        username_tv = findViewById(R.id.username_tv);
        useriamge = findViewById(R.id.useriamge);
        weblink_tv = findViewById(R.id.weblink_tv);

        save_tv = findViewById(R.id.save_tv);


        description_tv.setText(description);
        username_tv.setText(name);
        weblink_tv.setText(website);


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + image)
                .fit().centerCrop()
                .transform(new CircleTransform())
                .placeholder(R.drawable.profile_placeholder)
                .into(useriamge);


        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.edit_profile));


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.hideKeyboard(SponsorEditProfile.this);

                finish();
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


        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nameNEw = username_tv.getText().toString().trim();
                descNew = description_tv.getText().toString().trim();
                welinkNew = weblink_tv.getText().toString().trim();

                if (nameNEw.equalsIgnoreCase("")) {
                    Toast.makeText(SponsorEditProfile.this, getApplicationContext().getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (descNew.equalsIgnoreCase("")) {
                    Toast.makeText(SponsorEditProfile.this, getApplicationContext().getResources().getString(R.string.enter_description), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (welinkNew.equalsIgnoreCase("")) {
                    Toast.makeText(SponsorEditProfile.this, getApplicationContext().getResources().getString(R.string.enter_website_link), Toast.LENGTH_SHORT).show();
                    return;
                }

                UpdateSponsorDetail();
            }
        });


        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSelectImageClick(v);


            }
        });


    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            startCropImageActivity(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                Bitmap bitmap = null;
                Bitmap compressedImageBitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    File file = new File(resultUri.getPath());
                    compressedImageBitmap = new Compressor(this).setQuality(50).compressToBitmap(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


                useriamge.setImageBitmap(compressedImageBitmap);


                encodedImage = encoded;
                imageUploaded = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setOutputCompressQuality(50)
                .start(this);
    }


    private void UpdateSponsorDetail() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            if (imageUploaded == true) {
                mParams.put("sponsorImage", encodedImage);

            }


            mParams.put("sponsorwebLink", welinkNew);
            mParams.put("sponsorDescription", descNew);
            mParams.put("sponsorName", nameNEw);


            WebReq.post(getApplicationContext(), "sponsor/" + userID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(SponsorEditProfile.this);
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


                        JSONObject jsonObject = mResponse.getJSONObject("Result");




                        Toast.makeText(SponsorEditProfile.this, "Profile is updated Successfully!" , Toast.LENGTH_SHORT).show();

                        finish();


//
//                        sponsorName = jsonObject.getString("sponsorName");
//                        sponsorImage = jsonObject.getString("sponsorImage");
//                        sponsorDescription = jsonObject.getString("sponsorDescription");
//                        sponsorTitle = jsonObject.getString("sponsorTitle");
//                        sponsorshipLevel = jsonObject.getString("sponsorshipLevel");
//                        sponsorwebLink = jsonObject.getString("sponsorwebLink");
//
//
//
//
//
//
//                        GlobalClass.putPref("userName", sponsorName, getApplicationContext());
//                        GlobalClass.putPref("userImage", sponsorImage, getApplicationContext());
//                        GlobalClass.putPref("companyName", companyName, getApplicationContext());
//                        GlobalClass.putPref("jobTitle", jobTitle, getApplicationContext());
//                        GlobalClass.putPref("contact", contact, getApplicationContext());
//                        GlobalClass.putPref("gender", gender, getApplicationContext());
//                        GlobalClass.putPref("followingCount", followingCount, getApplicationContext());


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
