package com.example.myboard.post.service;

import com.example.myboard.post.dto.PostRequestDto;
import com.example.myboard.post.dto.PostResponseDto;
import com.example.myboard.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile image) throws IOException;

    public Page<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc);

    public List<PostResponseDto> searchPostsByKeyword(String keyword);

    public PostResponseDto getPostById(Long id);

    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile image, boolean orginal) throws IOException;

    public void deletePost(Long id, User user);
}
