package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clverpanda.nfshare.fragments.contentshare.ContactRecyclerAdapter;
import com.clverpanda.nfshare.model.ContactInfo;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class LoadContactListAsyncTask extends AsyncTask<Void, Void, List<ContactInfo>>
{
    private Context context;
    private RecyclerView recyclerView;
    private Shimmer shimmer;
    private ShimmerTextView shimmerTextView;
    public AsyncResponse<List<ContactInfo>> asyncResponse;

    public void setOnAsyncResponse(AsyncResponse<List<ContactInfo>> asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }

    public LoadContactListAsyncTask(Context context, RecyclerView recyclerView, Shimmer shimmer, ShimmerTextView shimmerTextView)
    {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
        this.shimmer = shimmer;
        this.shimmerTextView = shimmerTextView;
    }

    @Override
    protected List<ContactInfo> doInBackground(Void... params)
    {
        List<ContactInfo> Result = new ArrayList<>();
        Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(contactUri,
                new String[]{"display_name", "sort_key", "contact_id","data1"},
                null, null, "sort_key");
        try
        {
            String contactName;
            String contactNumber;

            while (cursor.moveToNext())
            {
                ContactInfo contactInfo = new ContactInfo();
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactInfo.setName(contactName);
                contactInfo.setNumber(contactNumber);
                Result.add(contactInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return Result;
    }

    @Override
    protected void onPostExecute(List<ContactInfo> result)
    {
        if (result != null) {
            shimmer.cancel();
            shimmerTextView.setVisibility(View.GONE);
            recyclerView.setAdapter(new ContactRecyclerAdapter(context, result));
            asyncResponse.onDataReceivedSuccess(result);
        }
        else
        {
            asyncResponse.onDataReceivedFailed();
        }
    }
}
