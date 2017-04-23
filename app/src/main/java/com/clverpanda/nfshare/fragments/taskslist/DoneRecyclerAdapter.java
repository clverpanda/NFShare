package com.clverpanda.nfshare.fragments.taskslist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miaol on 2017/4/8 0008.
 */

public class DoneRecyclerAdapter extends RecyclerView.Adapter<DoneRecyclerAdapter.DoneViewHolder>
{
    private List<Task> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public DoneRecyclerAdapter(Context context, List<Task> datas)
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
    public void onBindViewHolder(final DoneViewHolder holder, final int position)
    {
        Task theInfo = mDatas.get(position);

        holder.tvTaskFrom.setText(theInfo.getOriginDevice().getName());
        holder.tvTaskName.setText(theInfo.getName());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        holder.tvTaskTime.setText(df.format(theInfo.getReceiveTime()));
        holder.tvTaskType.setText(theInfo.getType().getName());
        Drawable typeIcon;
        switch (theInfo.getType())
        {
            case PLAIN:
                typeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_plain_text);
                holder.imgTaskType.setImageDrawable(typeIcon);
                break;
            case CONTACT:
                typeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_contact);
                holder.imgTaskType.setImageDrawable(typeIcon);
                break;
            case FILE:
                typeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_file);
                holder.imgTaskType.setImageDrawable(typeIcon);
                break;
            case STREAM:
                typeIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_stream);
                holder.imgTaskType.setImageDrawable(typeIcon);
                break;
            default:
                break;
        }
    }

    @Override
    public DoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_done_tasks, parent, false);
        DoneViewHolder holder = new DoneViewHolder(view);
        return holder;
    }

    class DoneViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.task_type_image)
        ImageView imgTaskType;
        @BindView(R.id.task_name)
        TextView tvTaskName;
        @BindView(R.id.task_from)
        TextView tvTaskFrom;
        @BindView(R.id.task_time)
        TextView tvTaskTime;
        @BindView(R.id.task_type_text)
        TextView tvTaskType;

        DoneViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
