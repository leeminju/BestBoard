package com.example.myboard.like.service;

import com.example.myboard.user.entity.User;

public interface LikeService {
    public boolean likePost(Long postId, User user);

    public boolean getLikePost(Long postId, User user);

}
