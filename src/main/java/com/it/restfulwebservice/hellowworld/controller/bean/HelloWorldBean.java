package com.it.restfulwebservice.hellowworld.controller.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //SET GET TOSTRING HASHCODE등 다있다.
@AllArgsConstructor // 맴버 필드의 생성자를 만들어준다.
public class HelloWorldBean {
    private String message;

}
