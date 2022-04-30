package com.it.restfulwebservice.exception;


import com.it.restfulwebservice.user.execption.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
// HTTP  Status code
// 2XX -> OK
// 4XX -> Client 오류, 권한 or 메서드 존재 x
// 5xx -> Server 오류

/**
 *  컨트롤러 AOP 설정
 * ResponseEntityExceptionHandler 상속받는다.
 * requset.getDescription(false) 상세내용 전달 X  //uri=/users/100"
 * 컨트롤러에 해당 exception 발생시 아래 로직이 실행된다.
 *
 * MethodArgumentNotValidException  VAILD 유효성검사 실패시 생기는 오류
 *  handleMethodArgumentNotValid 메서드 오버라딩하여 처리
 */

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest requset){//WebRequest어디서 발생했는지
        //우리가 등록한 POJO 객체                                                                   //uri=/users/100"
        ExceptionResponse exceptionResponse
                =new ExceptionResponse(new Date(),ex.getMessage() , requset.getDescription(false));//언제 , 메세지 , 상세정보내용 X

            return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR); //500에러 서버에서 일반적인 에러
    }

    @ExceptionHandler(UserNotFoundException.class)  // 컨트롤러에 해당 UserNotFoundException 발생시 아래 로직이 실행된다.
    public final ResponseEntity<Object> handleUserNotFoundExceptions(Exception ex, WebRequest requset){
        //우리가 등록한 POJO 객체
        ExceptionResponse exceptionResponse
                =new ExceptionResponse(new Date(),ex.getMessage() , requset.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND); //404Not Found
    }

    //부모클래스의 메서드를 가져와서 사용할떄는 override를 사용하자.( 메서드 이름 잘못되었을때 오류가 뜸)

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //우리가 만든 Exception 핸들러 객체  (오류 발생시간 , 오류 메새지  , 결과값? )
         ExceptionResponse exceptionResponse =new ExceptionResponse(new Date()
                 ,"Validation Failed", ex.getBindingResult().toString());

        return new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    //메서드 인자타입과 들어온 타입이 다를경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMthodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException ex,WebRequest requset){//WebRequest어디서 발생했는지
        //우리가 등록한 POJO 객체                                                                   //uri=/users/100"
        ExceptionResponse exceptionResponse
                =new ExceptionResponse(new Date(),ex.getName()+"체크해주세요",ex.getMessage());//언제 , 메세지 , 부가적인 내용

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST); //404Not Found
    }


}
