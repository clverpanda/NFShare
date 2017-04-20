package com.clverpanda.nfshare.taskslist;


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
import com.clverpanda.nfshare.tasks.LoadDoneTasksAsyncTask;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DoneTasksFrag extends Fragment
{

    @BindView(R.id.done_tasks_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_loading)
    ShimmerTextView shimmerTextView;

    private Shimmer shimmer;


    public DoneTasksFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_done_tasks, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //载入动画
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        LoadDoneTasksAsyncTask loadDoneTasksAsyncTask = new LoadDoneTasksAsyncTask(getContext(),
                recyclerView, shimmer, shimmerTextView);
        loadDoneTasksAsyncTask.execute();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
