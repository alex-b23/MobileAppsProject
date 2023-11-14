package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    TextInputEditText userEmailObj;
    TextInputEditText userPasswordObj;
    Button registrationBtnObj;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // setting up firebase
        mAuth = FirebaseAuth.getInstance();
        // getting the fields and the button from the registration xml
        userEmailObj = findViewById(R.id.userEmail);
        userPasswordObj = findViewById(R.id.userPassword);
        registrationBtnObj = findViewById(R.id.registrationBtn);

        // Registration button logic
        registrationBtnObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail;
                String userPassword;
                // Getting the text fields input as a string for submissions
                userEmail = userEmailObj.getText().toString();
                userPassword = userPasswordObj.getText().toString();

                // Added checks to see if the user tries to submit empty fields they will get the messages below
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(Registration.this, "User E-mail field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(Registration.this, "User Password field Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // This will create the users information for the login -
                // Code taken from the Firebase docs -> https://firebase.google.com/docs/auth/android/password-auth#java_2
                // The code was updates by me(alex) to work properly with out project
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // If account creation succeed, display a message to the user.
                                    Toast.makeText(Registration.this, "Account Successfully Created.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If account creation fails, display a message to the user.
                                    Toast.makeText(Registration.this, "Account Creation Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}