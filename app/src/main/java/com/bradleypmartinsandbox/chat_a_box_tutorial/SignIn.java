package com.bradleypmartinsandbox.chat_a_box_tutorial;

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

    String displayName;

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
                    displayName = displayNameEditText.getText().toString();

                    registerNewUser(email, password, displayName);
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
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    Log.i(TAG, "User is logged in : " + user.getEmail());
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
                    Log.i(TAG, "User registration failed.");
                }
            }
        };

        OnFailureListener fail = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Registration call failed.");
            }
        };

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

}
