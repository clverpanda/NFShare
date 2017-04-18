package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bilibili.boxing_impl.ui.BoxingBottomSheetActivity;
import com.clverpanda.nfshare.NFCSendActivity;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.contentshare.AppShareFrag;
import com.clverpanda.nfshare.model.AppInfoTransfer;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.FileInfo;
import com.clverpanda.nfshare.model.NFCTransferData;
import com.clverpanda.nfshare.model.WIFITransferData;
import com.clverpanda.nfshare.util.DeviceInfoGetter;
import com.clverpanda.nfshare.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class ResourceFrag extends Fragment {

    private static final int CHOOSE_IMAGE_CODE = 1024;
    private static final int CHOOSE_VIDEO_CODE = 2048;
    private static final int CHOOSE_FILE_CODE = 4096;

    private String mSelectedFilePath = null;
    private DataType mSelectedFileType = null;


    @BindView(R.id.resource_toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_btn)
    TextView btnShareImage;
    @BindView(R.id.video_btn)
    TextView btnShareVideo;
    @BindView(R.id.file_btn)
    TextView btnShareFile;
    @BindView(R.id.fileName_textView)
    TextView tvFileName;
    @BindView(R.id.image_preview)
    ImageView imgPreview;


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CHOOSE_IMAGE_CODE:
                    final ArrayList<BaseMedia> imageMedias = Boxing.getResult(data);
                    BaseMedia imageMedia = imageMedias.get(0);
                    mSelectedFilePath = imageMedia.getPath();
                    mSelectedFileType = DataType.FILE;
                    tvFileName.setText(FileUtil.getFileNameFromPath(mSelectedFilePath));
                    String path;
                    path = ((ImageMedia) imageMedia).getThumbnailPath();
                    BoxingMediaLoader.getInstance().displayThumbnail(imgPreview, path, 300, 300);
                    Log.e("ResourceShare", "选择了图片: " + mSelectedFilePath);
                    break;
                case CHOOSE_VIDEO_CODE:
                    final ArrayList<BaseMedia> videoMedias = Boxing.getResult(data);
                    BaseMedia videoMedia = videoMedias.get(0);
                    mSelectedFilePath = videoMedia.getPath();
                    mSelectedFileType = DataType.STREAM;
                    tvFileName.setText(FileUtil.getFileNameFromPath(mSelectedFilePath));
                    BoxingMediaLoader.getInstance().displayThumbnail(imgPreview, mSelectedFilePath, 300, 300);
                    Log.e("ResourceShare", "选择了视频: " + mSelectedFilePath);
                    break;
                case CHOOSE_FILE_CODE:
                    Uri uri = data.getData();
                    mSelectedFilePath = FileUtil.getFileAbsolutePath(getActivity(), uri);
                    mSelectedFileType = DataType.FILE;
                    tvFileName.setText(FileUtil.getFileNameFromPath(mSelectedFilePath));
                    imgPreview.setImageResource(R.drawable.file_preview);
                    Log.e("ResourceShare", "选择了文件: " + mSelectedFilePath);
                    break;
            }
        }
    }

    @OnClick(R.id.image_btn)
    void imageChooseClicked()
    {
        BoxingConfig singleImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        Boxing.of(singleImgConfig).withIntent(getContext(), BoxingActivity.class).start(this, CHOOSE_IMAGE_CODE);
    }

    @OnClick(R.id.video_btn)
    void videoChooseClicked()
    {
        BoxingConfig singleVideoConfig = new BoxingConfig(BoxingConfig.Mode.VIDEO);
        Boxing.of(singleVideoConfig).withIntent(getContext(), BoxingActivity.class).start(this, CHOOSE_VIDEO_CODE);
    }

    @OnClick(R.id.file_btn)
    void fileChooseClicked()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        try {
            startActivityForResult(intent, CHOOSE_FILE_CODE);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getActivity(), "没有默认文件管理器，请安装",  Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.resource_share_menu_nfc)
    void NFCShareClicked()
    {
        if (mSelectedFilePath == null || mSelectedFileType == null)
            Toast.makeText(getActivity(), "请先选择文件！", Toast.LENGTH_SHORT).show();
        else
        {
            DeviceInfoGetter deviceInfoGetter = DeviceInfoGetter.getInstance(getContext());
            File file = new File(mSelectedFilePath);
            FileInfo fileInfo = new FileInfo(FileUtil.getFileNameFromPath(mSelectedFilePath), mSelectedFilePath, file.getTotalSpace());
            NFCTransferData nfcData = new NFCTransferData(mSelectedFileType, deviceInfoGetter.getDeviceInfo(), JSON.toJSONString(fileInfo));
            Intent startIntent = new Intent(getContext(), NFCSendActivity.class);
            startIntent.putExtra(NFCSendActivity.DATA_INFO, nfcData);
            startActivity(startIntent);
        }
    }

    @OnClick(R.id.resource_share_menu_wifi)
    void WIFIShareClicked()
    {
        if (mSelectedFilePath == null || mSelectedFileType == null)
            Toast.makeText(getActivity(), "请先选择文件！", Toast.LENGTH_SHORT).show();
        else
        {
            DeviceInfoGetter deviceInfoGetter = DeviceInfoGetter.getInstance(getContext());
            File file = new File(mSelectedFilePath);
            FileInfo fileInfo = new FileInfo(FileUtil.getFileNameFromPath(mSelectedFilePath), mSelectedFilePath, file.getTotalSpace());
            WIFITransferData wifiData = new WIFITransferData(mSelectedFileType, deviceInfoGetter.getDeviceInfo(), JSON.toJSONString(fileInfo));
            Intent startIntent = new Intent(getContext(), WIFISendActivity.class);
            startIntent.putExtra(WIFISendActivity.DATA_INFO, wifiData);
            startActivity(startIntent);
        }
    }
}
