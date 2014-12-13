package com.example.android.location.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.location.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class RegisterFragment extends Fragment {

    private Button mCancel;
    private Button mSubmit;

    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passEditText;
    private EditText emailEditText;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register, container, false);


        mCancel = (Button) (view.findViewById(R.id.cancelID));
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Intent in = new Intent(getBaseContext(),LogInActivity.class);
                //    startActivity(in);
                LoginFragment lgFr = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(RegisterFragment.this);
                fragmentTransaction.add(R.id.fragment_container, lgFr);
                fragmentTransaction.commit();
                // getFragmentManager().beginTransaction(R.id.fragment_container,lgFr).commit();
            }
        });


        mSubmit = (Button) (view.findViewById(R.id.submitID));
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText = (EditText) (view.findViewById(R.id.editNameID));
                usernameEditText = (EditText) (view.findViewById(R.id.editUNameID));
                passEditText = (EditText) (view.findViewById(R.id.editPassID));
                emailEditText = (EditText) (view.findViewById(R.id.editEmailID));


                final String name = nameEditText.getText().toString();
                final String username = usernameEditText.getText().toString();
                final String password = passEditText.getText().toString();
                final String email = emailEditText.getText().toString();


                boolean invalid = false;

                if (name.equals("")) {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
                } else if (username.equals("")) {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    invalid = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                } else if (invalid == false) {


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


                                    Toast.makeText(getActivity().getApplicationContext(), "Submit succeed!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Try another username!", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                // handle Parse Exception here
                            }
                        }
                    });


                }
            }
        });

        return view;
    }



}
