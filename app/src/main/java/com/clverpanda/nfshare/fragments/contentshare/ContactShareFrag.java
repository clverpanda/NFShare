package com.clverpanda.nfshare.fragments.contentshare;


import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.clverpanda.nfshare.model.ContactInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.tasks.AsyncResponse;
import com.clverpanda.nfshare.tasks.LoadContactListAsyncTask;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
                             Bundle savedInstanceState)
    {
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

            ContactShareFragPermissionsDispatcher.startAsyncContactLoadWithCheck(this);
            IsFirst = false;
        }
        else
        {
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


    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    protected void startAsyncContactLoad()
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactShareFragPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public List<ContactInfo> getSelectedItems()
    {
        ContactRecyclerAdapter dataAdapter = (ContactRecyclerAdapter) recyclerView.getAdapter();
        return dataAdapter.getSelectedItems();
    }

}
