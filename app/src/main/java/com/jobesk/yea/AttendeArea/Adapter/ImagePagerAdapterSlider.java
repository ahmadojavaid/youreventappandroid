package com.jobesk.yea.AttendeArea.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.github.chrisbanes.photoview.PhotoView;
import com.jobesk.yea.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


/**
 * Created by DASTAAN on 11/10/2017.
 */

public class ImagePagerAdapterSlider extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> imageArray;
    PhotoView photoView;
    // ImageView imageView;


    public ImagePagerAdapterSlider(Context context, ArrayList<String> imageArray) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageArray = imageArray;
    }

    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item_slider, container, false);

        //imageView = (ImageView) itemView.findViewById(R.id.imageView);

//        Picasso.with(mContext).load(imageArray.get(position)).resize(400,400).into(imageView);


        photoView = (PhotoView) itemView.findViewById(R.id.imageView);

//        RequestOptions options = new RequestOptions();
//        options.centerCrop();
//
//        Glide.with(mContext).load(imageArray.get(position))
//                .thumbnail(Glide.with(mContext)
//                        .load(R.drawable.cover_placeholder)).apply(options)
//                .into(photoView);

        String imageValue = imageArray.get(position);


//        if (imageValue.contains("600x600")) {
//            Picasso.with(mContext).load(imageValue).resize(190, 250).into(photoView);
//        } else {
//
//        }
        Picasso.with(mContext).load(imageValue).into(photoView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}