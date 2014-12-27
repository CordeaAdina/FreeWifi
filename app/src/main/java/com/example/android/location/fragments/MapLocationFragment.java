package com.example.android.location.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.location.MainActivity;
import com.example.android.location.R;
import com.example.android.location.WifiNetworks;

import java.util.List;


public class MapLocationFragment extends Fragment {

    private Button mLogout;
    private Button mShowWifi;
    private TextView tv;
    private StringBuilder sb;
    List<ScanResult> scanList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_map_location, container, false);

        mLogout = (Button) view.findViewById(R.id.logoutID);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLogged", false);
                //  editor.putBoolean("wifiAdded",false);

                editor.commit();

                new Handler().post(new Runnable() {

                    @Override
                    public void run()
                    {
                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                });

            }
        });
        tv = (TextView) view.findViewById(R.id.textView);
        mShowWifi = (Button) view.findViewById(R.id.showWifiID);
        mShowWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                final WifiManager wifiManager =
                        (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
                getActivity().registerReceiver(new BroadcastReceiver(){

                    @SuppressLint("UseValueOf") @Override
                    public void onReceive(Context context, Intent intent) {
                        sb = new StringBuilder();
                        scanList = wifiManager.getScanResults();
                        sb.append("\n  Number Of Wifi connections :" + " " +scanList.size()+"\n\n");
                        for(int i = 0; i < scanList.size(); i++){
                            sb.append(new Integer(i+1).toString() + ". ");
                            sb.append((scanList.get(i)).toString());
                            sb.append("\n\n");
                        }

                        tv.setText(sb);
                    }

                },filter);
                wifiManager.startScan();
            }
        });

        return view;
    }
}


