package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;


public class MapActivity extends Activity {

    private EditText wifiNameEditText;
    private EditText wifiPassEditText;
    private Button mCancelW;
    private Button mOk;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Intent i = getIntent();
        final String objectId = i.getStringExtra("objectId");



        mCancelW = (Button)(findViewById(R.id.cancelWifiID));
        mCancelW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(),LogInActivity.class);
                startActivity(in);
            }
        });

        mOk = (Button) (findViewById(R.id.okWifiID));
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                wifiNameEditText = (EditText) (findViewById(R.id.editWifiNameID));
                wifiPassEditText = (EditText) (findViewById(R.id.editwifiPassID));
                String wifiName = wifiNameEditText.getText().toString();
                String wifiPass = wifiPassEditText.getText().toString();


                boolean invalid = false;

                if(wifiName.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Wifi Name", Toast.LENGTH_SHORT).show();

                }
                else
                if(wifiPass.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Wifi Password", Toast.LENGTH_SHORT).show();

                }
                else
                if(invalid == false) {


                    ParseObject wifi = new ParseObject("Wifi");
                    wifi.put("WifiName", wifiName);
                    wifi.put("WifiPassword", wifiPass);

                    wifi.put("UserId", objectId);
                    wifi.saveInBackground();


                    //start fragment 2


                    Toast.makeText(getApplicationContext(), "Submit succeed!", Toast.LENGTH_SHORT).show();

                    Intent in = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(in);

                }




            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
