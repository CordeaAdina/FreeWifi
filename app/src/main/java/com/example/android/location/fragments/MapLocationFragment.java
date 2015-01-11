package com.example.android.location.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.location.MapsActivity;
import com.example.android.location.R;

import java.util.List;



// incearca sa faci o functie in clicklistener, in care sa populezi lista, functia sa stransmita ca
//parametru Stringul creat in wifiScan

public class MapLocationFragment extends Fragment {


    private Button mShowWifi;
    private Button mShowMap;
    private WifiManager wifi;
    private List<ScanResult> scanList;
    private String[] sList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getActivity().getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_map_location, container, false);


        mShowWifi = (Button) view.findViewById(R.id.showWifiID);
        mShowWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                final WifiManager wifiManager =
                        (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                getActivity().registerReceiver(new BroadcastReceiver(){

                    @SuppressLint("UseValueOf") @Override
                    public void onReceive(Context context, Intent intent) {
                        scanList = wifiManager.getScanResults();
                        sList = new String[scanList.size()];
                        for(int i = 0; i < scanList.size(); i++){
                            sList[i] = (scanList.get(i)).toString();
                            populateList(sList);
                        }
                    }

                },filter);
                wifiManager.startScan();



            }
        });

        mShowMap = (Button)view.findViewById(R.id.showMapID);
        mShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public void populateList(String[] input){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.da_item, input);
        ListView list = (ListView) getActivity().findViewById(R.id.listView);
        list.setAdapter(adapter);
    }
}


