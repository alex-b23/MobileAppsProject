package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

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
                Map<String, Object> users = new HashMap<>();
                users.put("Username", username);
                users.put("DisplayName", displayName);
                users.put("Bio", bio);
                db = FirebaseFirestore.getInstance();

                db.collection("users")
                        .add(users)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(UserProfileCreation.this, "Profile Created Succesfully",Toast.LENGTH_SHORT).show();
                                // Start UserProfileCreation activity
                                Intent intent = new Intent(UserProfileCreation.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserProfileCreation.this, "Something went Wrong",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}