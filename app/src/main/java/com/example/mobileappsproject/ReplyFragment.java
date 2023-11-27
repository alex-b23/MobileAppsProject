package com.example.mobileappsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.ReplyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReplyFragment extends Fragment {
    private ReplyAdapter RecyclerViewAdapter;
    private List<Post.Reply> VisableReplies;

    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    public ReplyFragment() {
        VisableReplies = new ArrayList<>();
    }
    public static ReplyFragment newInstance(String param1, String param2) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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

        VisableReplies.add(new Post.Reply("test", "I am replying"));
        RecyclerViewAdapter = new ReplyAdapter(view.getContext(), VisableReplies);
        recyclerView.setAdapter(RecyclerViewAdapter);
    }
}