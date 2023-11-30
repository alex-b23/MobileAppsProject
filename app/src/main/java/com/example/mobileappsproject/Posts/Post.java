package com.example.mobileappsproject.Posts;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    protected static SimpleDateFormat DateFormat = new SimpleDateFormat("hh:mm:dd:MM:yyyy");
    private static FirebaseDatabase PostsDataBase;
    private static DatabaseReference PostRef;
    public String PostID;
    public String UserID;
    public String Content;
    public String Timestamp;
    public List<String> LikedUsers;
    public List<Reply> Replies;
    public Post(String postId, String userID, String content) {
        PostID = postId;
        UserID = userID;
        Content = content;
        Timestamp = DateFormat.format(new Date());
        Replies = new ArrayList<>();
        LikedUsers = new ArrayList<>();
    }
    public Post() {
        Replies = new ArrayList<>();
        LikedUsers = new ArrayList<>();
    }
    // Get the number of likes a post has
    public Integer Likes()
    {
        if(LikedUsers == null)
            return 0;
        return LikedUsers.size();
    }
    // Get the number of replies a post has
    public Integer NumReplies()
    {
        if(Replies == null)
            return 0;
        else
            return Replies.size();
    }
    // Test to see if the user has liked a post
    public boolean HasUserLiked(String userId)
    {
        if(LikedUsers == null)
            return false;
        return LikedUsers.contains(userId);
    }
    // This code will add a user to the liked users and then update the database automatically
    public void AddLikedUser(String userId, boolean updateDataBase)
    {
        if(LikedUsers == null)
            LikedUsers = new ArrayList<>();

        if(!LikedUsers.contains(userId))
            LikedUsers.add(userId);

        if(updateDataBase) {
            SetupDataBaseAndRef();
            PostRef.child(PostID).setValue(this);
        }
    }
    // This code will remove a user from the liked users and then update the database automatically
    public void RemoveLikedUser(String userId, boolean updateDataBase)
    {
        if(LikedUsers == null)
            LikedUsers = new ArrayList<>();

        if(LikedUsers.contains(userId))
            LikedUsers.remove(userId);

        if(updateDataBase) {
            SetupDataBaseAndRef();
            PostRef.child(PostID).setValue(this);
        }
    }
    // This code adds a reply to a user posts and then updates the database automatically
    public void AddReply(String userID, String content, boolean updateDataBase)
    {
        if(Replies == null)
            Replies = new ArrayList<>();
        Replies.add(new Reply(userID, content));

        if(updateDataBase) {
            SetupDataBaseAndRef();
            PostRef.child(PostID).setValue(this);
        }
    }
    private void SetupDataBaseAndRef() {
        if(PostsDataBase == null || PostRef == null)
        {
            PostsDataBase = FirebaseDatabase.getInstance();
            PostRef = PostsDataBase.getReference("posts");
        }
    }

    public static class Reply {
        public String UserID;
        public String Content;
        public Reply(String userId, String content) {
            UserID = userId;
            Content = content;
        }
        public Reply() {}
    }
}


