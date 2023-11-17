package com.example.mobileappsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class PostsActivity extends AppCompatActivity {
    private PostAdapter RecyclerViewAdapter;
    private FirebaseDatabase PostsDataBase;
    private DatabaseReference PostRef;

    private List<Post> VisablePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        VisablePosts = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.postsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter = new PostAdapter(this, VisablePosts);
        recyclerView.setAdapter(RecyclerViewAdapter);

        SetupDataBaseAndRef();
    }

    public void onClickPost(View view)
    {
        Intent intent = new Intent(PostsActivity.this, NewPostActivity.class);
        startActivity(intent);
    }

    private void SetupDataBaseAndRef()
    {
        // Get a reference to the database
        PostsDataBase = FirebaseDatabase.getInstance();
        PostRef = PostsDataBase.getReference("posts");

        // Add in a listener that will fetch the data on the cloud
        PostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Get Post object and do whatever you need to do with it
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        // If the post already exists, its we want to update it and not re-add it
                        boolean alreadyContainsPost = false;
                        for(int i = 0; i < VisablePosts.size() && !alreadyContainsPost; i++)
                        {
                            if(VisablePosts.get(i).PostID == post.PostID)
                            {
                                alreadyContainsPost = true;
                                VisablePosts.set(i, post);
                            }
                        }

                        // If the post doesn't already exist, we want to add it to the recycler view.
                        if(!alreadyContainsPost)
                            VisablePosts.add(post);
                    }
                }

                // Tell the Recycler View that some of the data has changed and that it needs updating.
                RecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void TestDateBase()
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("posts");
        String postId = myRef.push().getKey();

        Post post = new Post(postId, "Derek Brandt", "derekjbr", "Hey guys I'm testing a new post!", 0);
        post.AddReply("derekjbr","Wow this is cool");

        myRef.child(postId).setValue(post);
    }
}