package com.example.mobileappsproject.Posts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    protected static SimpleDateFormat DateFormat = new SimpleDateFormat("hh:mm:dd:MM:yyyy");
    public String PostID;
    public String UserID;
    public String Content;

    public String Timestamp;
    public List<String> LikedUsers;

    public List<Reply> Replies;

    public Post(String postId, String userID, String content, int likeCount) {
        PostID = postId;
        UserID = userID;
        Content = content;
        Timestamp = DateFormat.format(new Date());
        Replies = new ArrayList<>();
        LikedUsers = new ArrayList<>();
    }

    public Post() {}
    public int Likes()
    {
        return LikedUsers.size();
    }

    public void AddLikedUser(String userId)
    {
        if(!LikedUsers.contains(userId))
            LikedUsers.add(userId);
    }
    public void AddReply(String userID, String content)
    {
        Replies.add(new Reply(userID, content));
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


