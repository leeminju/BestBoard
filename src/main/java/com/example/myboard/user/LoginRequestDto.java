package com.example.myboard.user;

import com.example.myboard.global.annotation.Nickname;
import com.example.myboard.global.annotation.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @Nickname
    private String nickname;
    @Password
    private String password;
}
