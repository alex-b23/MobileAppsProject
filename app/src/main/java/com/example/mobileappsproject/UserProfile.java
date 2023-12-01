package com.example.mobileappsproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends Fragment {

    public static final String USERID = "userid";
    private String UserId;

    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView bioTextView;
    private FirebaseFirestore db;
    private PostAdapter RecyclerViewAdapter;
    private FirebaseDatabase PostsDataBase;
    private DatabaseReference PostRef;
    private List<Post> VisablePosts;


    public UserProfile() {
        // Required empty public constructor
    }

    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VisablePosts = new ArrayList<>();
        if (getArguments() != null) {
            UserId = getArguments().getString(USERID);
        }
        SetupDataBaseAndRef();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nameTextView = view.findViewById(R.id.name);
        usernameTextView = view.findViewById(R.id.username);
        bioTextView = view.findViewById(R.id.bio);

        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserId = getArguments().getString(USERID);

        if(UserId == null)
            return;

        db.collection("users").document(UserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    nameTextView.setText(document.getString("DisplayName"));
                    usernameTextView.setText(document.getString("Username"));
                    bioTextView.setText(document.getString("Bio"));
                }
            }
        });

        // Fetch the Recycler View and set the adapter to our custom post adapter that displays posts
        RecyclerView recyclerView = view.findViewById(R.id.usersPostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerViewAdapter = new PostAdapter(view.getContext(), VisablePosts);

        recyclerView.setAdapter(RecyclerViewAdapter);
    }

    // This is the code for setting up the Realtime Database
    private void SetupDataBaseAndRef()
    {
        Log.d("Search", "Users with " + UserId);
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
                        if(post.UserID.compareTo(UserId) != 0)
                            continue;
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
}