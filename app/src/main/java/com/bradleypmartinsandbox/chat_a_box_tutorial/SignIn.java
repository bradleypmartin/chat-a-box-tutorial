package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SignIn extends AppCompatActivity {

    final String TAG = "FirebaseTestSignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.i(TAG, "Starting SignIn activity.");
    }
}
