package com.example.myboard.post.repository;

import com.example.myboard.post.entity.Post;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryCustom {
    List<Post> getPostsWithKeyword(String keyword);

    Page<Post> getPostsWithPage(Pageable pageable);

    List<OrderSpecifier> getOrderSpecifier(Sort sort);

}
