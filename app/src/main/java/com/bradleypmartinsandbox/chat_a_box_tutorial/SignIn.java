package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    final String TAG = "FirebaseTestSignIn";

    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    EditText displayNameEditText;

    Button loginButton;
    TextView registerText;

    boolean loginInProgress = false;
    boolean registerInProgress = false;

    String mDisplayName = "Unknown";

    FirebaseApp app;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.i(TAG, "Starting SignIn activity.");

        initDisplayControls();
        initListeners();
        initFirebase();
    }

    private void initDisplayControls(){

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.passwordConfirmEditText);
        displayNameEditText = findViewById(R.id.displayNameEditText);

        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        emailEditText.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);
        confirmPasswordEditText.setVisibility(View.GONE);
        displayNameEditText.setVisibility(View.GONE);

    }

    private void initListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInProgress = false;

                if (!loginInProgress) {
                    // first time login has been hit
                    emailEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    confirmPasswordEditText.setVisibility(View.GONE);
                    displayNameEditText.setVisibility(View.GONE);

                    loginInProgress = true;

                } else {
                    // login button hit again (user tries to log in)
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    loginUser(email, password);
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInProgress = false;

                if (!registerInProgress) {
                    // first time register has been hit
                    emailEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    confirmPasswordEditText.setVisibility(View.VISIBLE);
                    displayNameEditText.setVisibility(View.VISIBLE);

                    registerInProgress = true;

                } else {
                    // register hit again (user tries to register)
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String confirmPassword = confirmPasswordEditText.getText().toString();
                    mDisplayName = displayNameEditText.getText().toString();

                    registerNewUser(email, password, mDisplayName);
                }
            }
        });
    }

    private void initFirebase() {
        app  = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    String tempName = user.getDisplayName();
                    if (tempName != null)
                        mDisplayName = tempName;

                    Log.i(TAG, "User is logged in : email [" +
                            user.getEmail() + "] display name [" +
                            mDisplayName + "]");

                    if (registerInProgress) {
                        Log.i(TAG, "Should set display name now. Name: [" + mDisplayName + "]");
                        setDisplayName(user, mDisplayName);
                    } else {
                        mDisplayName = user.getDisplayName();
                    }
                    loginInProgress = false;
                    registerInProgress = false;

                    finishActivity();
                } else {
                    Log.i(TAG, "No user is logged in.");
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    private void registerNewUser(String email, String password, String displayName) {
        OnCompleteListener<AuthResult> success = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "User registration successful.");
                } else {
                    Log.i(TAG, "User registration response successful, but process failed.");
                }
            }
        };

        OnFailureListener fail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Registration call failed.");
            }
        };

        Log.i(TAG, "SignIn : Registering : email [" + email
                + "] password [" + password +
                "] Display Name [" + displayName + "]");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(success)
                .addOnFailureListener(fail);
    }

    private void loginUser(String email, String password) {
        OnCompleteListener<AuthResult> success = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "User logged on.");
                } else {
                    Log.i(TAG, "User logon failed.");
                }
            }
        };

        OnFailureListener fail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Login call failed.");
            }
        };

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(success)
                .addOnFailureListener(fail);
    }

    private void setDisplayName(FirebaseUser user, String displayName) {
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        user.updateProfile(changeRequest);
    }

    private void finishActivity() {

        Log.i(TAG, "Finishing Sign In activity.");

        Intent returningIntent = new Intent();
        returningIntent.putExtra("displayName", mDisplayName);
        setResult(RESULT_OK, returningIntent);

        finish();
    }

}
