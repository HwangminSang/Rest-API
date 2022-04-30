package com.it.restfulwebservice.user.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.it.restfulwebservice.user.Dto.User;
import com.it.restfulwebservice.user.Service.UserDaoService;
import com.it.restfulwebservice.user.execption.UserNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    //스프링에서 관리는하는 인스턴스를 BEAN 이라고 한다.
    //생성자 의존성주입 DI
      private final UserDaoService userDaoService;


    /**
     *
     *  ServletUriComponentsBuilder 이용 상태코드 번호 및 url 설정하여 반환
     *  fromCurrentRequest() 현재 요청해준 url을 사용
     *  ResponseEntity<User> return
     *  http://localhost:8088/users/4  4번추가해서 보내주고 201 created 응답해준다.
     *  생성된 사용자는 id를 모르기때문에 서버에서 만들어서 보내주는게 가장 좋다!.
     *  모든 요청을 post로 200 사용하는것은 정말로 좋지 않다.! 구체적이지 않기때문에.
     *  Headers의 location에서 확인가능
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user){  //Obejct 타입을 받기위해 해당 어노테이션 사용

        User savedUser=userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()) //저장된 유저의 id를 넣어준다
                .toUri(); //uri로 변환

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/users")
    public MappingJacksonValue  retrieveAllUsers(){
                List<EntityModel<User>> models= new ArrayList<>();
                List<User> userList = userDaoService.findAll();
               //HATEOAS
                for(User user : userList){
                   EntityModel<User> entityModel=EntityModel.of(user);
                   entityModel.add(linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel());
                   models.add(entityModel);

               }
        MappingJacksonValue mapping=new MappingJacksonValue(models);
        SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate"); // 화면에 보여주고싶은 프로펄티 설정
        FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo",filter); // @JsonFilter("UserInfo") 이름 ,필터
        mapping.setFilters(filters); // 등록

        return mapping;
    }

    /**
     *
     *  GET   /users/1 or /users/10  ---> String  => int 자동으로 컨벌팅 된다.
     *   HATEOAS 적용 및 filter적용  하나의 자원에서 다른 api 정보도 줄수있음.
     *   @ApiResponses를 통해 문서에서 200ok시 어떤값을 볼수있는지 안내. filter 사용해서 특별이 이 어노테이션사용
     *   Swagger 가 인식하는 것은 Endpoint 의 `반환타입`입니다.
     */
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) })
    })
    @GetMapping("/users/{id}") //가변변수에 포함됨
       public MappingJacksonValue retrieveUser(@Valid @PathVariable int id){

        User user = userDaoService.findOne(id);
          if(user==null){
              throw new UserNotFoundException(String.format("ID[%s] not found",id));
          }

          //HATEOAS  -> href를 보냄
        // 전제 회원보기 링크를 보냄  < 메서드(retrieveAllUsers())로 연결> http://localhost:8088/users"
        //user객체
        EntityModel entityModel=EntityModel.of(user);

          //전체회원보기
        WebMvcLinkBuilder linkto = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkto.withRel("all-users"));

        //업데이트유저
        linkto =linkTo(methodOn(this.getClass()).updateUser(id,user));
        entityModel.add(linkto.withRel("update-user"));


        //필터적용  entityModel 넣어준다.
        MappingJacksonValue mapping=new MappingJacksonValue(entityModel);
        SimpleBeanPropertyFilter filter=SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate"); // 화면에 보여주고싶은 프로펄티 설정
        FilterProvider filters=new SimpleFilterProvider().addFilter("UserInfo",filter); // @JsonFilter("UserInfo") 이름 ,필터
        mapping.setFilters(filters); // 등록

        return mapping;

       }
       //삭제
       @DeleteMapping("/users/{id}")
       public  void deleteUser(@PathVariable int id){
            User user = userDaoService.deleteById(id);

        if(user==null){
            throw  new UserNotFoundException(String.format("ID[%s] not found",id));
        }

       }
       //수정
    @PutMapping("/users/{id}")
    public  User updateUser(@PathVariable int id,@RequestBody User user)
    {
        userDaoService.updateUser(id,user);
        return user;
    }


}
