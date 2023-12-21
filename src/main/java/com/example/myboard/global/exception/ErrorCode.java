package com.example.myboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    NOT_NICKNAME_DUPLICATE_CHECK("닉네임 중복 확인이 필요합니다.", HttpStatus.BAD_REQUEST),
    IS_DUPLICATE_NICKNAME("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_CONTAINS_NICKNAME_IN_PASSWORD("비밀번호에 닉네임과 같은 값이 포함될 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_IMAGE_FILE("이미지 파일 형식이 아닙니다.", HttpStatus.BAD_REQUEST),
    ALREADY_LIKED_POST("이미 좋아요한 게시글 입니다.",HttpStatus.BAD_REQUEST),

    //NOT_FOUND
    NOT_FOUND_USER("닉네임 또는 패스워드를 확인해 주세요!", HttpStatus.NOT_FOUND),
    NOT_FOUND_POST("게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMENT("해당 댓글 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    //FORBIDDEN
    CAN_NOT_MODIFY_POST("작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_DELETE_POST("작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_MODIFY_COMMENT("댓글 작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    CAN_NOT_DELETE_COMMENT("댓글 작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    ;

    private String errorMessage;
    private HttpStatus statusCode;
}