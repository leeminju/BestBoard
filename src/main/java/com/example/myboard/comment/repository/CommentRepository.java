package com.example.myboard.comment.repository;

import com.example.myboard.comment.entity.Comment;
import com.example.myboard.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
