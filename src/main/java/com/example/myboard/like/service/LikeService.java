package com.example.myboard.like.service;

import com.example.myboard.user.entity.User;

public interface LikeService {
    //게시글 좋아요/취소
    public boolean likePost(Long postId, User user);

    //사용자가 게시글 좋아요했는지 확인
    public boolean getLikePost(Long postId, User user);

}
