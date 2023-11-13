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
    List<String> Usernames;

    public PostAdapter(Context context, List<String> usernames)
    {
        Inflator = LayoutInflater.from(context);
        Usernames = usernames;
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = Inflator.inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        String username = Usernames.get(position);
        holder.UserNameText.setText(username);
    }

    @Override
    public int getItemCount() {
        return Usernames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView UserNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserNameText = itemView.findViewById(R.id.postUserName);
        }
    }
}
