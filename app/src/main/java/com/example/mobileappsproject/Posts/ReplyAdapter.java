package com.example.mobileappsproject.Posts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappsproject.R;
import com.example.mobileappsproject.ReplyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>{
    private LayoutInflater Inflator;
    private List<Post.Reply> Replies;
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private FirebaseAuth MAuth = FirebaseAuth.getInstance();
    public ReplyAdapter(Context context, List<Post.Reply> replies)
    {
        Inflator = LayoutInflater.from(context);
        Replies = replies;
    }
    @NonNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = Inflator.inflate(R.layout.reply_layout, parent, false);
        return new ReplyAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ViewHolder holder, int position) {
        // Hide the reply until we have all of the information
        holder.itemView.setVisibility(View.GONE);
        // Fetch the last reply so newer replies go first
        Post.Reply reply = Replies.get(Replies.size() - 1 - position);
        holder.CaptionText.setText(reply.Content);

        // Fetch the User database, the post only has a link to the user id
        DocumentReference docRef = Database.collection("users").document(reply.UserID);

        // Once we successfully fetch the database, we want to update the post information
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.DisplayNameText.setText((String)document.getData().getOrDefault("DisplayName", "Error"));
                        holder.UsernameText.setText("@" + (String)document.getData().getOrDefault("Username", "Error"));
                        // Reveal the reply once we have fetched the repliers information from the user database
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Replies.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CaptionText;
        TextView DisplayNameText;
        TextView UsernameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CaptionText = itemView.findViewById(R.id.replyCardCaption);
            DisplayNameText = itemView.findViewById(R.id.replyCardDisplayName);
            UsernameText = itemView.findViewById(R.id.replyCardUsername);
        }
    }
}
