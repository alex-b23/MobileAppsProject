package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private PostAdapter RecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        List<Post> posts = GetTestPosts();
        RecyclerView recyclerView = findViewById(R.id.PostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(RecyclerViewAdapter);
    }

    private List<Post> GetTestPosts() {
        List<Post> posts = new ArrayList<Post>();
        posts.add(new Post("Derek Brandt", "derekjbr", "Hey guys I'm testing a new post!", 0));
        return posts;
    }
}