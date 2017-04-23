package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Device;
import com.clverpanda.nfshare.fragments.DevicesRecyclerAdapter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class LoadDeviceListAsyncTask extends AsyncTask<Void, Void, List<Device>>
{
    private Context context;
    private RecyclerView recyclerView;
    private Shimmer shimmer;
    private ShimmerTextView shimmerTextView;

    public LoadDeviceListAsyncTask(Context context, RecyclerView recyclerView, Shimmer shimmer, ShimmerTextView shimmerTextView)
    {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
        this.shimmer = shimmer;
        this.shimmerTextView = shimmerTextView;
    }

    @Override
    protected List<Device> doInBackground(Void... params)
    {
        DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
        return daoSession.getDeviceDao().loadAll();
    }

    @Override
    protected void onPostExecute(List<Device> result)
    {
        if (result != null)
        {
            shimmer.cancel();
            shimmerTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new DevicesRecyclerAdapter(context, result));
        }
    }
}
