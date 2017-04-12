package com.clverpanda.nfshare.TasksList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clverpanda.nfshare.Model.DataType;
import com.clverpanda.nfshare.Model.TaskInfo;
import com.clverpanda.nfshare.R;

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
        TaskInfo theInfo = mDatas.get(position);

        holder.tvTaskFrom.setText(theInfo.getFrom());
        holder.tvTaskName.setText(theInfo.getName());
        holder.tvTaskType.setText(DataType.getName(theInfo.getType()));
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
}
