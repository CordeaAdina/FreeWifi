package com.example.android.location.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.location.R;
import com.parse.ParseObject;


public class AddWifiFragment extends Fragment {

    private EditText wifiNameEditText;
    private EditText wifiPassEditText;
    private Button mCancelW;
    private Button mOk;
    private String objectId;


    public void addWifi(String id){
        // Here you have it
        this.objectId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final  View view = inflater.inflate(R.layout.fragment_add_wifi, container, false);



   //     Intent i = getIntent();
   //     final String objectId = i.getStringExtra("objectId");



        mCancelW = (Button)(view.findViewById(R.id.cancelWifiID));
        mCancelW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment lgFr = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(AddWifiFragment.this);
                fragmentTransaction.add(R.id.fragment_container, lgFr);
                fragmentTransaction.commit();
            }
        });

        mOk = (Button) (view.findViewById(R.id.okWifiID));
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                wifiNameEditText = (EditText) (view.findViewById(R.id.editWifiNameID));
                wifiPassEditText = (EditText) (view.findViewById(R.id.editwifiPassID));
                String wifiName = wifiNameEditText.getText().toString();
                String wifiPass = wifiPassEditText.getText().toString();


                boolean invalid = false;

                if(wifiName.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your Wifi Name", Toast.LENGTH_SHORT).show();

                }
                else
                if(wifiPass.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your Wifi Password", Toast.LENGTH_SHORT).show();

                }
                else
                if(invalid == false) {


                    ParseObject wifi = new ParseObject("Wifi");
                    wifi.put("WifiName", wifiName);
                    wifi.put("WifiPassword", wifiPass);

                    wifi.put("UserId", objectId);
                    wifi.saveInBackground();


                    //start fragment 2


                    Toast.makeText(getActivity().getApplicationContext(), "Submit succeed!", Toast.LENGTH_SHORT).show();

                    MapLocationFragment mapFr = new MapLocationFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(AddWifiFragment.this);
                    fragmentTransaction.add(R.id.fragment_container, mapFr);
                    fragmentTransaction.commit();

                }




            }
        });


        return view;


    }



}
