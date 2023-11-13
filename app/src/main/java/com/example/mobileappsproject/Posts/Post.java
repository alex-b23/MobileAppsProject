package com.example.mobileappsproject.Posts;

import java.util.ArrayList;
import java.util.List;

public class Post {
    public String DisplayName;
    public String Username;
    public String Content;
    public int LikeCount;

    public List<Reply> Replies;

    public Post(String displayName, String username, String content, int likeCount) {
        DisplayName = displayName;
        Username = username;
        Content = content;
        LikeCount = likeCount;
        Replies = new ArrayList<Reply>();
    }

    private static class Reply {
        private String Username;
        private String Content;
    }
}


