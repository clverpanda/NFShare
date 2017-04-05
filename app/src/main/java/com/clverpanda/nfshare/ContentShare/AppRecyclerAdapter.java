package com.clverpanda.nfshare.ContentShare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.clverpanda.nfshare.Model.AppInfo;
import com.clverpanda.nfshare.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miaol on 2017/3/29 0029.
 */

public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.AppViewHolder>
{
    private List<AppInfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private Map<Integer, Boolean> selectMap = new HashMap<>();


    public AppRecyclerAdapter(Context context, List<AppInfo> datas){
        this.mContext = context;
        this.mDatas = datas;
        initSelectMap();
        inflater = LayoutInflater.from(mContext);
    }

    private void initSelectMap()
    {
        for (int i = 0; i < mDatas.size(); i++)
            selectMap.put(i, false);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position)
    {
        AppInfo theInfo = mDatas.get(position);

        holder.tvAppName.setText(theInfo.getAppName());
        holder.tvAppPkg.setText(theInfo.getPkgName());
        holder.imgAppIcon.setImageDrawable(theInfo.getAppIcon());
        holder.cbxApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectMap.put(holder.getAdapterPosition(), b);
            }
        });
        if (selectMap.get(position) == null)
            selectMap.put(position, false);
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
        @BindView(R.id.app_share_icon)
        ImageView imgAppIcon;
        @BindView(R.id.app_share_checkBox)
        CheckBox cbxApp;

        AppViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public Map<Integer, Boolean> getSelectMap()
    {
        return selectMap;
    }

    public AppInfo getItem(int position)
    {
        return mDatas.get(position);
    }
}
