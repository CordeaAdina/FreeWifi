package com.example.android.location.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.android.location.R;
import com.parse.ParseObject;


public class AddWifiFragment extends Fragment {

    private EditText wifiNameEditText;
    private EditText wifiPassEditText;
    private EditText wifiRePassEditText;
    private Button mCancelW;
    private Button mOk;
    private String objectId;
    private ImageView checkmark;
    private  boolean isValidPass;

    //set id
    public void addWifi(String id){
        // Here you have it
        this.objectId = id;
    }


    public String getUserID(){
        return this.objectId;
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

        checkmark = (ImageView) view.findViewById(R.id.wifiCheckmarkID);
        wifiRePassEditText = (EditText) view.findViewById(R.id.editwifiRePassID);
        wifiPassEditText = (EditText) (view.findViewById(R.id.editwifiPassID));

        //     Intent i = getIntent();
        //     final String objectId = i.getStringExtra("objectId");


        isValidPass = false;


        wifiRePassEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String password = wifiPassEditText.getText().toString();
                String passwordRetype = wifiRePassEditText.getText().toString();

                if(!hasFocus){
                    if (password.equals(passwordRetype)){
                        isValidPass = true;
                        checkmark.setImageResource(R.drawable.checkmark);
                    }
                    else {
                        checkmark.setImageResource(R.drawable.checkfail);
                        isValidPass = false;
                    }
                }
            }
        });



        mCancelW = (Button)(view.findViewById(R.id.cancelWifiID));
        mCancelW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment lgFr = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, lgFr).commit();
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

                }  else if(isValidPass == false){
                    Toast.makeText(getActivity().getApplicationContext(), "Please check your password!", Toast.LENGTH_SHORT).show();
                }
                else
                if(invalid == false) {


                    ParseObject wifi = new ParseObject("Wifi");
                    wifi.put("WifiName", wifiName);
                    wifi.put("WifiPassword", wifiPass);

                    wifi.put("UserId", objectId);
                    wifi.saveInBackground();


                    SharedPreferences wifiAddedPref = getActivity().getSharedPreferences("WifiAdded", 0);
                    SharedPreferences.Editor wifiAddedEditor = wifiAddedPref.edit();
                    wifiAddedEditor.putBoolean(getUserID(),true);
                    wifiAddedEditor.commit();

                   /* SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("wifiAdded", true);
                    editor.commit();*/

                    //start fragment 2


                    Toast.makeText(getActivity().getApplicationContext(), "Submit succeed!", Toast.LENGTH_SHORT).show();

                    MapLocationFragment mapFr = new MapLocationFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFr).commit();

                }




            }
        });


        return view;


    }



}