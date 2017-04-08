package com.clverpanda.nfshare.ContentShare;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.clverpanda.nfshare.Model.AppInfo;
import com.clverpanda.nfshare.Model.AppInfoTransfer;
import com.clverpanda.nfshare.Model.ContactInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.Tasks.AsyncResponse;
import com.clverpanda.nfshare.Tasks.LoadContactListAsyncTask;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactShareFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean IsFirst = true;
    private static final int REQUEST_CODE_ASK_CONTACT = 123;

    private String mParam1;
    private String mParam2;

    private static List<ContactInfo> mData;
    private Shimmer shimmer;


    @BindView(R.id.no_permission)
    TextView textView;
    @BindView(R.id.contact_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_loading)
    ShimmerTextView shimmerTextView;


    public ContactShareFrag() {
        // Required empty public constructor
    }


    public static ContactShareFrag newInstance(String param1, String param2) {
        ContactShareFrag fragment = new ContactShareFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_contact_share, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (IsFirst)
        {
            //载入动画
            shimmer = new Shimmer();
            shimmer.start(shimmerTextView);

            initialData();
            IsFirst = false;
        }
        else {
            recyclerView.setAdapter(new ContactRecyclerAdapter(getContext(), mData));
            shimmerTextView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    private void initialData()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_CONTACT);
            else
                startAsyncContactLoad();
        }
        else
            startAsyncContactLoad();
    }

    private void startAsyncContactLoad()
    {
        LoadContactListAsyncTask contactListAsyncTask = new LoadContactListAsyncTask(getContext(),
                recyclerView, shimmer, shimmerTextView);
        contactListAsyncTask.setOnAsyncResponse(new AsyncResponse<List<ContactInfo>>() {
            @Override
            public void onDataReceivedSuccess(List<ContactInfo> listData) {
                mData = listData;
                IsFirst = false;
            }

            @Override
            public void onDataReceivedFailed() {
                mData = null;
                Toast.makeText(getContext(), "获取通讯录数据失败！", Toast.LENGTH_SHORT).show();
            }
        });
        contactListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CONTACT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startAsyncContactLoad();
                else
                    textView.setVisibility(View.VISIBLE);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public List<ContactInfo> getSelectedItems()
    {
        List<ContactInfo> result = new ArrayList<>();
        ContactRecyclerAdapter dataAdapter = (ContactRecyclerAdapter) recyclerView.getAdapter();
        Map<Integer, Boolean> selectMap = dataAdapter.getSelectMap();
        for (Map.Entry<Integer, Boolean> entry : selectMap.entrySet())
        {
            if (entry.getValue())
            {
                result.add(dataAdapter.getItem(entry.getKey()));
            }
        }
        return result;
    }

}
