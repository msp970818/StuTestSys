package com.example.stutestsys.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stutestsys.R;

/**
 * Created by Ma on 2018/5/30.
 */

public class FourFragment extends Fragment{

    LinearLayout ll_mine_item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourfragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    public void initViews(){
        ll_mine_item = (LinearLayout) getActivity().findViewById(R.id.ll_mine_item);
    }
}
