package com.clverpanda.nfshare;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.clverpanda.nfshare.fragments.ContentFrag;
import com.clverpanda.nfshare.fragments.DevicesFrag;
import com.clverpanda.nfshare.fragments.ResourceFrag;
import com.clverpanda.nfshare.fragments.TasksFrag;
import com.clverpanda.nfshare.fragments.TestFrag;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    FragmentManager mFm;
    NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFm = getSupportFragmentManager();

        //Fragment
        mFm.beginTransaction().replace(R.id.fragment_container, new ContentFrag()).commit();

        //NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "不支持NFC", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            String msg = processIntent(getIntent());
            mFm.beginTransaction().replace(R.id.fragment_container, TasksFrag.newInstance(msg, 1)).commit();
        }
    }


    protected String processIntent(Intent intent)
    {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        return msg.toString();
    }


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
    public boolean onNavigationItemSelected(MenuItem item)
    {
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
        } else if (id == R.id.nav_devices)
        {
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.fragment_container, new DevicesFrag());
            ft.commit();
        }
        else if (id == R.id.nav_tasks)
        {
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.fragment_container, new TasksFrag());
            ft.commit();
        }
        else if (id == R.id.nav_settings)
        {
            FragmentTransaction ft = mFm.beginTransaction();
            ft.replace(R.id.fragment_container, new TestFrag());
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }


}
