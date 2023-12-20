package com.example.myboard.global.validator;

import com.example.myboard.global.annotation.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.MessageFormat;

public class NicknameValidator implements ConstraintValidator<Nickname, String> { // 1
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 10;
    private static final String regexNickname = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{" + MIN_SIZE + "," + MAX_SIZE + "}+$";


    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        boolean isValidUsername = nickname.matches(regexNickname);
        if (nickname.length() < MIN_SIZE || nickname.length() > MAX_SIZE) {
            isValidUsername = false;
        }

        if (!isValidUsername) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영어 대소문자를 포함한 닉네임을 입력해주세요.",
                            MIN_SIZE,
                            MAX_SIZE)).addConstraintViolation();
        }
        return isValidUsername;
    }
}