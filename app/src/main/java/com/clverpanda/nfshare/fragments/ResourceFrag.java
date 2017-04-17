package com.clverpanda.nfshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.clverpanda.nfshare.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResourceFrag extends Fragment {

    private static final int CHOOSE_IMAGE_CODE = 1024;
    private static final int CHOOSE_VIDEO_CODE = 2048;
    private static final int CHOOSE_FILE_CODE = 4096;


    @BindView(R.id.resource_toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_btn)
    TextView btnShareImage;
    @BindView(R.id.video_btn)
    TextView btnShareVideo;
    @BindView(R.id.file_btn)
    TextView btnShareFile;


    public ResourceFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                parentActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        toolbar.setTitle(R.string.drawer_item_resource);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @OnClick(R.id.image_btn)
    void imageChooseClicked()
    {
        BoxingConfig singleImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        Boxing.of(singleImgConfig).withIntent(getContext(), BoxingActivity.class).start(this, CHOOSE_IMAGE_CODE);
    }

}
