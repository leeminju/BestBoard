package com.example.myboard.comment.controller;

import com.example.myboard.comment.dto.CommentRequestDto;
import com.example.myboard.comment.dto.CommentResponseDto;
import com.example.myboard.comment.service.CommentService;
import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CustomResponseEntity> createComment(@PathVariable Long postId, @RequestBody @Valid CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(postId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CustomResponseEntity(
                                "댓글 작성 완료",
                                HttpStatus.CREATED.value()
                        )
                );
    }

    //게시글에 해당되는 댓글 모두 보기
    @GetMapping("/posts/{postId}/comments")
    public Page<CommentResponseDto> getComments(@PathVariable Long postId,
                                                @RequestParam("page") int page,
                                                @RequestParam("size") int size,
                                                @RequestParam("sortBy") String sortBy,
                                                @RequestParam("isAsc") boolean isAsc) {
        return commentService.getComments(postId,page-1,size,sortBy,isAsc);
    }

    //댓글 1개 보기
    @GetMapping("/comments/{commentId}")
    public CommentResponseDto getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CustomResponseEntity> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "댓글 수정 완료",
                        HttpStatus.OK.value()
                )
        );
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CustomResponseEntity> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity(
                        "댓글 삭제 완료",
                        HttpStatus.OK.value()
                )
        );
    }
}
