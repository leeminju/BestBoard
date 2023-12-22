package com.example.myboard.like.repository;

import com.example.myboard.like.entity.Like;
import com.example.myboard.post.entity.Post;
import com.example.myboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);
}
