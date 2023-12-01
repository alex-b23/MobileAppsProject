package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button signOutBtns;
    private Button viewProfileBtns;
    private Button editProfileBtns;
    private Button homeBtns;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        signOutBtns = findViewById(R.id.signOutbtn);
        viewProfileBtns = findViewById(R.id.viewProfileBtn);
        editProfileBtns = findViewById(R.id.editProfilebtn);
        homeBtns = findViewById(R.id.homeBtn);

        signOutBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user
                mAuth.signOut();

                // Redirect to the Login activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        // Sending the user to the View Profile Page
        viewProfileBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfile userProfileFragment = new UserProfile();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, userProfileFragment)
                        .commit();
            }
        });
        // Sending the to the Profile Changing Page
        editProfileBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the Profile changing activity
                Intent intent = new Intent(getApplicationContext(), UserProfileCreation.class);
                startActivity(intent);
                finish();
            }
        });
        homeBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the Home  activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}