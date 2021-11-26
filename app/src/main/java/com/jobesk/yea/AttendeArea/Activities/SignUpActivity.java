package com.jobesk.yea.AttendeArea.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.yea.R;

public class SignUpActivity extends AppCompatActivity {
    private TextView login_tv;
    private LinearLayout signUp_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        login_tv = findViewById(R.id.login_tv);
        login_tv.setPaintFlags(login_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        signUp_tv = findViewById(R.id.signUp_tv);
        signUp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, DrawerActivity.class);

                startActivity(i);
            }
        });
    }
}
