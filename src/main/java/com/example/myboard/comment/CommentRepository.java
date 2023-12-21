package com.example.myboard.comment;

import com.example.myboard.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,CommentRepositoryCustom {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);

}
