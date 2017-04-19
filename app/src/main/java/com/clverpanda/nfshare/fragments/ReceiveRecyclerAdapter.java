package com.clverpanda.nfshare.fragments;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
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
 * Created by clverpanda on 2017/4/19 0019.
 * It's the file for NFShare.
 */

public class ReceiveRecyclerAdapter extends RecyclerView.Adapter<ReceiveRecyclerAdapter.ReceiveViewHolder>
{
    private List<WifiP2pDevice> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public ReceiveRecyclerAdapter(Context context, List<WifiP2pDevice> datas)
    {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {return mDatas.size();}

    @Override
    public void onBindViewHolder(final ReceiveViewHolder holder, final int position)
    {
        WifiP2pDevice theDevice = mDatas.get(position);

        holder.tvDeviceName.setText(theDevice.deviceName);
        holder.tvDeviceMac.setText(theDevice.deviceAddress);
    }

    @Override
    public ReceiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_devices, parent, false);
        return new ReceiveViewHolder(view);
    }



    class ReceiveViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.devices_name)
        TextView tvDeviceName;
        @BindView(R.id.devices_mac)
        TextView tvDeviceMac;

        ReceiveViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
