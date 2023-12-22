package com.example.myboard.comment.repository;

import com.example.myboard.comment.entity.Comment;
import com.example.myboard.comment.entity.QComment;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> getCommentsByPostWithPage(Long postId, Pageable pageable) {
        QComment comment = QComment.comment;

        var query = jpaQueryFactory.selectFrom(comment)
                .where(comment.post.id.eq(postId))
                .offset(pageable.getOffset())// 조회하려고 하는 페이지가 과거일 수록 느려짐.
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort())
                        .stream().toArray(OrderSpecifier[]::new)
                );

        var comments = query.fetch();

        long totalSize = jpaQueryFactory.select(Wildcard.count)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetch().get(0);

        return PageableExecutionUtils.getPage(comments, pageable, () -> totalSize);
    }

    @Override
    public List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Comment.class, "comment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }
}
