package com.jobesk.yea.AttendeArea.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jobesk.yea.AttendeArea.Models.SpinnerTitleModel;
import com.jobesk.yea.R;

import java.util.ArrayList;

public class AdapterSpinnerTitles extends BaseAdapter implements SpinnerAdapter {

    ArrayList<SpinnerTitleModel> list;

    Activity activity;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public AdapterSpinnerTitles(Activity activity, ArrayList<SpinnerTitleModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(activity, R.layout.spinner_layout_title, null);

        TextView textView = (TextView) view.findViewById(R.id.userName_tv);

        String value = list.get(0).getSelectedText().toString();
        textView.setText(value);

        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view = View.inflate(activity, R.layout.spinner_layout_titles, null);
        TextView textView = (TextView) view.findViewById(R.id.userName_tv);
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);


        if (position == 0) {
            checkbox.setVisibility(View.GONE);
            String value = list.get(0).getName().toString();
            textView.setText(value);

//            for (int i = 0; i < list.size(); i++) {
//
//                if (list.get(i).getChecked().equalsIgnoreCase("1")) {
//
//                    String namesTiltles = list.get(i).getName();
//                    textView.append(namesTiltles + ",");
//
//                }
//
//
//            }


        } else {

            if (list.get(position).getChecked().equalsIgnoreCase("1")) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }

            textView.setText(list.get(position).getName());

        }


        checkbox.setTag(position);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int pos = (int) buttonView.getTag();

                if (isChecked == true) {
                    list.get(pos).setChecked("1");


                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).getChecked().equalsIgnoreCase("1")) {
                            String namesTiltles = list.get(i).getName();
                            stringBuilder.append(namesTiltles).append(",");

                        }

                    }

                    list.get(0).setSelectedText(stringBuilder.toString());
                    notifyDataSetChanged();


                } else {
                    list.get(pos).setChecked("0");
                }

            }
        });


        return view;
    }


}