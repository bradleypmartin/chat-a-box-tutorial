package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    final String TAG = "FirebaseTest";

    FirebaseApp app;
    FirebaseAuth auth;

    FirebaseAuth.AuthStateListener authStateListener;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
    }

    private void initFirebase() {

        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        logoutUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    Log.i(TAG, "Auth state update : valid current user logged on : email [" +
                            user.getEmail() + "] display name [" +
                            user.getDisplayName() + "]");
                    displayName = user.getDisplayName();
                } else {
                    Log.i(TAG, "Auth state update : no valid current user logged on.");
                    displayName = "Null";

                    Intent signIn = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(signIn);
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    private void logoutUser() {
        auth.signOut();
    }
}
