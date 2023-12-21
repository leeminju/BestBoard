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

    public boolean likePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            Like alreadyLike = like.get();
            likeRepository.delete(alreadyLike);//있던 좋아요 삭제
            return false;
        }
        Like new_like = new Like(post, user);
        likeRepository.save(new_like);
        return true;
    }

    public boolean getLikePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            return true;
        }
        return false;
    }
}
