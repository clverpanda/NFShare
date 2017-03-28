package com.clverpanda.nfshare.ContentShare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.R;


public class AppShareFrag extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;


    public AppShareFrag() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AppShareFrag newInstance(int page) {
        AppShareFrag fragment = new AppShareFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_app_share, container, false);
    }

}
