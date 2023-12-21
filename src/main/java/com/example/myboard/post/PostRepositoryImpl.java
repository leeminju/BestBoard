package com.example.myboard.post;

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

@Slf4j(topic = "레포지토리")
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPostsWithKeyword(String keyword) {
        QPost post = QPost.post;
        return jpaQueryFactory.selectFrom(post)
                .where(post.contents.contains(keyword)
                        .or(post.title.contains(keyword))
                ).fetch();

    }

    @Override
    public Page<Post> getPostsWithPage(Pageable pageable) {
        QPost post = QPost.post;

        var query = jpaQueryFactory.selectFrom(post)
                .offset(pageable.getOffset())// 조회하려고 하는 페이지가 과거일 수록 느려짐.
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort())
                        .stream().toArray(OrderSpecifier[]::new)
                );

        var posts = query.fetch();
        long totalSize = jpaQueryFactory.select(Wildcard.count)
                .from(post)
                .fetch().get(0);
         log.info("전체 사이즈"+totalSize);

        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
    }

    @Override
    public List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            System.out.println("prop : " + prop);
            PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
            System.out.println("orderByExpression.get(prop) = " + orderByExpression.get(prop));
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }
}
