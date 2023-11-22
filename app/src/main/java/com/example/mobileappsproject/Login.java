package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText userEmailObj;
    TextInputEditText userPasswordObj;
    Button loginBtnObj;
    FirebaseAuth mAuth;
    TextView registerNowBtnObj;

    // This checks if the User it's already signed in
    // This code it's taken from the official Firebase docs -> https://firebase.google.com/docs/auth/android/password-auth#java_2
    // Code was updated by me(alex) as needed for our project
    // If the user it's already logged in, this will automatically open the Main activity
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // setting up firebase
        mAuth = FirebaseAuth.getInstance();
        // getting the fields and the button from the registration xml
        userEmailObj = findViewById(R.id.userEmail);
        userPasswordObj = findViewById(R.id.userPassword);
        loginBtnObj = findViewById(R.id.loginBtn);
        registerNowBtnObj = findViewById(R.id.registerNowBtn);

        // Setting up the register now button which will take the user to
        // the register screen once the user taps on the button
        registerNowBtnObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtnObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail;
                String userPassword;
                // Getting the text fields input as a string for submissions
                userEmail = userEmailObj.getText().toString();
                userPassword = userPasswordObj.getText().toString();

                // Added checks to see if the user tries to submit empty fields they will get the messages below
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(Login.this, "User E-mail field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(Login.this, "User Password field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Just like in Registration, the original code for this was taken from the firebase docs
                // Here's the link where the original code can be found -> https://firebase.google.com/docs/auth/android/password-auth#java_2
                // The code has been changed by me(alex) to fit with our project
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Authentication Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}