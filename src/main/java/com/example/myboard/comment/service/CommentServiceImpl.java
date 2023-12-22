package com.example.myboard.comment.service;

import com.example.myboard.comment.dto.CommentRequestDto;
import com.example.myboard.comment.dto.CommentResponseDto;
import com.example.myboard.comment.entity.Comment;
import com.example.myboard.comment.repository.CommentRepository;
import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.post.entity.Post;
import com.example.myboard.post.repository.PostRepository;
import com.example.myboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, post));

        return new CommentResponseDto(comment);
    }

    @Override
    public Page<CommentResponseDto> getComments(Long postId, int page, int size, String sortBy, boolean isAsc) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return commentRepository.getCommentsByPostWithPage(postId, pageable).map(CommentResponseDto::new);
    }

    @Override
    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_MODIFY_COMMENT);
        }

        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_DELETE_COMMENT);
        }

        commentRepository.delete(comment);
    }
}
