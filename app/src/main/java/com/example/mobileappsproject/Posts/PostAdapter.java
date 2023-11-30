package com.example.mobileappsproject.Posts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappsproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private LayoutInflater Inflator;
    private OnClickListener onClickListener;
    private List<Post> Posts;
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private FirebaseAuth MAuth = FirebaseAuth.getInstance();
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
        // Hide the post to start, so we can reveal it once we have all the information
        holder.itemView.setVisibility(View.INVISIBLE);
        // Update so newer posts are on top of the feed
        Post post = Posts.get(Posts.size() - position - 1);
        // Set the caption from what was stored in the realtime database
        holder.CaptionText.setText(post.Content);

        // Update the reply button text to have the number of replies
        holder.ReplyButton.setText(post.NumReplies().toString());
        // Here we are setting the button click listen of the Reply button so it will transition the fragment view to the reply fragment
        holder.ReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UserId", post.UserID);
                bundle.putString("Caption", post.Content);
                bundle.putString("PostId", post.PostID);
                Navigation.findNavController(holder.itemView).navigate(R.id.replyFragment, bundle);
            }
        });

        // Update the like button to match the number of likes
        holder.LikeButton.setText(post.Likes().toString());
        // If the user has liked the post before, change the icon to a filled icon, if they havent it will be an empty icon
        if(post.HasUserLiked(MAuth.getUid()))
        {
            holder.LikeButton.setIcon(ContextCompat.getDrawable(holder.LikeButton.getContext(),R.drawable.baseline_thumb_up_24));
        } else {
            holder.LikeButton.setIcon(ContextCompat.getDrawable(holder.LikeButton.getContext(),R.drawable.outline_thumb_up_24));
        }

        // Add an onclick listener to the like button so the user can like a post
        holder.LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = MAuth.getUid();
                if(post.HasUserLiked(userId))
                    post.RemoveLikedUser(userId, true);
                else
                    post.AddLikedUser(MAuth.getUid(), true);
            }
        });
        // We add an onclick listener to the post to allow them to go to the reply
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, post);
                }
            }
        });

        // Fetch the User database, the post only has a link to the user id
        DocumentReference docRef = Database.collection("users").document(post.UserID);
        // Once we successfully fetch the database, we want to update the post information
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.DisplayNameText.setText((String)document.getData().getOrDefault("DisplayName", "Error"));
                        holder.UserNameText.setText("@" + (String)document.getData().getOrDefault("Username", "Error"));
                        // Once we have successfully retrieved all of the information with no error, we show the post to the user
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
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
        MaterialButton LikeButton;
        MaterialButton ReplyButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DisplayNameText = itemView.findViewById(R.id.replyPostName);
            UserNameText = itemView.findViewById(R.id.replyPostUsername);
            CaptionText = itemView.findViewById(R.id.replyPostCaption);
            LikeButton = itemView.findViewById(R.id.likeButton);
            ReplyButton = itemView.findViewById(R.id.replyButton);
        }
    }
}
