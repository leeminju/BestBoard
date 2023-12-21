package com.example.myboard.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryCustom {
    void deleteByModifiedAtBefore(LocalDateTime before90Days);
}
