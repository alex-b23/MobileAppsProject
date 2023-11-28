package com.example.mobileappsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileappsproject.Posts.Post;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class CreatePostFragment extends Fragment {
    private FirebaseDatabase PostsDataBase;
    private FirebaseAuth MAuth;
    private DatabaseReference PostRef;
    public CreatePostFragment() {
        MAuth = FirebaseAuth.getInstance();
        SetupDataBaseAndRef();
    }

    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();
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
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Here we grab the post button and setup the code to make a post
        Button newPostButton = view.findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // We grab the text from the post
                TextInputLayout contextText = getView().findViewById(R.id.contentTextInput);
                String context = contextText.getEditText().getText().toString();

                // We check to make sure the user hasn't been logged out / timed out
                // the text of the user post isn't null and isn't a length of 0
                // if it is, we let the user know through an error toast
                if(MAuth.getCurrentUser() == null || context == null || context.length() == 0)
                {
                    Toast.makeText(view.getContext(), "Unable to post!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // We fetch a new post id
                String postId = PostRef.push().getKey();
                // Create a basic post
                Post post = new Post(postId, MAuth.getUid(), context, 0);
                // Add it to the database
                PostRef.child(postId).setValue(post);
                // Navigate the user back to the global posts page
                Navigation.findNavController(v).navigate(R.id.postsFragment);
            }
        });
    }

    private void SetupDataBaseAndRef() {
        // Get a reference to the database
        PostsDataBase = FirebaseDatabase.getInstance();
        PostRef = PostsDataBase.getReference("posts");
    }
}