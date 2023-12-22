package com.example.myboard.like.service;

import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.like.entity.Like;
import com.example.myboard.like.repository.LikeRepository;
import com.example.myboard.post.entity.Post;
import com.example.myboard.post.repository.PostRepository;
import com.example.myboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
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

    @Override
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
