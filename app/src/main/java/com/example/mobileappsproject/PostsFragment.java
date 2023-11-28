package com.example.mobileappsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private PostAdapter RecyclerViewAdapter;
    private FirebaseDatabase PostsDataBase;
    private DatabaseReference PostRef;
    private List<Post> VisablePosts;

    // When creating this fragment, we want to initialize the posts array list and setup the run the database setup code
    public PostsFragment() {
        VisablePosts = new ArrayList<>();
    }

    public static PostsFragment newInstance() {
        PostsFragment fragment = new PostsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupDataBaseAndRef();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Fetch the Recycler View and set the adapter to our custom post adapter that displays posts
        RecyclerView recyclerView = view.findViewById(R.id.postsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerViewAdapter = new PostAdapter(view.getContext(), VisablePosts);

        // We then set the onclick method which will handle clicking on a post and redirecting them to the reply section
        RecyclerViewAdapter.setOnClickListener(new PostAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Post post) {
                Toast.makeText(getContext(), "Clicked on post\n" + post.PostID, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(RecyclerViewAdapter);

    }

    // This is the code for setting up the Realtime Database
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
                            // If we find a post that is already in the recycler view, we stop the for loop
                            // update the post in the list and update the singular item in the recycler view
                            if(VisablePosts.get(i).PostID == post.PostID)
                            {
                                alreadyContainsPost = true;
                                VisablePosts.set(i, post);
                                RecyclerViewAdapter.notifyItemChanged(i);
                            }
                        }

                        // If the post doesn't already exist, we want to add it to the recycler view
                        // and update the last item in the RecyclerView
                        if(!alreadyContainsPost) {
                            VisablePosts.add(post);
                            RecyclerViewAdapter.notifyItemChanged(VisablePosts.size() - 1);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);

    }
}