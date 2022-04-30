package com.it.restfulwebservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 *  error 발생시 각각 명시하지 않고 하나의 객체를 이용하여 공통으로 처리후 프론트로 반환
 */
@Data
@AllArgsConstructor //모든 필드를 가지는 생성자
@NoArgsConstructor //디폴트 생성자
public class ExceptionResponse {
        private  Date timestamp; //에러발생시간
        private  String message; //에러메세지
        private  String details; //상세메세지
}
