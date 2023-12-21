package com.example.myboard.like;

import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.post.Post;
import com.example.myboard.post.PostRepository;
import com.example.myboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public void likePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            throw new RestApiException(ErrorCode.ALREADY_LIKED_POST);
        }
        Like new_like = new Like(post, user);
        likeRepository.save(new_like);
    }
}
