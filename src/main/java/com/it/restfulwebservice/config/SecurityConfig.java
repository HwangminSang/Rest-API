package com.it.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity //Spring Security를 활성화 시킵니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    // 로그인 설정
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() //메모리방식
                .withUser("hds8162") //로그인시 사용 aml의 설정 무시하고 이거 사용
                .password("{noop}test1234") //noop 인코딩없이 바로 사용 < 현업에서는 noop 없애고 인코딩함
                .roles("USER"); //USER 권한

    }


    //jdbc접근시
    // 특정 요청에 대해 설정
    // 인가(요청에 대한 권한 설정)
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                 .mvcMatchers("/h2-console/**").permitAll() // 인증없이 접근 가능
                 .mvcMatchers("/admin").hasRole("ADMIN") // role 에 따라 구분
                 .anyRequest().authenticated(); // 기타 요청은 인증을 하기만 하면 됨
        // 인증(login 방식 설정)
         http.formLogin();  // form login 을 사용
         http.csrf().disable(); //cs 사용 안함?
         http.headers().frameOptions().disable(); // 헤더에 프레임속성 사용 x
         http.httpBasic(); // http basic authentication 사용

    }
}
