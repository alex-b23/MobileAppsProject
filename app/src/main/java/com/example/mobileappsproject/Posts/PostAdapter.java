package com.example.mobileappsproject.Posts;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappsproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private LayoutInflater Inflator;
    private OnClickListener onClickListener;
    private List<Post> Posts;

    private FirebaseFirestore Database = FirebaseFirestore.getInstance();

    public PostAdapter(Context context, List<Post> posts)
    {
        Inflator = LayoutInflater.from(context);
        Posts = posts;
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = Inflator.inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = Posts.get(Posts.size() - position - 1);
        holder.CaptionText.setText(post.Content);

        DocumentReference docRef = Database.collection("users").document(post.UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.DisplayNameText.setText((String)document.getData().getOrDefault("DisplayName", "Error"));
                        holder.UserNameText.setText("@" + (String)document.getData().getOrDefault("Username", "Error"));
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, post);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Post model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView DisplayNameText;
        TextView UserNameText;
        TextView CaptionText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DisplayNameText = itemView.findViewById(R.id.postCardDisplayName);
            UserNameText = itemView.findViewById(R.id.postCardUsername);
            CaptionText = itemView.findViewById(R.id.postCardCaption);
        }
    }
}
