package com.example.myboard.user.service;

import com.example.myboard.user.dto.LoginRequestDto;
import com.example.myboard.user.dto.SignupRequestDto;

public interface UserService {
    //닉네임 중복확인
    public void checkNickname(String username);

    //회원가입
    public String signup(SignupRequestDto requestDto);

    //로그인
    public boolean login(LoginRequestDto loginRequestDto);
}
