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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextInputEditText userEmailObj;
    TextInputEditText userPasswordObj;
    Button registrationBtnObj;
    FirebaseAuth mAuth;
    TextView loginNowBtnObj;
    FirebaseFirestore db;

    // OLD CODE -> Kept in Just in Case the New Idea Doesn't work as intended
    /// -------------------------------------------------- //
    // This checks if the User it's already signed in
    // This code it's taken from the official Firebase docs -> https://firebase.google.com/docs/auth/android/password-auth#java_2
    // Code was updated by me(alex) as needed for our project
    // If the user it's already logged in, this will automatically open the Main activity
    // @Override
    //public void onStart() {
    //    super.onStart();
    //    // Check if user is signed in (non-null) and update UI accordingly.
    //    FirebaseUser currentUser = mAuth.getCurrentUser();
    //    if(currentUser != null){
    //        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    //        startActivity(intent);
    //        finish();
    //    }
    //}
    /// -------------------------------------------------- //

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
        loginNowBtnObj = findViewById(R.id.loginNowBtn);
        // setting up the Firestore
        db = FirebaseFirestore.getInstance();
        // Setting up the login now button which will take the user to
        // the login screen once the user taps on the button
        loginNowBtnObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

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
                // The code was updates by me(alex) to work properly with our project
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Get the UID of the newly created user
                                    String uid = mAuth.getCurrentUser().getUid();
                                    // Creating a HashMap with the UID
                                    Map<String, Object> users = new HashMap<>();
                                    users.put("uid", uid);
                                    // Get a CollectionReference where you want to store the user data
                                    CollectionReference usersRef = db.collection("users");
                                    // Save the user to the database
                                    usersRef.document(uid).set(users)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // If account creation succeed, display a message to the user.
                                                    Toast.makeText(Registration.this, "Account Successfully Created.",
                                                            Toast.LENGTH_SHORT).show();

                                                    // Start UserProfileCreation activity
                                                    Intent intent = new Intent(Registration.this, UserProfileCreation.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // If account creation fails, display a message to the user.
                                                    Toast.makeText(Registration.this, "Account Creation Failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Log.w("Registration", "Error adding document", e);
                                                }
                                            });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Registration.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // Log the exception
                                    Log.w("Registration", "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });
            }
        });
    }
}