package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.location.fragments.AddWifiFragment;
import com.example.android.location.fragments.LoginFragment;
import com.example.android.location.fragments.MapLocationFragment;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity implements LoginFragment.LoginClickInterface{

    public void sendId(String id){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLogged", true);
        editor.putString("lastLoggedUserId", id);

        SharedPreferences wifiAddedPref = getApplicationContext().getSharedPreferences("WifiAdded", 0);
        SharedPreferences.Editor wifiAddedEditor = wifiAddedPref.edit();

        Set<String> userIds;
        userIds = pref.getStringSet("user", null);

        if (userIds == null) {
            userIds = new HashSet<String>();
        }
        if (!userIds.contains(id)) {
            userIds.add(id);
            editor.putStringSet("user", userIds);
            wifiAddedEditor.putBoolean(id, false);
            wifiAddedEditor.commit();
            AddWifiFragment addWifiFragment = new AddWifiFragment();
            getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("loginFragment"));
            getFragmentManager().beginTransaction().add(R.id.fragment_container, addWifiFragment).commit();
            addWifiFragment.addWifi(id);
        }  else  if ( wifiAddedPref.getBoolean(id, false) )

        {

            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapLocationFragment()).commit();


            //  editor.putBoolean("wifiAdded", true);
            //  getFragmentManager().beginTransaction().replace(R.id.fragment_container, addWifiFragment).commit();
        } else {
            AddWifiFragment addWifiFragment = new AddWifiFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, addWifiFragment).commit();
            addWifiFragment.addWifi(id);
        }

        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "Lrr5RvsXIoBVOopnyf61gy6yUztnq0qfshDoFZdq", "e6ZLVdTFWBU1w59movCMbzHDdU822cD6s1amDmyr");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences wifiAddedPref = getApplicationContext().getSharedPreferences("WifiAdded", 0); // 0 - for private mode

        boolean isLogged = pref.getBoolean("isLogged",false);
        if (!isLogged) {
            LoginFragment fragment = new LoginFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "loginFragment").commit();
        } else {
            String lastId = pref.getString("lastLoggedUserId", null);
            boolean wifiAdded = wifiAddedPref.getBoolean(lastId, false);
            if (!wifiAdded) {
                AddWifiFragment addWifiFragment = new AddWifiFragment();
                getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("loginFragment"));
                getFragmentManager().beginTransaction().add(R.id.fragment_container, addWifiFragment).commit();
                addWifiFragment.addWifi(lastId);
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapLocationFragment()).commit();
            }
        }
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
        if (id == R.id.action_logout) {

            SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isLogged", false);
            //  editor.putBoolean("wifiAdded",false);

            editor.commit();

            new Handler().post(new Runnable() {

                @Override
                public void run()
                {
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    overridePendingTransition(0, 0);
                    finish();

                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
