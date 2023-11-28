package com.example.mobileappsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.ReplyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplyFragment extends Fragment {
    private ReplyAdapter RecyclerViewAdapter;
    private Post PostReference;
    private List<Post.Reply> Replies;
    private FirebaseDatabase PostsDataBase;
    private DatabaseReference PostRef;
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private FirebaseAuth MAuth = FirebaseAuth.getInstance();

    public ReplyFragment() {
        PostReference = new Post();
        Replies = new ArrayList<>();
    }
    public static ReplyFragment newInstance() {
        ReplyFragment fragment = new ReplyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reply, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch the bundle arguments
        String userId = getArguments().getString("UserId");
        String caption = getArguments().getString("Caption");
        String postId = getArguments().getString("PostId");

        // Find the UI elements
        TextView displayNameTextView = view.findViewById(R.id.replyPostName);
        TextView usernameTextView = view.findViewById(R.id.replyPostUsername);
        TextView captionTextview = view.findViewById(R.id.replyPostCaption);

        // Fetch the User database, the post only has a link to the user id
        DocumentReference docRef = Database.collection("users").document(userId);
        // Once we successfully fetch the database, we want to update the post information
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        displayNameTextView.setText((String)document.getData().getOrDefault("DisplayName", "Error"));
                        usernameTextView.setText("@" + (String)document.getData().getOrDefault("Username", "Error"));
                    }
                }
            }
        });
        captionTextview.setText(caption);

        // Fetch the Recycler View and set the adapter to our custom post adapter that displays replies
        RecyclerView recyclerView = view.findViewById(R.id.replyRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerViewAdapter = new ReplyAdapter(view.getContext(), Replies);
        recyclerView.setAdapter(RecyclerViewAdapter);

        //Finally We want to set up the database
        SetupDataBaseAndRef(postId);

        // Setup reply to post button
        Button replyToPostButton = view.findViewById(R.id.replyToPostButton);
        TextInputLayout content = view.findViewById(R.id.contentTextInput);
        replyToPostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String context = content.getEditText().getText().toString();

                // We check to make sure the user hasn't been logged out / timed out
                // the text of the user reply isn't null and isn't a length of 0
                // if it is, we let the user know through an error toast
                if(MAuth.getCurrentUser() == null || context == null || context.length() == 0)
                {
                    Toast.makeText(view.getContext(), "Unable to post!", Toast.LENGTH_SHORT).show();
                    return;
                }
                PostReference.AddReply(MAuth.getUid(), context, true);
                // Clear the reply fields text
                content.getEditText().setText("");
            }
        });
    }

    // This is the code for setting up the Realtime
    private void SetupDataBaseAndRef(String postId)
    {
        // Get a reference to the database
        PostsDataBase = FirebaseDatabase.getInstance();
        // We make it specific to the post we want to view replies for post id
        PostRef = PostsDataBase.getReference("posts").child(postId);

        // Add in a listener that will fetch the data on the cloud
        PostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostReference = dataSnapshot.getValue(Post.class);
                // If the post as some replies, we want to add it to our reply list
                if(PostReference.Replies != null) {
                    Replies.clear();
                    for(Post.Reply reply : PostReference.Replies) {
                        Replies.add(reply);
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
}