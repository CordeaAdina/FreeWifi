package com.example.android.location.fragments;

import android.app.Dialog;

import android.content.SharedPreferences;
import android.location.Location;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;


public class AddWifiFragment extends Fragment implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private EditText wifiNameEditText;
    private EditText wifiPassEditText;
    private EditText wifiRePassEditText;
    private Button mCancelW;
    private Button mOk;
    private String objectId;
    private ImageView checkmark;
    private  boolean isValidPass;

    private LocationClient mLocationClient;

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

        mLocationClient = new LocationClient(getActivity().getApplicationContext(), this, this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    public void onStop() {
        mLocationClient.disconnect();
        super.onStop();
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
                ParseGeoPoint geoPoint ;


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


                    double[] locations = getLocation();

                    geoPoint = new ParseGeoPoint(locations[0], locations[1]);

                    ParseObject wifi = new ParseObject("Wifi");
                    wifi.put("WifiName", wifiName);
                    wifi.put("WifiPassword", wifiPass);
                    wifi.put("GeoPoint", geoPoint);
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


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public double[] getLocation() {

        double[] locations = new double[2];
        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

            locations[0] = latitude;
            locations[1] = longitude;
        }
        return locations;
    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 0);
            if (dialog != null) {
                Toast.makeText(getActivity(), "Google Play Services are not available", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}