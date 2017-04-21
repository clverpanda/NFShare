package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clverpanda.nfshare.fragments.contentshare.AppRecyclerAdapter;
import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.util.AppInfoGetter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

/**
 * Created by clverpanda on 2017/3/31 0031.
 * It's the file for NFShare.
 */

public class LoadAppListAsyncTask extends AsyncTask<Void, Void, List<AppInfo>>
{
    private Context context;
    private RecyclerView recyclerView;
    private Shimmer shimmer;
    private ShimmerTextView shimmerTextView;
    public AsyncResponse<List<AppInfo>> asyncResponse;

    public void setOnAsyncResponse(AsyncResponse<List<AppInfo>> asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }


    public LoadAppListAsyncTask(Context context, RecyclerView recyclerView, Shimmer shimmer, ShimmerTextView shimmerTextView)
    {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
        this.shimmer = shimmer;
        this.shimmerTextView = shimmerTextView;
    }

    @Override
    protected List<AppInfo> doInBackground(Void... params)
    {
        return AppInfoGetter.getInstance(context).getInstalledApps();
    }

    @Override
    protected void onPostExecute(List<AppInfo> result)
    {
        if (result != null) {
            shimmer.cancel();
            shimmerTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new AppRecyclerAdapter(context, result));
            asyncResponse.onDataReceivedSuccess(result);
        }
        else
        {
            asyncResponse.onDataReceivedFailed();
        }
    }


}
