package com.clverpanda.nfshare;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.clverpanda.nfshare.Fragments.ContentFrag;
import com.clverpanda.nfshare.Fragments.ResourceFrag;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
//        NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback{
    private static final int MESSAGE_SENT = 1;


    NfcAdapter mNfcAdapter;
    EditText mSendText;
    FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFm = getSupportFragmentManager();

        //Fragment
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mFm.beginTransaction().add(R.id.fragment_container, new ContentFrag()).commit();
        }


        //NFC
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "不支持NFC", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            mNfcAdapter.setNdefPushMessageCallback(this, this);
//            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
//        }
    }

//    @Override
//    public NdefMessage createNdefMessage(NfcEvent event) {
//        mSendText = (EditText) findViewById(R.id.nfc_text_send);
//        String message = mSendText.getText().toString();
//        NdefMessage msg = new NdefMessage(NdefRecord.createMime(
//                "application/com.clverpanda.nfshare", message.getBytes()),
//                NdefRecord.createApplicationRecord("com.clverpanda.nfshare"));
//        return msg;
//    }

    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SENT:
                    Toast.makeText(getApplicationContext(), "NFC消息已经发送!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_content)
        {
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.fragment_container, new ContentFrag());
            ft.commit();
        }
        else if (id == R.id.nav_resource)
        {
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.fragment_container, new ResourceFrag());
            ft.commit();
        } else if (id == R.id.nav_devices) {

        } else if (id == R.id.nav_tasks) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }


    public void toReceive(View view) {
        Intent intent = new Intent(this, ReceiveActivity.class);
        startActivity(intent);
    }


}
