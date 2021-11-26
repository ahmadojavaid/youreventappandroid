package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jobesk.yea.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<String> dataArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, del_img;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            del_img = (ImageView) view.findViewById(R.id.del_img);

        }
    }


    public MultiImageAdapter(Activity activity, ArrayList<String> dataArrayList) {
        this.dataArrayList = dataArrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        Movie movie = moviesList.get(position);
//        holder.imageView.setText(movie.getTitle());

        String imagePath = dataArrayList.get(position);


        File imageFile = new File(imagePath);

        Picasso.with(activity)
                .load(imageFile)
                .fit()
                .centerCrop()
                .into(holder.imageView);


        holder.del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dataArrayList.remove(position);

                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
}