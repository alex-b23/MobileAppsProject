package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.auth.User;

public class UserProfileCreation extends AppCompatActivity {
    TextInputEditText usernameEditText, displayNameEditText, bioEditText;
    Button createProfilebtn;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_creation);

        // Initializing the EditText fields & button
        usernameEditText = findViewById(R.id.userUsername);
        displayNameEditText = findViewById(R.id.userDisplayName);
        bioEditText = findViewById(R.id.userBio);
        createProfilebtn = findViewById(R.id.createprofileButton);
        createProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String displayName = displayNameEditText.getText().toString();
                String bio = bioEditText.getText().toString();
            }
        });
    }
}