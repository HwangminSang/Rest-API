package com.it.restfulwebservice.user.execption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP  Status code
// 2XX -> OK
// 4XX -> Client 오류, 권한 or 메서드 존재 x
// 5xx -> Server 오류

@ResponseStatus(HttpStatus.NOT_FOUND) // 기존 5XX -> 404Not Found
public class UserNotFoundException extends  RuntimeException { //실행시 발생

    public UserNotFoundException(String message){

        super(message);

    }
}
