package com.clverpanda.nfshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NFCSendActivity extends AppCompatActivity
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.NFCSendActivity.data";

    private String jsonToSend;
    @BindView(R.id.nfc_animation)
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcsend);
        ButterKnife.bind(this);

        getINFO();
        Log.d("clver", "onCreate: " + jsonToSend);
    }

    private void getINFO()
    {
        jsonToSend = getIntent().getStringExtra(DATA_INFO);
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
