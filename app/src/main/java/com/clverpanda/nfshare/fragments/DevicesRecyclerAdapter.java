package com.clverpanda.nfshare.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.dao.Device;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class DevicesRecyclerAdapter extends RecyclerView.Adapter<DevicesRecyclerAdapter.DevicesViewHolder>
{
    private List<Device> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public DevicesRecyclerAdapter(Context context, List<Device> datas)
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
    public void onBindViewHolder(final DevicesViewHolder holder, final int position)
    {
        Device theInfo = mDatas.get(position);

        holder.tvDeviceName.setText(theInfo.getName());
        holder.tvDeviceMac.setText(theInfo.getWiFiMac());
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_devices, parent, false);
        return new DevicesViewHolder(view);
    }

    class DevicesViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.devices_name)
        TextView tvDeviceName;
        @BindView(R.id.devices_mac)
        TextView tvDeviceMac;

        DevicesViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
