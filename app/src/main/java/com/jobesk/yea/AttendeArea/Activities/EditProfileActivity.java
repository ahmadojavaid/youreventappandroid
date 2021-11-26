package com.jobesk.yea.AttendeArea.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Base64OutputStream;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {


    private ImageView back_img;
    private TextView toolbar_title_tv;
    private FrameLayout pickImage;
    private final int REQUEST_WRITE_PERMISSION = 92;
    private final int RESULT_LOAD_IMAGE = 41;
    private File compressedImageFile;
    private Uri selectedImage, uri;
    private boolean imageUploaded = false;
    private ImageView userImageView;
    private String encodedImage = "";
    private TextView update_profile_tv;
    private EditText userName_et, email_et, phone_et, company_name_et, job_title_et;
    private String name, phone, companyName, jobTitle;
    //    private RadioGroup radioSexGroup;
//    private int selectedId;
    private String TAG = "EditProfileActivity";
    private String gender;
    private ImageView logo_toolbar, camera_circle_orange_img;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private LinearLayout bg_container_ln;
    private Uri mCropImageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        GlobalClass.hideKeyboard(EditProfileActivity.this);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        toolbar_title_tv = findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getApplicationContext().getResources().getString(R.string.edit_profile));


        pickImage = findViewById(R.id.pickImage);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CropImage.startPickImageActivity(EditProfileActivity.this);


            }
        });


        userImageView = findViewById(R.id.userImageView);

        userName_et = findViewById(R.id.userName_et);
        email_et = findViewById(R.id.email_et);
        phone_et = findViewById(R.id.phone_et);
        company_name_et = findViewById(R.id.company_name_et);
        job_title_et = findViewById(R.id.job_title_et);


        String userName = GlobalClass.getPref("userName", getApplicationContext());
        String userEmail = GlobalClass.getPref("userEmail", getApplicationContext());
        String userImage = GlobalClass.getPref("userImage", getApplicationContext());
        String companyNamePre = GlobalClass.getPref("companyName", getApplicationContext());
        String jobTitlePre = GlobalClass.getPref("jobTitle", getApplicationContext());
        String contact = GlobalClass.getPref("contact", getApplicationContext());
        gender = GlobalClass.getPref("gender", getApplicationContext());


        if (!userName.equalsIgnoreCase("null")) {


            userName_et.setText(userName);
        }
        if (!companyNamePre.equalsIgnoreCase("null")) {


            company_name_et.setText(companyNamePre);

        }
        if (!jobTitlePre.equalsIgnoreCase("null")) {


            job_title_et.setText(jobTitlePre);

        }
        if (!contact.equalsIgnoreCase("null")) {


            phone_et.setText(contact);

        }

        email_et.setText(userEmail);
        email_et.setEnabled(false);


        Picasso.with(getApplicationContext())
                .load(Urls.BASE_URL_IMAGE + userImage)
                .fit()
                .transform(new CircleTransform())
                .placeholder(R.drawable.profile_placeholder)
                .centerCrop()
                .into(userImageView);


        update_profile_tv = findViewById(R.id.update_profile_tv);
        update_profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = userName_et.getText().toString().trim();
                phone = phone_et.getText().toString().trim();
                companyName = company_name_et.getText().toString().trim();
                jobTitle = job_title_et.getText().toString().trim();


                if (name.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_name));
                    return;
                }

                if (phone.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_phone));
                    return;
                }
                if (companyName.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_company));
                    return;
                }
                if (jobTitle.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_job_title));
                    return;
                }
                updateProfile();

            }
        });

//        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
//        RadioButton maleBtn = (RadioButton) radioSexGroup.findViewById(R.id.radioMale);
//        RadioButton femaleBtn = (RadioButton) radioSexGroup.findViewById(R.id.radioFemale);
//
//
//        if (gender.equalsIgnoreCase("1")) {
//            radioSexGroup.check(R.id.radioMale);
////            maleBtn.setSelected(true);
//        } else {
//            radioSexGroup.check(R.id.radioFemale);
////            femaleBtn.setSelected(true);
//        }
//
//
//        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//
//                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//
//                boolean isChecked = checkedRadioButton.isChecked();
//                // If the radiobutton that has changed in check state is now checked...
//                if (isChecked) {
//                    String checkedText = checkedRadioButton.getText().toString();
//
//                    if (checkedText.equalsIgnoreCase("male")) {
//                        gender = "1";
//                    } else {
//                        gender = "0";
//                    }
//
//                }
//
//            }
//        });
//        selectedId = radioSexGroup.getCheckedRadioButtonId();
//        Log.d("selectedID", selectedId + "");


        String statusBarColor = GlobalClass.getPref("statusBarColor", getApplicationContext());
        String appMainColor = GlobalClass.getPref("appMainColor", getApplicationContext());
        String btnColor = GlobalClass.getPref("btnColor", getApplicationContext());
        String appLogo = GlobalClass.getPref("appLogo", getApplicationContext());


        logo_toolbar = findViewById(R.id.logo_toolbar);
        toolbar_header = findViewById(R.id.toolbar_header);
        bg_container_ln = findViewById(R.id.bg_container_ln);
        camera_circle_orange_img = findViewById(R.id.camera_circle_orange_img);

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
            camera_circle_orange_img.setColorFilter(btnColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);
            bg_container_ln.setBackgroundColor(appMainColor_int);
            update_profile_tv.setBackgroundColor(btnColor_int);
            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(getApplicationContext())
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);

        }

    }


    private void updateProfile() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("name", name);
            mParams.put("contact", phone);
            mParams.put("companyName", companyName);
            mParams.put("jobTitle", jobTitle);
            mParams.put("gender", "1");


            if (imageUploaded == true) {
                mParams.put("profileImage", encodedImage);
            }
            String userID = GlobalClass.getPref("userID", getApplicationContext());
            WebReq.post(getApplicationContext(), "updateUser/" + userID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(EditProfileActivity.this);
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
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
//                        String token = jsonObject.getString("token");
                        String name = jsonObject.getString("name");
                        String profileImage = jsonObject.getString("profileImage");
                        String companyName = jsonObject.getString("companyName");
                        String jobTitle = jsonObject.getString("jobTitle");
                        String contact = jsonObject.getString("contact");

                        GlobalClass.putPref("userID", id, getApplicationContext());
                        GlobalClass.putPref("userEmail", email, getApplicationContext());
//                        GlobalClass.putPref("clientToken", token, getApplicationContext());
                        GlobalClass.putPref("userName", name, getApplicationContext());
                        GlobalClass.putPref("userImage", profileImage, getApplicationContext());
                        GlobalClass.putPref("companyName", companyName, getApplicationContext());
                        GlobalClass.putPref("jobTitle", jobTitle, getApplicationContext());
                        GlobalClass.putPref("contact", contact, getApplicationContext());
                        GlobalClass.putPref("gender", gender, getApplicationContext());

                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();


                        finish();

                    } else {


                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(EditProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                String resultUri = result.getUri().getPath();
                Log.d("resultUriPath", resultUri);
                File file = new File(resultUri);
                try {
                    File compressedImageFile = new Compressor(this).setQuality(30).compressToFile(file);
                    encodedImage = getStringFile(compressedImageFile);
                    Log.d("encodedimage", encodedImage);

                    Picasso.with(getApplicationContext())
                            .load(compressedImageFile).fit().centerCrop()
                            .placeholder(R.drawable.profile_placeholder)
                            .transform(new CircleTransform()).into(userImageView);
                    imageUploaded = true;
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(this, getApplication().getString(R.string.unable_to_process_image), Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {

        try {
            CropImage.activity(imageUri)
                    .start(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getApplication().getString(R.string.unable_to_process_image), Toast.LENGTH_SHORT).show();
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[50240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

}





