package com.example.android.location.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class LoginFragment extends Fragment {


    private Button mRegister, mLogin;

    private EditText usernameTextLogin;
    private EditText passTextLogin;
    private String uN;
    private String pass;
    private String userName;
    private String password;

    private ParseObject user ; //= new ParseObject("User");
    private boolean loginSuccessful;

    LoginClickInterface loginCallback;

    public LoginFragment() {
        // Required empty public constructor
    }

    public interface LoginClickInterface{
        public void sendId(String id);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            loginCallback = (LoginClickInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        mRegister = (Button)view.findViewById(R.id.registerID);
        mLogin = (Button)view.findViewById((R.id.loginID));


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterFragment regFr = new RegisterFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, regFr).commit();

            }
        });


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usernameTextLogin = (EditText) (view.findViewById(R.id.editUNameLoginID));
                passTextLogin = (EditText) (view.findViewById(R.id.editPassLoginID));

                String uN = usernameTextLogin.getText().toString();
                String pass = passTextLogin.getText().toString();


                extractDB(uN, pass);



            }
        });

        return view;
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
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter your Username and Password", Toast.LENGTH_SHORT).show();

                    } else { // login successful
                        user = users.get(0);
                        String uId = user.getObjectId();
                        loginCallback.sendId(uId);
                    }


                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

}