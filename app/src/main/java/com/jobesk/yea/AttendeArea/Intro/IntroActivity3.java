package com.jobesk.yea.AttendeArea.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jobesk.yea.AttendeArea.Activities.LoginActivity;
import com.jobesk.yea.R;

import me.relex.circleindicator.CircleIndicator;

public class IntroActivity3 extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator indicator;
    private RelativeLayout rootViewLayout;
    private LinearLayout next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_3);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        rootViewLayout = findViewById(R.id.rootViewLayout);


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intenNext = new Intent(IntroActivity3.this, LoginActivity.class);
                intenNext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intenNext);

            }
        });


    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:

                    return FragIntro1.newInstance("FirstFragment, Instance 1");
                case 1:

                    return FragIntro1.newInstance("SecondFragment, Instance 1");
                case 2:

                    return FragIntro1.newInstance("ThirdFragment, Instance 1");

                default:

                    return FragIntro1.newInstance("ThirdFragment, Default");
            }
        }


        @Override
        public int getCount() {
            return 3;
        }
    }

}
