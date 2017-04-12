package com.clverpanda.nfshare.contentshare;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.model.AppInfoTransfer;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.tasks.AsyncResponse;
import com.clverpanda.nfshare.tasks.LoadAppListAsyncTask;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            appListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            recyclerView.setAdapter(new AppRecyclerAdapter(getContext(), mData));
            shimmerTextView.setVisibility(View.GONE);
        }
        return view;
    }

    public List<AppInfoTransfer> getSelectedItems()
    {
        List<AppInfoTransfer> result = new ArrayList<>();
        AppRecyclerAdapter dataAdapter = (AppRecyclerAdapter) recyclerView.getAdapter();
        Map<Integer, Boolean> selectMap = dataAdapter.getSelectMap();
        for (Map.Entry<Integer, Boolean> entry : selectMap.entrySet())
        {
            if (entry.getValue())
            {
                AppInfo appInfo = dataAdapter.getItem(entry.getKey());
                AppInfoTransfer appInfoTransfer = new AppInfoTransfer(appInfo.getAppName(),
                        appInfo.getPkgName(), appInfo.getAppVersion());
                result.add(appInfoTransfer);
            }
        }
        return result;
    }



}
