package com.jobesk.yea.AttendeArea.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.jobesk.yea.AttendeArea.Adapter.ImagePagerAdapterSlider;
import com.jobesk.yea.R;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import java.util.ArrayList;

public class ImageViewMultiActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<String> myList;
    private String selectedPos;
    private ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_multi);


        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myList = (ArrayList<String>) getIntent().getSerializableExtra("imageList");


        selectedPos = getIntent().getStringExtra("selectedPos");
        SetupViewPaer();
    }

    private void SetupViewPaer() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ImagePagerAdapterSlider(getApplicationContext(), myList));
        viewPager.setCurrentItem(Integer.valueOf(selectedPos));

        ExtensiblePageIndicator extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
