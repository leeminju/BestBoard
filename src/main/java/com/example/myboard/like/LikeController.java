package com.example.myboard.like;

import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    //좋아요
    @PostMapping("/{post_id}")
    public ResponseEntity<CustomResponseEntity> likePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likePost(post_id,userDetails.getUser());
        return new ResponseEntity<>(
                new CustomResponseEntity(
                        "게시글 좋아요",
                        HttpStatus.CREATED.value()
                ),
                HttpStatus.CREATED
        );
    }
}
