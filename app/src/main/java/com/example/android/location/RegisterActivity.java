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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class RegisterActivity extends Activity {

    private Button mCancel;
    private Button mSubmit;

    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passEditText;
    private EditText emailEditText;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister);

        mCancel = (Button)(findViewById(R.id.cancelID));
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(),LogInActivity.class);
                startActivity(in);
            }
        });


        mSubmit = (Button)(findViewById(R.id.submitID));
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText = (EditText)(findViewById(R.id.editNameID));
                usernameEditText = (EditText) (findViewById(R.id.editUNameID));
                passEditText = (EditText) (findViewById(R.id.editPassID));
                emailEditText = (EditText) (findViewById(R.id.editEmailID));


                final String name = nameEditText.getText().toString();
                final String username = usernameEditText.getText().toString();
                final String password = passEditText.getText().toString();
                final String email = emailEditText.getText().toString();


                boolean invalid = false;

                if(name.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
                }
                else

                if(username.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
                }
                else

                if(password.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else

                if(email.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                }
                else
                if(invalid == false) {


                    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                    query.whereEqualTo("Username", username);

                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> users, ParseException e) {
                            if (e == null) {
                                // your logic here

                                if (users.isEmpty()) {

                                    ParseObject user = new ParseObject("User");
                                    user.put("Name", name);
                                    user.put("Username", username);
                                   user.put("Password", password);
                                    user.put("Email", email);
                                    user.saveInBackground();



                                    Toast.makeText(getApplicationContext(), "Submit succeed!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Try another username!", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                // handle Parse Exception here
                            }
                        }
                    });



                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rgister, menu);
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
