package com.clverpanda.nfshare;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.model.TransferData;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NFCSendActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.NFCSendActivity.data";
    private static final int MESSAGE_SENT = 1;


    private NfcAdapter mNfcAdapter;

    private TransferData dataToSend;
    @BindView(R.id.nfc_animation)
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcsend);
        ButterKnife.bind(this);

        getINFO();
        //NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "不支持NFC", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event)
        {
        String message = JSON.toJSONString(dataToSend);
        NdefMessage msg = new NdefMessage(NdefRecord.createMime(
                "application/com.clverpanda.nfshare", message.getBytes()),
                NdefRecord.createApplicationRecord("com.clverpanda.nfshare"));
        return msg;
    }

    @Override
    public void onNdefPushComplete(NfcEvent arg0)
    {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "需要传输的内容已经发送完毕!", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    };

    private void getINFO()
    {
        dataToSend = getIntent().getParcelableExtra(DATA_INFO);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        rippleBackground.startRippleAnimation();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        rippleBackground.stopRippleAnimation();
    }
}
