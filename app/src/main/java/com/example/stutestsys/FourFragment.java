package com.example.stutestsys;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ma on 2018/5/30.
 */

public class FourFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contentview_fragment,container,false);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText("我的");
        return view;
    }
}
