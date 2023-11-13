package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobileappsproject.Posts.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private PostAdapter RecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        List<String> usernames = GetTestUsernames();
        RecyclerView recyclerView = findViewById(R.id.PostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter = new PostAdapter(this, usernames);
        recyclerView.setAdapter(RecyclerViewAdapter);
    }

    private List<String> GetTestUsernames() {
        List<String> usernames = new ArrayList<String>();
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");
        usernames.add("user1");
        usernames.add("user2");
        usernames.add("user3");
        usernames.add("user4");
        usernames.add("user5");

        return usernames;
    }
}