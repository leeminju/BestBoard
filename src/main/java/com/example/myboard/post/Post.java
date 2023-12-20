package com.example.myboard.post;


import com.example.myboard.comment.Comment;
import com.example.myboard.global.entity.Timestamped;
import com.example.myboard.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //게시글 삭제되면 댓글도 같이 삭제
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto todoRequestDto) {
        super();
        this.title = todoRequestDto.getTitle();
        this.contents = todoRequestDto.getContents();
    }

    public void addCommentList(Comment comment) {
        commentList.add(comment);
        comment.addPost(this);// 외래 키(연관 관계) 설정
    }

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addUser(User user) {
        this.user = user;
    }
}
