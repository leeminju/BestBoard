package com.example.myboard.user;

import com.example.myboard.global.annotation.Nickname;
import com.example.myboard.global.jwt.JwtUtil;
import com.example.myboard.global.response.CustomResponseEntity;
import com.example.myboard.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/users/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/users/signup/nickname")
    @ResponseBody
    public ResponseEntity<CustomResponseEntity> checkNickname(@RequestParam @Nickname String nickname) {
        userService.checkNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).
                body(
                        new CustomResponseEntity(
                                "사용 가능한 닉네임 입니다.",
                                HttpStatus.OK.value()
                        )
                );
    }


    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity<CustomResponseEntity> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).
                body(
                        new CustomResponseEntity(
                                "회원 가입 성공",
                                HttpStatus.CREATED.value()
                        )
                );
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public String getUsername(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser().getNickname();
    }

    @PostMapping("/users/login")
    public ResponseEntity<CustomResponseEntity> login(@Valid @RequestBody LoginRequestDto userRequestDto) {
        userService.login(userRequestDto);

        String token = jwtUtil.createToken(userRequestDto.getNickname());

        return ResponseEntity.ok()
                .header(JwtUtil.AUTHORIZATION_HEADER, token)
                .body(new CustomResponseEntity("로그인 성공", HttpStatus.OK.value()));
    }
}
