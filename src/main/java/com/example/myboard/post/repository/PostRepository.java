package com.example.myboard.post.repository;

import com.example.myboard.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryCustom {
    void deleteByModifiedAtBefore(LocalDateTime before90Days);
}
