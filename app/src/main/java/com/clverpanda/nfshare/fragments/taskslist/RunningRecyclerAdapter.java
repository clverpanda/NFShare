package com.clverpanda.nfshare.fragments.taskslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        if (theInfo.getStatus() == 0)//暂停中的任务
        {
            holder.imgbPause.setImageResource(R.drawable.ic_pause_clicked);
            holder.imgbStart.setImageResource(R.drawable.ic_start);
        }
        else if (theInfo.getStatus() == 2)//进行中的任务
        {
            holder.imgbStart.setImageResource(R.drawable.ic_start_clicked);
            holder.imgbPause.setImageResource(R.drawable.ic_pause);
        }
        else if (theInfo.getStatus() == -1)//失败的任务
        {
            holder.tvTaskFailed.setVisibility(View.VISIBLE);
            holder.imgbStart.setVisibility(View.INVISIBLE);
            holder.imgbPause.setVisibility(View.INVISIBLE);
        }
        if (theInfo.getType() == DataType.APP.getIndex())
        {
            AppInfo appInfo = JSON.parseObject(theInfo.getDescription(), AppInfo.class);
            final String downloadUrl = "http://www.wandoujia.com/apps/" + appInfo.getPkgName() + "/download";
            final String fileName = appInfo.getAppName() + ".apk";
            holder.imgbStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (theInfo.getStatus() == 0)
                    {
                        Intent intent = new Intent(mContext, DownloadService.class);
                        intent.setAction(DownloadService.ACTION_START);
                        DownloadFileInfo fileInfo = new DownloadFileInfo(theInfo.getId(), downloadUrl,
                                fileName, 0, 0);
                        intent.putExtra("fileinfo", fileInfo);
                        mContext.startService(intent);
                    }
                }
            });
            holder.imgbPause.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (theInfo.getStatus() == 2) {
                        Intent intent = new Intent(mContext, DownloadService.class);
                        intent.setAction(DownloadService.ACTION_PAUSE);
                        DownloadFileInfo fileInfo = new DownloadFileInfo(theInfo.getId(), downloadUrl,
                                fileName, 0, 0);
                        intent.putExtra("fileinfo", fileInfo);
                        mContext.startService(intent);
                    }
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
        ImageButton imgbStart;
        @BindView(R.id.btn_pause)
        ImageButton imgbPause;
        @BindView(R.id.task_failed)
        TextView tvTaskFailed;


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

    public void removeTask(int id)
    {
        int i = 0;
        for (TaskInfo taskItem : mDatas)
        {
            if (taskItem.getId() == id)
            {
                mDatas.remove(i);
                notifyItemRemoved(i);
            }
            i++;
        }
    }

    public void setStarted(int id)
    {
        for (TaskInfo taskItem : mDatas)
        {
            if (taskItem.getId() == id)
            {
                taskItem.setStatus(2);
                notifyDataSetChanged();
            }
        }
    }

    public void setPaused(int id)
    {
        for (TaskInfo taskItem : mDatas)
        {
            if (taskItem.getId() == id)
            {
                taskItem.setStatus(0);
                notifyDataSetChanged();
            }
        }
    }

    public void setFailed(int id)
    {
        for (TaskInfo taskItem : mDatas)
        {
            if (taskItem.getId() == id)
            {
                taskItem.setStatus(-1);
                notifyDataSetChanged();
            }
        }
    }

    public void refreshData(int id)
    {
        notifyDataSetChanged();
    }

}
