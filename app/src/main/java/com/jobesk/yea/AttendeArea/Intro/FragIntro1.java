package com.jobesk.yea.AttendeArea.Intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobesk.yea.R;

public class FragIntro1 extends Fragment {


    public static FragIntro1 newInstance(String text) {

        FragIntro1 f = new FragIntro1();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.intro_layout_1, container, false);

        return rootView;
    }
}
