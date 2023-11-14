package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class Registration extends AppCompatActivity {

    TextInputEditText userEmail;
    TextInputEditText userPassword;
    Button registrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        registrationBtn = findViewById(R.id.registrationBtn);
    }
}