package com.example.myboard.comment.service;

import com.example.myboard.comment.dto.CommentRequestDto;
import com.example.myboard.comment.dto.CommentResponseDto;
import com.example.myboard.user.entity.User;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user);

    public Page<CommentResponseDto> getComments(Long postId, int page, int size, String sortBy, boolean isAsc);

    public CommentResponseDto getComment(Long commentId);

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user);

    public void deleteComment(Long commentId, User user);
}
