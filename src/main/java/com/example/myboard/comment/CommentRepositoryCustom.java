package com.example.myboard.comment;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepositoryCustom {
    Page<Comment> getCommentsByPostWithPage(Long postId, Pageable pageable);
    List<OrderSpecifier> getOrderSpecifier(Sort sort);
}
