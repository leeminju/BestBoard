package com.example.myboard.global.annotation;

import com.example.myboard.global.validator.NicknameValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NicknameValidator.class)
public @interface Nickname {
    String message() default "Username";

    Class[] groups() default {};

    Class[] payload() default {};
}
