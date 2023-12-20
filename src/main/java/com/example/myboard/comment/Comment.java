package com.example.myboard.comment;

import com.example.myboard.global.entity.Timestamped;
import com.example.myboard.post.Post;
import com.example.myboard.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.contents = requestDto.getContents();
        this.post = post;
        this.user = user;
    }

    public Comment(String contents, User user) {
        this.contents = contents;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void addPost(Post post0) {
        this.post = post;
    }
}
