package com.example.mobileappsproject.Posts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    }

    @Override
    public int getItemCount() {
        return Replies.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
