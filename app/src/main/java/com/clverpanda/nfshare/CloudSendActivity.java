package com.clverpanda.nfshare;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clverpanda on 2017/5/24 0024.
 * It's the file for NFShare.
 */

public class CloudSendActivity extends WIFISendActivity
{
    public static final String TAG = "CloudSendActivity";
    @BindView(R.id.textView_share)
    TextView tvShareWord;
    @BindView(R.id.tv_share_pin)
    TextView tvSharePin;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudsend);
        ButterKnife.bind(this);
    }

    @Override
    protected void startServiceRegistration()
    {
        super.startServiceRegistration();

    }

}
