package com.example.myboard.post.service;

import com.example.myboard.global.s3.S3Uploader;
import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.post.dto.PostRequestDto;
import com.example.myboard.post.dto.PostResponseDto;
import com.example.myboard.post.entity.Post;
import com.example.myboard.post.repository.PostRepository;
import com.example.myboard.user.entity.User;
import com.example.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final S3Uploader s3Uploader;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile image) throws IOException {
        Post post;
        if (image != null && !image.isEmpty()) {
            if (!image.getContentType().startsWith("image")) {
                throw new RestApiException(ErrorCode.NOT_IMAGE_FILE);
            }
            String url = s3Uploader.upload(image, "images");
            post = postRepository.save(new Post(requestDto, user, url));
        } else {
            post = postRepository.save(new Post(requestDto, user));
        }

        post.addUser(user);
        return new PostResponseDto(post);
    }

    @Override
    public Page<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return postRepository.getPostsWithPage(pageable).map(PostResponseDto::new);
    }

    @Override
    public List<PostResponseDto> searchPostsByKeyword(String keyword) {
        return postRepository.getPostsWithKeyword(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );
        return new PostResponseDto(post);
    }


    @Transactional
    @Override
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile image, boolean orginal) throws IOException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_MODIFY_POST);
        }

        if (image == null || image.getContentType() == null) {
            if (post.getImage() != null) {
                if (orginal) {
                    post.update(requestDto, post.getImage());//원래 이미지 유지
                } else {
                    post.update(requestDto, null);//원래 이미지 삭제
                }
            } else {
                post.update(requestDto, null);
            }
        } else {
            if (!image.getContentType().startsWith("image")) {
                throw new RestApiException(ErrorCode.NOT_IMAGE_FILE);
            }
            String url = s3Uploader.upload(image, "images");
            post.update(requestDto, url);
        }

        return new PostResponseDto(post);
    }

    @Override
    public void deletePost(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RestApiException(ErrorCode.NOT_FOUND_POST)
        );

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RestApiException(ErrorCode.CAN_NOT_DELETE_POST);
        }

        postRepository.delete(post);
    }


}
