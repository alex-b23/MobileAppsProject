package com.example.mobileappsproject.Posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappsproject.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    LayoutInflater Inflator;
    List<Post> Posts;

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

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = Posts.get(position);
        holder.DisplayNameText.setText(post.DisplayName);
        holder.UserNameText.setText("@" + post.Username);
        holder.CaptionText.setText(post.Content);
    }

    @Override
    public int getItemCount() {
        return Posts.size();
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
