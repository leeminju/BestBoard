package com.example.myboard.post;

import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.user.User;
import com.example.myboard.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        post.addUser(user);
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findALLByOrderByCreatedAtDesc();
        return posts.stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPostById(Long id) {
        Post Post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );
        return new PostResponseDto(Post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post Post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        if (!Post.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_MODIFY_POST);
        }

        Post.update(requestDto);
        return new PostResponseDto(Post);
    }

    public void deletePost(Long id, User user) {
        Post Post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        if (!Post.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_DELETE_POST);
        }

        postRepository.delete(Post);
    }

}
