package com.clverpanda.nfshare.ContentShare;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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

import com.clverpanda.nfshare.MainActivity;
import com.clverpanda.nfshare.Model.ContactInfo;
import com.clverpanda.nfshare.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactShareFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean IsFirst = true;
    private static final int REQUEST_CODE_ASK_CONTACT = 123;

    private String mParam1;
    private String mParam2;
    private List<ContactInfo> mData;

    @BindView(R.id.no_permission)
    TextView textView;
    @BindView(R.id.contact_recyclerView)
    RecyclerView recyclerView;


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
        if (IsFirst) {
            mData = new ArrayList<>();
            initialData();
            textView.setVisibility(View.GONE);
            IsFirst = false;
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
            {
                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_CONTACT);
            }
            else
            {
                getData();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                recyclerView.setAdapter(new ContactRecyclerAdapter(getContext(), mData));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }
    }

    private void getData()
    {
        Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(contactUri,
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
                mData.add(contactInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CONTACT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    textView.setText("已获得权限！");
                }
                else
                {
                    textView.setText("申请权限失败！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
