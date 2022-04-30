package com.it.restfulwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class FirstprojectApplication {

    public static void main(String[] args) {

        SpringApplication.run(FirstprojectApplication.class, args
        );
    }
    
    //다국어 지원을 위해 bean등록
    //초기화시 사용할수있다.
    //메모리에 올라간다.
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver loacaleResolver=new SessionLocaleResolver(); //세션을 통해 지정
        loacaleResolver.setDefaultLocale(Locale.KOREA); //한국으로기본값 처리
        return loacaleResolver;
    }

}
