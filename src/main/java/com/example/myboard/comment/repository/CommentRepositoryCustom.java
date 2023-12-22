package com.example.myboard.comment.repository;

import com.example.myboard.comment.entity.Comment;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
    Page<Comment> getCommentsByPostWithPage(Long postId, Pageable pageable);
    List<OrderSpecifier> getOrderSpecifier(Sort sort);
}
