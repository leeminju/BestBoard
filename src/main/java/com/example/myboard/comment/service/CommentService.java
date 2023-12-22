package com.example.myboard.comment.service;

import com.example.myboard.comment.dto.CommentRequestDto;
import com.example.myboard.comment.dto.CommentResponseDto;
import com.example.myboard.user.entity.User;
import org.springframework.data.domain.Page;

public interface CommentService {
    //특정 게시글에 댓글 생성 
    CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user);

    //특정 게시글의 댓글 목록 조회(페이징과 동적정렬 사용) 
    public Page<CommentResponseDto> getComments(Long postId, int page, int size, String sortBy, boolean isAsc);

    //댓글 1개 조회
    public CommentResponseDto getComment(Long commentId);

    //댓글 수정
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user);

    //댓글 삭제
    public void deleteComment(Long commentId, User user);
}
