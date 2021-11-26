package com.jobesk.yea.AttendeArea.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jobesk.yea.R;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgetPassActivity extends AppCompatActivity {

  private ImageView back_img;
  private EditText email_et;

  private LinearLayout next_btn;
  private String email;
  private String TAG = "ForgetPassActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forget_pass);


    back_img = findViewById(R.id.back_img);
    back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });


    email_et = findViewById(R.id.email_et);

    next_btn = findViewById(R.id.next_btn);
    next_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        email = email_et.getText().toString().trim();


        if (email.equalsIgnoreCase("")) {
          GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_email));
          return;
        }
        if (GlobalClass.emailValidator(email) == false) {
          GlobalClass.showToast(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_valid_email));
          return;
        }


        getpass();


      }
    });

  }


  private void getpass() {

    if (GlobalClass.isOnline(getApplicationContext()) == true) {

      RequestParams mParams = new RequestParams();
      mParams.put("email", email);

      WebReq.post(getApplicationContext(), "forgotPassword", mParams, new MyTextHttpResponseHandler());

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
      GlobalClass.showLoading(ForgetPassActivity.this);
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


          if (status.equalsIgnoreCase("1")){


            String statusMessage = mResponse.getString("statusMessage");
            Toast.makeText(ForgetPassActivity.this, statusMessage, Toast.LENGTH_SHORT).show();
            finish();
          }else {

            String statusMessage = mResponse.getString("statusMessage");
            Toast.makeText(ForgetPassActivity.this, statusMessage, Toast.LENGTH_SHORT).show();

          }


        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    }
  }
}
