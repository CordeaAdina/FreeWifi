package com.example.android.location;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.location.fragments.AddWifiFragment;
import com.example.android.location.fragments.LoginFragment;
import com.parse.Parse;


public class MainActivity extends Activity implements LoginFragment.LoginClickInterface{




    public void sendId(String id){
//        AddWifiFragment frag = (AddWifiFragment)
//              getFragmentManager().findFragmentById(R.id.fragment_container);
//        frag.addWifi(id);

        AddWifiFragment addWifiFragment = new AddWifiFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, addWifiFragment, "addWifiFragmentTag").commit();
        addWifiFragment.addWifi(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "Lrr5RvsXIoBVOopnyf61gy6yUztnq0qfshDoFZdq", "e6ZLVdTFWBU1w59movCMbzHDdU822cD6s1amDmyr");

        LoginFragment fragment = new LoginFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
