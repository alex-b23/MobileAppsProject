package com.example.mobileappsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mobileappsproject.Posts.Post;
import com.example.mobileappsproject.Posts.PostAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPostActivity extends AppCompatActivity {

    private FirebaseDatabase PostsDataBase;
    private DatabaseReference PostRef;

    private TextInputLayout ContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        ContentText = findViewById(R.id.contentTextInput);
        SetupDataBaseAndRef();
    }

    private void SetupDataBaseAndRef() {
        // Get a reference to the database
        PostsDataBase = FirebaseDatabase.getInstance();
        PostRef = PostsDataBase.getReference("posts");
    }

    public void onClickPost(View view)
    {
        Intent intent = new Intent(NewPostActivity.this, PostsActivity.class);
        String postId = PostRef.push().getKey();
        Post post = new Post(postId, "Derek Brandt", "derekjbr", ContentText.getEditText().getText().toString(), 0);

        PostRef.child(postId).setValue(post);
        startActivity(intent);
    }
}