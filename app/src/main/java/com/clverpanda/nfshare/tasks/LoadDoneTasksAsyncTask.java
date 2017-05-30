package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.dao.TaskDao;
import com.clverpanda.nfshare.fragments.taskslist.DoneRecyclerAdapter;
import com.clverpanda.nfshare.model.TaskStatus;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

/**
 * Created by miaol on 2017/4/8 0008.
 */

public class LoadDoneTasksAsyncTask extends AsyncTask<Void, Void, List<Task>>
{
    private Context context;
    private RecyclerView recyclerView;


    public LoadDoneTasksAsyncTask(Context context, RecyclerView recyclerView)
    {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<Task> doInBackground(Void... params)
    {
        DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
        return daoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.Status.eq(TaskStatus.DONE.getIndex()))
                .orderDesc(TaskDao.Properties.Id)
                .list();
    }

    @Override
    protected void onPostExecute(List<Task> result)
    {
        if (result != null)
            recyclerView.setAdapter(new DoneRecyclerAdapter(context, result));
    }
}
