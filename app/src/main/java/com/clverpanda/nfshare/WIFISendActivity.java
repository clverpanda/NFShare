package com.clverpanda.nfshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clverpanda.nfshare.model.NFCTransferData;
import com.clverpanda.nfshare.model.WIFITransferData;

import butterknife.ButterKnife;

public class WIFISendActivity extends AppCompatActivity
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.WIFISendActivity.data";

    private WIFITransferData dataToSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifisend);
        ButterKnife.bind(this);

        getINFO();
    }

    private void getINFO()
    {
        dataToSend = (WIFITransferData) getIntent().getSerializableExtra(DATA_INFO);
    }
}
