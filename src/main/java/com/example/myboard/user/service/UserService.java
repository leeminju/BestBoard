package com.example.myboard.user.service;

import com.example.myboard.user.dto.LoginRequestDto;
import com.example.myboard.user.dto.SignupRequestDto;

public interface UserService {
    public void checkNickname(String username);
    public String signup(SignupRequestDto requestDto);
    public boolean login(LoginRequestDto loginRequestDto);
}
