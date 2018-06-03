package com.example.stutestsys.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stutestsys.R;

/**
 * Created by Ma on 2018/5/30.
 */

public class ThreeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contentview_fragment,container,false);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText("聊天");
        return view;
    }
}
