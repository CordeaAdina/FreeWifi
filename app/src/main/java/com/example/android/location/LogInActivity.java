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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.List;


public class LogInActivity extends Activity {


    private Button mRegister;
    private Button mLogin;

    private EditText usernameTextLogin;
    private EditText passTextLogin;
    private String uN;
    private String pass;
    private String userName;
    private String password;


    private ParseObject user ; //= new ParseObject("User");
    private boolean loginSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "Lrr5RvsXIoBVOopnyf61gy6yUztnq0qfshDoFZdq", "e6ZLVdTFWBU1w59movCMbzHDdU822cD6s1amDmyr");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        mRegister = (Button)findViewById(R.id.registerID);
        mLogin = (Button) findViewById((R.id.loginID));


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);

            }
        });


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usernameTextLogin = (EditText) (findViewById(R.id.editUNameLoginID));
                passTextLogin = (EditText) (findViewById(R.id.editPassLoginID));

                String uN = usernameTextLogin.getText().toString();
                String pass = passTextLogin.getText().toString();


                extractDB(uN, pass);


            }
        });


    }



    public void extractDB(String username, String password){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("Username", username);
        query.whereEqualTo("Password", password);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    // your logic here

                    if (users.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter your Username and Password", Toast.LENGTH_SHORT).show();

                    } else {
                        user = users.get(0);
                        String uId = user.getObjectId();
                        Intent i = new Intent(getBaseContext(), MapActivity.class);
                        i.putExtra("objectId", uId);
                        startActivity(i);
                    }


                } else {
                    // handle Parse Exception here
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return false;
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
