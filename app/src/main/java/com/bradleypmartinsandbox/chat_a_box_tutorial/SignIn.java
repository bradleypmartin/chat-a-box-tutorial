package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    final String TAG = "FirebaseTestSignIn";

    EditText emailEdit;
    EditText passwordEdit;
    EditText confirmPassword;
    EditText displayName;

    Button loginButton;
    TextView registerText;

    boolean loginInProgress = false;
    boolean registerInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.i(TAG, "Starting SignIn activity.");

        initDisplayControls();
    }

    private void initDisplayControls(){

        emailEdit = findViewById(R.id.emailEditText);
        passwordEdit = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.passwordConfirmEditText);
        displayName = findViewById(R.id.displayNameEditText);

        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        emailEdit.setVisibility(View.GONE);
        passwordEdit.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
        displayName.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInProgress = false;

                if (!loginInProgress) {
                    // first time login has been hit
                    emailEdit.setVisibility(View.VISIBLE);
                    passwordEdit.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.GONE);
                    displayName.setVisibility(View.GONE);

                    loginInProgress = true;

                } else {
                    // login button hit again (user tries to log in)

                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInProgress = false;

                if (!registerInProgress) {
                    // first time register has been hit
                    emailEdit.setVisibility(View.VISIBLE);
                    passwordEdit.setVisibility(View.VISIBLE);
                    confirmPassword.setVisibility(View.VISIBLE);
                    displayName.setVisibility(View.VISIBLE);

                    registerInProgress = true;

                } else {
                    // register hit again (user tries to register)

                }
            }
        });
    }

}
