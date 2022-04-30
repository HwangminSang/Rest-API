package com.it.restfulwebservice.hellowworld.controller;



import com.it.restfulwebservice.hellowworld.controller.bean.HelloWorldBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RequiredArgsConstructor
@RestController
public class HelloController {

        //메세지 반환 객체
      private final MessageSource messageSource;


    //Get
    // /hello-world (endpoint)
    //@RequestMapping(method=RequestMethod.GET , path="/hello-world")
    @GetMapping(path="/hello-world")
       public String helloWorld(){

                return "Hello World";
       }

    // alt + enter
    @GetMapping(path="/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        //{"message":"Hello World"}
        //자동으로 json 포멧을 변환해서 반환한다.
        return new HelloWorldBean("Hello World");
    }

        //@PathVariable 이용 가변데이터 처리
    @GetMapping(path="/hello-world-bean/path-variable/{name}") //이름이 같을경우 @뒤에 파라미터 이름 적을 필요 x 다를 경우 value="이름"
    public HelloWorldBean helloWorldPathBean(@PathVariable String name){ //가변데이터
        return new HelloWorldBean(String.format("Hello World, %s",name)); //%s 자리에 뒤의 name이 들어감
    }

        @GetMapping(path = "/hello-world-internationalized")  //해더에 Accept-Language 에 원하는 언어를 담아오지 않았을경우 우리가 설정한 default 한국어가 들어감.
    public String helloWorldInternationalized(
            @RequestHeader(name="Accept-Language" ,required = false) Locale locale){


            return messageSource.getMessage("greeting.message",null,locale);
        }

}
