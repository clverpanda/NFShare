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

import com.clverpanda.nfshare.model.ContactInfo;
import com.clverpanda.nfshare.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clverpanda on 2017/3/31 0031.
 * It's the file for NFShare.
 */

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder>
{
    private List<ContactInfo> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private Map<Integer, Boolean> selectMap = new HashMap<>();


    public ContactRecyclerAdapter(Context context, List<ContactInfo> datas)
    {
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
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position)
    {
        ContactInfo theInfo = mDatas.get(position);

        holder.tvContactName.setText(theInfo.getName());
        holder.tvContactNumber.setText(theInfo.getNumber());
        holder.cbxContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectMap.put(holder.getAdapterPosition(), b);
            }
        });
        if (selectMap.get(position) == null)
            selectMap.put(position, false);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_contact_share, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view);
        return holder;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.contact_share_name)
        TextView tvContactName;
        @BindView(R.id.contact_share_number)
        TextView tvContactNumber;
        @BindView(R.id.contact_share_img)
        ImageView imgContactImg;
        @BindView(R.id.contact_share_checkBox)
        CheckBox cbxContact;

        ContactViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public Map<Integer, Boolean> getSelectMap()
    {
        return selectMap;
    }

    public ContactInfo getItem(int position)
    {
        return mDatas.get(position);
    }
}
