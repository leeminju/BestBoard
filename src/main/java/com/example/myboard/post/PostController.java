package com.example.myboard.post;

import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "post 검증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<CustomResponseEntity> createPost(@RequestBody @Valid PostRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomResponseEntity(
                        "게시글 작성 완료",
                        HttpStatus.CREATED.value()
                )
        );
    }


    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/posts/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<CustomResponseEntity> updatePost(@PathVariable Long id,
                                                           @RequestBody @Valid PostRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "게시글 수정 완료",
                        HttpStatus.OK.value()
                )
        );
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<CustomResponseEntity> deletePost(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "게시글 삭제 완료",
                        HttpStatus.OK.value()
                )
        );
    }
}
