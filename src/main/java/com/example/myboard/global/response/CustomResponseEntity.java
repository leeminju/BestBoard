package com.example.myboard.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponseEntity {
    private String responseMessage;
    private int statusCode;
}
