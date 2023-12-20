package com.example.myboard.comment;

import com.example.myboard.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
