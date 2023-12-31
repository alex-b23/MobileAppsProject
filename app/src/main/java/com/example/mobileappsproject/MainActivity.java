package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {
    public SearchView ToolbarSearch;
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
    }

    // Inflate the menu to fit our toolbar design
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Find the search bar from the list
        MenuItem menuItem = menu.findItem(R.id.searchFriends);
        ToolbarSearch = (SearchView) menuItem.getActionView();
        ToolbarSearch.setQueryHint("Search for a friend");

        // When we click the search bar, we want the list of usernames to appear
        ToolbarSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(FragmentView).navigate(R.id.searchFragment);
            }
        });

        // When close the search bar, we want to return to the post fragment
        ToolbarSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Navigation.findNavController(FragmentView).navigate(R.id.postsFragment);
                return false;
            }
        });

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
        } else if(id == R.id.account) {
            // Navigate to User profile screen
            Bundle bundle = new Bundle();
            bundle.putString(UserProfile.USERID, mAuth.getUid());
            Navigation.findNavController(FragmentView).navigate(R.id.userProfile, bundle);
        } else if(id == R.id.settings) {
            // Navigate to Settings screen
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }
}