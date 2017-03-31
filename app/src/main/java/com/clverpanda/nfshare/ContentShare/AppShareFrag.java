package com.clverpanda.nfshare.ContentShare;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.Model.AppInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.Util.AppInfoGetter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppShareFrag extends Fragment
{
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final int APP_LIST_LOADED = 1;
    private int mPage;


    @BindView(R.id.app_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_loading)
    ShimmerTextView shimmerTextView;

    private List<AppInfo> mDatas;
    private AppRecyclerAdapter recycleAdapter;
    private Shimmer shimmer;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //载入动画
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

        loadAppList();
        return view;
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case APP_LIST_LOADED:
                    shimmer.cancel();
                    shimmerTextView.setVisibility(View.GONE);
                    recycleAdapter= new AppRecyclerAdapter(getContext(), mDatas);
                    recyclerView.setAdapter(recycleAdapter);
                    break;
            }
        }
    };
    private void loadAppList()
    {
        Thread thread = new Thread() {
            @Override
            public void run()
            {
                initData();
                Message msg = handler.obtainMessage();
                msg.what = APP_LIST_LOADED;
                msg.obj = mDatas;
                handler.sendMessage(msg);
            }
        };
        thread.start();
    }

    private void initData()
    {
        mDatas = AppInfoGetter.getInstance(getContext()).getInstalledApps();
    }

}
