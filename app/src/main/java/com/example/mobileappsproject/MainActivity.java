package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FragmentContainerView FragmentView;
    private Button signOut;
    private FirebaseAuth mAuth;
    private Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentView = findViewById(R.id.fragmentContainerView);

        // Here we setup the toolbar, this will allow the onOptionsItemSelected function to be called
        // when clicking on buttons in the toolbar
        Toolbar toolbar = findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);

        // get the signout button and setup the authentication instanec
        mAuth = FirebaseAuth.getInstance();
        signOut = findViewById(R.id.signOut);
        profileBtn = findViewById(R.id.profile);

        profileBtn.setOnClickListener(view -> {
            UserProfile userProfileFragment = new UserProfile();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, userProfileFragment)
                    .commit();
        });

        signOut.setOnClickListener(new View.OnClickListener() {
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
    }

    // Inflate the menu to fit our toolbar design
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        // Here we control the navigation. We don't want to do transitions as we want each page
        // to be accessible by any other page.
        if(id == R.id.homeMenu)
        {
            // Navigate to the list of all global posts
            Navigation.findNavController(FragmentView).navigate(R.id.postsFragment);
        } else if(id == R.id.newPostMenu)
        {
            // Navigate to the create post screen
            Navigation.findNavController(FragmentView).navigate(R.id.createPostFragment);
        }

        return true;
    }
}