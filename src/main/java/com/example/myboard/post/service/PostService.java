package com.example.myboard.post.service;

import com.example.myboard.post.dto.PostRequestDto;
import com.example.myboard.post.dto.PostResponseDto;
import com.example.myboard.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    //게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile image) throws IOException;

    //전체 게시글 조회(페이징과 동적정렬 사용)
    public Page<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc);

    //제목이나 내용에서 키워드를 가진 게시글 찾기
    public List<PostResponseDto> searchPostsByKeyword(String keyword);

    //postId로 게시글 1개 조회
    public PostResponseDto getPostById(Long id);

    //게시글 수정
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile image, boolean orginal) throws IOException;

    //게시글 삭제
    public void deletePost(Long id, User user);
}
