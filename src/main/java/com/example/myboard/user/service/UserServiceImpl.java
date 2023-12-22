package com.example.myboard.user.service;

import com.example.myboard.global.exception.ErrorCode;
import com.example.myboard.global.exception.RestApiException;
import com.example.myboard.user.dto.LoginRequestDto;
import com.example.myboard.user.dto.SignupRequestDto;
import com.example.myboard.user.entity.User;
import com.example.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void checkNickname(String username) {
        Optional<User> checkNickname = userRepository.findByNickname(username);
        if (checkNickname.isPresent()) {
            throw new RestApiException(ErrorCode.IS_DUPLICATE_NICKNAME);
        }
    }
    @Override
    public String signup(SignupRequestDto requestDto) {

        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 중복 확인 안됨
        if (!requestDto.isCheck()) {
            throw new RestApiException(ErrorCode.NOT_NICKNAME_DUPLICATE_CHECK);
        }

        //비밀번호 확인 검사
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new RestApiException(ErrorCode.NOT_EQUALS_PASSWORD);
        }

        // 비밀번호 중복 확인
        if (requestDto.getPassword().contains(nickname)) {
            throw new RestApiException(ErrorCode.NOT_CONTAINS_NICKNAME_IN_PASSWORD);
        }


        // 사용자 등록
        User user = new User(nickname, password);
        User savedUser = userRepository.save(user);


        return savedUser.getNickname();
    }
    @Override
    public boolean login(LoginRequestDto loginRequestDto) {
        String nickname = loginRequestDto.getNickname();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RestApiException(ErrorCode.NOT_FOUND_USER);
        }

        return true;
    }


}
