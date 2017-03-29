package com.clverpanda.nfshare.ContentShare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppShareFrag extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    @BindView(R.id.app_recyclerView)
    RecyclerView recyclerView;

    private List<String> mDatas;
    private AppRecyclerAdapter recycleAdapter;


    public AppShareFrag() {
        // Required empty public constructor
    }


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
        View view = inflater.inflate(R.layout.content_app_share, container, false);
        ButterKnife.bind(this, view);

        initData();
        recycleAdapter= new AppRecyclerAdapter(getContext() , mDatas );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter( recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for ( int i=0; i < 40; i++) {
            mDatas.add( "item"+i);
        }
    }

}
