package com.example.android.location;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.android.location.fragments.AddWifiFragment;
import com.example.android.location.fragments.MapLocationFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private View mapView;
    private LocationClient mLocationClient;
    private ParseObject wifi ;
    private ArrayList<ParseGeoPoint> geopoints;
    HashMap<ParseGeoPoint, String>  hashMap;
    private Marker myMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLocationClient = new LocationClient(this, this, this);
        setUpMapIfNeeded();
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
    protected void onResume() {
        super.onResume();
        //   setUpMapIfNeeded();


    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {





        ParseQuery<ParseObject> query = ParseQuery.getQuery("Wifi");
        query.whereExists("GeoPoint");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> wifis, ParseException e) {
                if (e == null) {
                    // your logic here

                    if (wifis.isEmpty()) {
                        //   Toast.makeText(this, "Please enter your Username and Password", Toast.LENGTH_SHORT).show();

                    } else {

                        hashMap = new HashMap<ParseGeoPoint, String>(wifis.size());

                        geopoints = new ArrayList<ParseGeoPoint>();
                        for (ParseObject wifi : wifis) {
                            hashMap.put(wifi.getParseGeoPoint("GeoPoint"),wifi.getString("WifiName"));
                            geopoints.add(wifi.getParseGeoPoint("GeoPoint"));

                        }
                        final double[] locations = getLocation();

                        mMap.addMarker(new MarkerOptions().position(new LatLng(locations[0], locations[1])).title("This is me!"));
                        final LatLng p1 = new LatLng(locations[0], locations[1]);
                        getAddress();

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p1, 15));
                    }
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("AAAAAAAAAAAAAAAAA","bun");
                //In loc de asta trebuie pusa metoda in care te conectezi automat la wifi-ul respectiv(plasat la acel marker)
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
            }
        });
/*
        mMap.setOnMarkerClickListener(this);

        myMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Spot")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

*/

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(myMarker))
        {
            //handle click here
        }
        return true;
    }



    @Override
    public void onConnected(Bundle bundle) {
        setUpMap();
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
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                Toast.makeText(this, "Google Play Services are not available", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
                String errorString = getString(
                        R.string.illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );
                // Log the error and print the stack trace
                Log.e(LocationUtils.APPTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );



                // Return the text
                return addressText+"#"+location.getLatitude()+"#"+location.getLongitude();

                // If there aren't any addresses, post a message
            } else {
                return getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {
            String[] addr = address.split("#");
            final LatLng p = new LatLng(Double.parseDouble(addr[1]), Double.parseDouble(addr[2]));
            ParseGeoPoint location = null;
            for (ParseGeoPoint geop : geopoints) {
                if (geop.getLatitude() == (Double.parseDouble(addr[1])) && (geop.getLongitude() == Double.parseDouble(addr[2]))) {
                    location = geop;
                    break;
                }
            }
            String titleWifi = hashMap.get(location);
            mMap.addMarker(new MarkerOptions().position(p).title(titleWifi).snippet(addr[0]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    public void getAddress() {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Start the background task
            for (ParseGeoPoint geoPoint : geopoints) {
                Location loc = new Location("loc");
                loc.setLatitude(geoPoint.getLatitude());
                loc.setLongitude(geoPoint.getLongitude());
                (new GetAddressTask(this)).execute(loc);
            }
        }
    }

}
