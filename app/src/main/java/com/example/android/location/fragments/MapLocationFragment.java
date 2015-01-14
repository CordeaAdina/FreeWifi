package com.example.android.location.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.location.MapsActivity;
import com.example.android.location.R;

import java.util.Iterator;
import java.util.List;



// incearca sa faci o functie in clicklistener, in care sa populezi lista, functia sa stransmita ca
//parametru Stringul creat in wifiScan

public class MapLocationFragment extends Fragment {


    private Button mShowWifi;
    private Button mShowMap;
    private WifiManager wifi;
    private List<ScanResult> scanList;
    private String[] sList, sListS;
    private String ssId;
    private String capabilites;
    private String security;
    private ListView list;

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
                        (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                getActivity().registerReceiver(new BroadcastReceiver() {

                    @SuppressLint("UseValueOf")
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        scanList = wifiManager.getScanResults();
                        sList = new String[scanList.size()];
                        sListS = new String[scanList.size()];
                        for (int i = 0; i < scanList.size(); i++) {
                            sList[i] = scanList.get(i).SSID;
                            System.out.println("aka " + scanList.get(i).SSID);
                            populateList(sList);
                        }
                    }

                }, filter);
                wifiManager.startScan();
                registerClickCallback();

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
        list = (ListView) getActivity().findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    public void registerClickCallback() {
        ListView list = (ListView) getActivity().findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                ssId = textView.getText().toString();
                Log.e("logcat", "am apasat");
                Toast.makeText(getActivity().getApplicationContext(), "You clicked " + ssId , Toast.LENGTH_LONG).show();
                checkTypeOfConnection(ssId);
            }
        });
    }

    public void checkTypeOfConnection(final String ssId){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final WifiManager wifiManager =
                (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        getActivity().registerReceiver(new BroadcastReceiver() {

            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {
                scanList = wifiManager.getScanResults();
                sList = new String[scanList.size()];
                sListS = new String[scanList.size()];
                for (int i = 0; i < scanList.size(); i++) {
                    String idCheck = scanList.get(i).SSID;
                    if(idCheck.equals(ssId)){
                        capabilites = scanList
                                .get(i).capabilities;
                        break;
                    }
                }
                security = capabilites.substring(1,4);
                if(security.equals("WPA")){
                    connectToWifiWPA(ssId);
                }
                else if(security.equals("WEP")){
                    connectToWifiWEP(ssId);
                }
                Toast.makeText(getActivity().getApplicationContext(), security, Toast.LENGTH_LONG).show();
            }

        }, filter);
        wifiManager.startScan();
    }

    public void connectToWifiWPA(String input){



        String networkSSID = input;
        String networkPass = "xxVLADEE";

        WifiConfiguration conf = new WifiConfiguration();
        WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        conf.SSID = "\"" + networkSSID + "\"";

        conf.preSharedKey = "\""+ networkPass +"\"";

        System.out.println("CONF  " +conf.SSID + "   " + conf.preSharedKey);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        Iterator it = list.iterator();
        int ok =0;
        while(it.hasNext())
        {
            WifiConfiguration i = (WifiConfiguration)it.next();
            System.out.println("haha   " + i.networkId + "  " + i.preSharedKey + "  " + i.SSID);
            if(conf.SSID==i.SSID)
            {
                ok=1;
            }

        }

         if(ok==0)
             wifiManager.addNetwork(conf);



        for( WifiConfiguration i : list ){
            System.out.println("am apasat pe " + i.SSID);

            if(i.SSID != null && i.SSID.equals("\"" + input + "\"")) {
                Toast.makeText(getActivity().getApplicationContext(), "CONECTAM", Toast.LENGTH_LONG).show();
                wifiManager.disconnect();
                System.out.println("NET ID2 " +wifiManager.getConnectionInfo());
                conf.networkId = i.networkId;
                wifiManager.updateNetwork(conf);
                wifiManager.enableNetwork(i.networkId, true);


                wifiManager.reconnect();

                System.out.println("NET ID5 " +wifiManager.getConnectionInfo());

                if(wifiManager.getConnectionInfo().getNetworkId() > 0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "you are connected", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "a aparut o eroare", Toast.LENGTH_LONG).show();

                break;



            }

        }

    }

    public void connectToWifiWEP(String input){



        String networkSSID = input;
        String networkPass = "xxVLADEE";

        WifiConfiguration conf = new WifiConfiguration();
        WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        conf.SSID = "\"" + networkSSID + "\"";

        conf.wepKeys[0] = "\"" + networkPass + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        Iterator it = list.iterator();
        int ok =0;
        while(it.hasNext())
        {
            WifiConfiguration i = (WifiConfiguration)it.next();
            if(conf.SSID==i.SSID)
            {
                ok=1;
            }

        }

        if(ok==0)
            wifiManager.addNetwork(conf);



        for( WifiConfiguration i : list ){
            System.out.println("am apasat pe " + i.SSID);

            if(i.SSID != null && i.SSID.equals("\"" + input + "\"")) {
                Toast.makeText(getActivity().getApplicationContext(), "CONECTAM", Toast.LENGTH_LONG).show();
                wifiManager.disconnect();
                System.out.println("NET ID2 " +wifiManager.getConnectionInfo());
                conf.networkId = i.networkId;
                wifiManager.updateNetwork(conf);
                wifiManager.enableNetwork(i.networkId, true);


                wifiManager.reconnect();

                System.out.println("NET ID5 " +wifiManager.getConnectionInfo());

                if(wifiManager.getConnectionInfo().getNetworkId() > 0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "you are connected", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "a aparut o eroare", Toast.LENGTH_LONG).show();

                break;



            }

        }

    }



}


