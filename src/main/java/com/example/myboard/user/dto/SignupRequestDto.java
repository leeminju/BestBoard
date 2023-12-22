package com.example.myboard.user.dto;

import com.example.myboard.global.annotation.Nickname;
import com.example.myboard.global.annotation.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    @Nickname
    private String nickname;
    private boolean check;
    @Password
    private String password;
    private String confirmPassword;
}