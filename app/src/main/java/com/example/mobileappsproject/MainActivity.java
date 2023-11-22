package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FragmentContainerView FragmentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentView = findViewById(R.id.fragmentContainerView);

        Toolbar toolbar = findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);
    }

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
        if(id == R.id.homeMenu)
        {
            Navigation.findNavController(FragmentView).navigate(R.id.postsFragment);
        } else if(id == R.id.newPostMenu)
        {
            Navigation.findNavController(FragmentView).navigate(R.id.createPostFragment);
        }

        return true;
    }
}