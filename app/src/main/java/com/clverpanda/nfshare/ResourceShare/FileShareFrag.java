package com.clverpanda.nfshare.ResourceShare;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clverpanda.nfshare.Model.DeviceInfo;
import com.clverpanda.nfshare.Model.TaskInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.Util.Database.DeviceDbHelper;
import com.clverpanda.nfshare.Util.Database.TasksDbHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FileShareFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.test_db_button)
    Button button;


    public FileShareFrag() {
        // Required empty public constructor
    }

    public static FileShareFrag newInstance(String param1, String param2) {
        FileShareFrag fragment = new FileShareFrag();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_file_share, container, false);
        ButterKnife.bind(this, view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
        return view;
    }

}
