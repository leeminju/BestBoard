package com.example.myboard.post.controller;

import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import com.example.myboard.post.dto.PostRequestDto;
import com.example.myboard.post.dto.PostResponseDto;
import com.example.myboard.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "post 검증")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<CustomResponseEntity> createPost(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart("data") @Valid PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        postService.createPost(requestDto, userDetails.getUser(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomResponseEntity(
                        "게시글 작성 완료",
                        HttpStatus.CREATED.value()
                )
        );
    }

    //제목 또는 내용에서 키워드 검색
    @GetMapping("/posts/search/{keyword}")
    public List<PostResponseDto> searchPostsByKeyword(
            @PathVariable @Size(min = 2, max = 10, message = "2자이상 10자 이하의 검색어를 입력해주세요!")
            String keyword) {
        return postService.searchPostsByKeyword(keyword);
    }

    @GetMapping("/posts")
    public Page<PostResponseDto> getPosts(@RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam("sortBy") String sortBy,
                                          @RequestParam("isAsc") boolean isAsc
    ) {
        return postService.getPosts(page - 1, size, sortBy, isAsc);
    }

    @GetMapping("/posts/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/posts/{id}/{original}")
    public ResponseEntity<CustomResponseEntity> updatePost(@PathVariable Long id,
                                                           @PathVariable boolean original,
                                                           @RequestParam(value = "image", required = false) MultipartFile image,
                                                           @RequestPart("data") @Valid PostRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        postService.updatePost(id, requestDto, userDetails.getUser(), image, original);
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
