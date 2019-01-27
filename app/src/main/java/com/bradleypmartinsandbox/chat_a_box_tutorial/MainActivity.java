package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
                    displayName = "No valid user";

                    auth.removeAuthStateListener(authStateListener);
                    Intent signIn = new Intent(getApplicationContext(), SignIn.class);
                    startActivityForResult(signIn, 101);
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"Activity returned");

        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                displayName = data.getStringExtra("displayName");
                Log.i(TAG, "Intent returned display name : [" + displayName + "].");
                auth.addAuthStateListener(authStateListener);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            Log.i(TAG, "Logout option selected.");
            auth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
