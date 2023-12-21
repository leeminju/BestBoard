package com.example.myboard.like;

import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    //좋아요
    @PostMapping("/{post_id}")
    public ResponseEntity<Boolean> likePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Boolean result = likeService.likePost(post_id, userDetails.getUser());
        if (result) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    //좋아요 했는지 확인
    @GetMapping("/{post_id}")
    public boolean getLikePost(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.getLikePost(post_id, userDetails.getUser());
    }
}
