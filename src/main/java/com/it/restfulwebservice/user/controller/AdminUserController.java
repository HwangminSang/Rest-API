package com.it.restfulwebservice.user.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.it.restfulwebservice.user.Dto.User;
import com.it.restfulwebservice.user.Dto.UserV2;
import com.it.restfulwebservice.user.Service.UserDaoService;
import com.it.restfulwebservice.user.execption.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminUserController { //관리자 컨트롤러

    //스프링에서 관리는하는 인스턴스를 BEAN 이라고 한다.
    //생성자 의존성주입 DI
      private final UserDaoService userDaoService;


    /**
     *   SimpleBeanPropertyFilter 사용
     *   FilterProvider 필터 적용시키위해 사용
     *  @JsonFilter("UserInfo") 도메인
     *  MappingJacksonValue 반환
     *
     */

    @GetMapping("/users")
      public MappingJacksonValue retrieveAllUsers(){

          List<User> users = userDaoService.findAll();
         MappingJacksonValue mapping=new MappingJacksonValue(users); //해당 도메인을 json형태로
          SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","password"); // 보고싶은 내용
          FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo",filter); // 어떤 도메인에 사용할것이   @JsonFilter("UserInfo")
          mapping.setFilters(filters); // 필터등록


           return mapping;
      }

    /**
     *     admin/users/1   ---> admin/v1/users/1  -->버전추가
     *         @GetMapping("v1/users/{id}") //가변변수에 포함됨
     *         @GetMapping(value = "/users/{id}/",params = "version=1")  //파라미터로 버전 명시
     *         @GetMapping(value = "/users/{id}",headers = "X-API-VERSION=1")
     */

    @GetMapping(value = "/users/{id}" , produces = "application/vnd.company.appv1+json")  //produces 이용 mine-type이용
    public MappingJacksonValue retrieveUserV1(@Valid @PathVariable int id){

        User user = userDaoService.findOne(id);

          if(user==null){
              throw new UserNotFoundException(String.format("ID[%s] not found",id));
          }
        MappingJacksonValue mapping=new MappingJacksonValue(user); //해당 도메인
        SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept("id","name","password","ssn");
        FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo",filter);
        mapping.setFilters(filters); // 등록

        return mapping; //반환

       }

    /**
     *   버전 관리~! (uri , 파라미터 , 헤더 ,  mine-type)
     *
     *   Caching을 주의하자!!  캐쉬삭제후 재실행    ( url , 파라미터 (일반 브라우저 가능) )  /  (해드, mine-tyoe) 일반브라우저 x
     *   @GetMapping("v2/users/{id}") //버전 2 user2 도메인으로 변경   uri로 버전관리(/admin/v2/users/1)
     *   @GetMapping(value = "/users/{id}/",params = "version=2")  /admin/users/1/?version=2    두개는 params = {"version=2","api=v2"})
     *   @GetMapping(value = "/users/{id}",headers = "X-API-VERSION=2")  //Headers 버전관리 (/admin/v2/users/1) Headers key=X-API-VERSION ,value=2 설정필요
     *
     *    인스턴스 카피
     *   -->BeanUtils.copyProperties(user,user2);
     */
  //Headers key=Accept , value=application/vnd.company.appv1+json
   @GetMapping(value = "/users/{id}" , produces = "application/vnd.company.appv2+json") //mine-type으로 버전관리 , 전달 json형태
    public MappingJacksonValue retrieveUserV2(@Valid @PathVariable int id){

        User user = userDaoService.findOne(id);

        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        //User --> User2
        UserV2 user2=new UserV2("VIP");
        // user의 내용을 user2로 카피해주는 기능 제공
        BeanUtils.copyProperties(user,user2);
       MappingJacksonValue mapping=new MappingJacksonValue(user2); //해당 도메인을 json형태로 converting

        SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","grade"); // 화면에 보여주고싶은 프로펄티 설정
        FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfoV2",filter); // @JsonFilter("UserInfo") 이름 ,필터
        mapping.setFilters(filters); // 등록

        return mapping; //반환

    }


}
