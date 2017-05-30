package com.clverpanda.nfshare.fragments.taskslist;


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

import butterknife.BindView;
import butterknife.ButterKnife;


public class DoneTasksFrag extends Fragment
{

    @BindView(R.id.done_tasks_recyclerView)
    RecyclerView recyclerView;



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

        refreshData();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void refreshData()
    {
        LoadDoneTasksAsyncTask loadDoneTasksAsyncTask = new LoadDoneTasksAsyncTask(getContext(),
                recyclerView);
        loadDoneTasksAsyncTask.execute();
    }

}
