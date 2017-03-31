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
import android.widget.Toast;

import com.clverpanda.nfshare.Model.AppInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.Tasks.AsyncResponse;
import com.clverpanda.nfshare.Tasks.LoadAppListAsyncTask;
import com.clverpanda.nfshare.Util.AppInfoGetter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppShareFrag extends Fragment
{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static boolean IsFirst = true;

    private static List<AppInfo> mData;


    private Shimmer shimmer;
    @BindView(R.id.app_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_loading)
    ShimmerTextView shimmerTextView;


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

        if (IsFirst)
        {
            //载入动画
            shimmer = new Shimmer();
            shimmer.start(shimmerTextView);

            LoadAppListAsyncTask appListAsyncTask = new LoadAppListAsyncTask(getContext(),
                    recyclerView, shimmer, shimmerTextView);
            appListAsyncTask.setOnAsyncResponse(new AsyncResponse<List<AppInfo>>() {
                @Override
                public void onDataReceivedSuccess(List<AppInfo> listData) {
                    mData = listData;
                    IsFirst = false;
                }

                @Override
                public void onDataReceivedFailed() {
                    mData = null;
                    Toast.makeText(getContext(), "获取APP数据失败！", Toast.LENGTH_SHORT).show();
                }
            });
            appListAsyncTask.execute();
        }
        else
        {
            recyclerView.setAdapter(new AppRecyclerAdapter(getContext(), mData));
            shimmerTextView.setVisibility(View.GONE);
        }
        return view;
    }



}
