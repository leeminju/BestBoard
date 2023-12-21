package com.example.myboard.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "제목을 입력해 주세요!")
    @Size(min = 1, max = 500, message = "1자 이상 500자 이하의 제목을 입력해 주세요!")
    String title;
    @NotBlank(message = "내용을 입력해 주세요!")
    @Size(min = 1, max = 5000, message = "1자 이상 5000자 이하의 내용을 입력해 주세요!")
    String contents;
}
