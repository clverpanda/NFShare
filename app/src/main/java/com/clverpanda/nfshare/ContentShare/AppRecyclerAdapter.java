package com.clverpanda.nfshare.ContentShare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clverpanda.nfshare.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miaol on 2017/3/29 0029.
 */

public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.AppViewHolder>
{
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public AppRecyclerAdapter(Context context, List<String> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {

        holder.tvAppName.setText(mDatas.get(position));
        holder.tvAppPkg.setText(mDatas.get(position));
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_app_share, parent, false);
        AppViewHolder holder= new AppViewHolder(view);
        return holder;
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.app_share_name)
        TextView tvAppName;
        @BindView(R.id.app_share_pkg)
        TextView tvAppPkg;

        AppViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
