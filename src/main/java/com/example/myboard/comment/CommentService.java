package com.example.myboard.comment;

import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.post.Post;
import com.example.myboard.post.PostRepository;
import com.example.myboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, post));

        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        List<Comment> commentList = commentRepository.findByPostOrderByCreatedAtDesc(post);//댓글 작성일 기준 내림차순 정렬
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }

    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_COMMENT)
        );

        return new CommentResponseDto(comment);
    }

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