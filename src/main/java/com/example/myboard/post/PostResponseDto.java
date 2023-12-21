package com.example.myboard.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String nickname;
    private String contents;
    private String image;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean finished;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.nickname = post.getUser().getNickname();
        this.image = post.getImage();
        this.likeCount = post.getLikeList().size();
        this.commentCount = post.getCommentList().size();
    }
}
