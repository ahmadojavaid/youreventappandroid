package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.DrawerActivitySponsor;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {


    private TextView signUp_tv, froget_pass_tv;
    private EditText email_et, pass_et;
    private LinearLayout signIn_tv;
    private String TAG = "LoginActivity";
    private String email, password;
    private String firebaseToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        froget_pass_tv = findViewById(R.id.froget_pass_tv);
        froget_pass_tv.setPaintFlags(froget_pass_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        froget_pass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);

            }
        });




        email_et = findViewById(R.id.email_et);
        pass_et = findViewById(R.id.pass_et);


        signIn_tv = findViewById(R.id.signIn_tv);
        signIn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = email_et.getText().toString().trim();
                password = pass_et.getText().toString().trim();

                if (email.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_email));
                    return;
                }
                if (GlobalClass.emailValidator(email) == false) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_valid_email));
                    return;
                }
                if (password.equalsIgnoreCase("")) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_password));
                    return;
                }
                if (password.length() < 6) {
                    GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.pass_length));
                    return;
                }


                signIn();


            }
        });

        signUp_tv = findViewById(R.id.signUp_tv);
        signUp_tv.setPaintFlags(signUp_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);

                startActivity(i);
            }
        });

    }


    private void signIn() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("password", password);
            firebaseToken = GlobalClass.getToken();
            mParams.put("deviceId", firebaseToken);
            mParams.put("deviceType", "2");

            WebReq.post(getApplicationContext(), "doLogin", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(LoginActivity.this);
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
                        String gender = jsonObject.getString("gender");
                        String followingCount = jsonObject.getString("followingCount");
                        String role = jsonObject.getString("role");


                        JSONObject schemesObject = mResponse.getJSONObject("schemes");
                        String statusBarColor = schemesObject.getString("statusColor");
                        String appMainColor = schemesObject.getString("appColor");
                        String btnColor = schemesObject.getString("btnColor");
                        String appLogo = schemesObject.getString("appLogo");

                        GlobalClass.putPref("statusBarColor", statusBarColor, getApplicationContext());
                        GlobalClass.putPref("appMainColor", appMainColor, getApplicationContext());
                        GlobalClass.putPref("btnColor", btnColor, getApplicationContext());
                        GlobalClass.putPref("appLogo", appLogo, getApplicationContext());
                        GlobalClass.putPref("role", role, getApplicationContext());


                        GlobalClass.putPref("userID", id, getApplicationContext());
                        GlobalClass.putPref("userEmail", email, getApplicationContext());
                        GlobalClass.putPref("clientToken", firebaseToken, getApplicationContext());
                        GlobalClass.putPref("userName", name, getApplicationContext());
                        GlobalClass.putPref("userImage", profileImage, getApplicationContext());
                        GlobalClass.putPref("companyName", companyName, getApplicationContext());
                        GlobalClass.putPref("jobTitle", jobTitle, getApplicationContext());
                        GlobalClass.putPref("contact", contact, getApplicationContext());
                        GlobalClass.putPref("gender", gender, getApplicationContext());
                        GlobalClass.putPref("followingCount", followingCount, getApplicationContext());



                        //Role
                        // 1 is for sponsor
                        // 2 is for the attendee

                        if (role.equalsIgnoreCase("1")) {
                            Intent i = new Intent(getApplicationContext(), DrawerActivitySponsor.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(), DrawerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }




                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
