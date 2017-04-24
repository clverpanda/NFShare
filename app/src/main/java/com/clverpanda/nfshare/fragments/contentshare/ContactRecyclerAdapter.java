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

import java.util.ArrayList;
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
    private List<ContactInfo> selectedItems = new ArrayList<>();


    public ContactRecyclerAdapter(Context context, List<ContactInfo> datas)
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
    public void onBindViewHolder(final ContactViewHolder holder, final int position)
    {
        ContactInfo theInfo = mDatas.get(position);

        holder.tvContactName.setText(theInfo.getName());
        holder.tvContactNumber.setText(theInfo.getNumber());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_contact_share, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view);
        return holder;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener
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

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
            {
                selectedItems.add(mDatas.get(getLayoutPosition()));
            }
            else
            {
                selectedItems.remove(mDatas.get(getLayoutPosition()));
            }
        }
    }

    public List<ContactInfo> getSelectedItems() { return selectedItems; }
}
