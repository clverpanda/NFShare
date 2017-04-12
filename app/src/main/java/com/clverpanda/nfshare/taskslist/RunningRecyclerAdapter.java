package com.clverpanda.nfshare.taskslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.TaskInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.service.DownloadService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miaol on 2017/4/8 0008.
 */

public class RunningRecyclerAdapter extends RecyclerView.Adapter<RunningRecyclerAdapter.RunningViewHolder>
{
    private List<TaskInfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public RunningRecyclerAdapter(Context context, List<TaskInfo> datas)
    {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final RunningViewHolder holder, final int position)
    {
        final TaskInfo theInfo = mDatas.get(position);

        holder.tvTaskFrom.setText(theInfo.getFrom());
        holder.tvTaskName.setText(theInfo.getName());
        holder.tvTaskType.setText(DataType.getName(theInfo.getType()));
        int pro = (int) theInfo.getFinish();
        holder.pbDownload.setProgress(pro);
        if (theInfo.getType() == DataType.APP.getIndex())
        {
            AppInfo appInfo = JSON.parseObject(theInfo.getDescription(), AppInfo.class);
            final String downloadUrl = "http://www.wandoujia.com/apps/" + appInfo.getPkgName() + "/download";
            final String fileName = appInfo.getAppName() + ".apk";
            holder.imgbStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);
                    DownloadFileInfo fileInfo = new DownloadFileInfo(theInfo.getId(), downloadUrl,
                            fileName, 0, 0);
                    intent.putExtra("fileinfo", fileInfo);
                    mContext.startService(intent);
                }
            });
            holder.imgbPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_PAUSE);
                    DownloadFileInfo fileInfo = new DownloadFileInfo(theInfo.getId(), downloadUrl,
                            fileName, 0, 0);
                    intent.putExtra("fileinfo", fileInfo);
                    mContext.startService(intent);
                }
            });
        }
    }

    @Override
    public RunningViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_running_tasks, parent, false);
        return new RunningViewHolder(view);
    }

    class RunningViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.task_name)
        TextView tvTaskName;
        @BindView(R.id.task_from)
        TextView tvTaskFrom;
        @BindView(R.id.task_type_text)
        TextView tvTaskType;
        @BindView(R.id.progressBar)
        ProgressBar pbDownload;
        @BindView(R.id.btn_start)
        ImageView imgbStart;
        @BindView(R.id.btn_pause)
        ImageView imgbPause;


        RunningViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void updateProgress(int id, long progress)
    {
        for (TaskInfo taskItem : mDatas)
        {
            if (taskItem.getId() == id)
            {
                taskItem.setFinish(progress);
                notifyDataSetChanged();
            }
        }
    }
}
