package com.clverpanda.nfshare.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clverpanda.nfshare.Model.TaskInfo;
import com.clverpanda.nfshare.TasksList.DoneRecyclerAdapter;
import com.clverpanda.nfshare.Util.Database.TasksDbHelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

/**
 * Created by miaol on 2017/4/8 0008.
 */

public class LoadDoneTasksAsyncTask extends AsyncTask<Void, Void, List<TaskInfo>>
{
    private Context context;
    private RecyclerView recyclerView;
    private Shimmer shimmer;
    private ShimmerTextView shimmerTextView;

    public LoadDoneTasksAsyncTask(Context context, RecyclerView recyclerView, Shimmer shimmer, ShimmerTextView shimmerTextView)
    {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
        this.shimmer = shimmer;
        this.shimmerTextView = shimmerTextView;
    }

    @Override
    protected List<TaskInfo> doInBackground(Void... params)
    {
        TasksDbHelper tasksDb = new TasksDbHelper(context);
        return tasksDb.getAllDoneTaskInfo();
    }

    @Override
    protected void onPostExecute(List<TaskInfo> result)
    {
        if (result != null)
        {
            shimmer.cancel();
            shimmerTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new DoneRecyclerAdapter(context, result));
        }
    }
}
