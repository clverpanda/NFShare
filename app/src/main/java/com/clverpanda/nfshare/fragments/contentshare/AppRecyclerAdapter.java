package com.clverpanda.nfshare.fragments.contentshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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
    private static final int MAX_SELECT_COUNT = 5;

    private List<AppInfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private List<AppInfo> selectedItems = new ArrayList<>();


    public AppRecyclerAdapter(Context context, List<AppInfo> datas){
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
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

    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_app_share, parent, false);
        AppViewHolder holder= new AppViewHolder(view);
        return holder;
    }

    class AppViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener
    {

        @BindView(R.id.app_share_name)
        TextView tvAppName;
        @BindView(R.id.app_share_pkg)
        TextView tvAppPkg;
        @BindView(R.id.app_share_icon)
        ImageView imgAppIcon;
        @BindView(R.id.app_share_checkBox)
        CheckBox cbxApp;

        AppViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            cbxApp.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            if (b)
            {
                if (selectedItems.size() < MAX_SELECT_COUNT) {
                    selectedItems.add(mDatas.get(getLayoutPosition()));
                }
                else {
                    compoundButton.setChecked(false);
                    Toast.makeText(mContext, "已经到达可选最大值", Toast.LENGTH_SHORT).show();
                }
            }
            else
                selectedItems.remove(mDatas.get(getLayoutPosition()));
        }
    }

    public List<AppInfo> getSelectedItems()
    {
        return selectedItems;
    }
}
