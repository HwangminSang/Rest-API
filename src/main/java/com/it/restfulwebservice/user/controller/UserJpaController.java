package com.it.restfulwebservice.user.controller;

import com.it.restfulwebservice.user.Dto.Post;
import com.it.restfulwebservice.user.Dto.User;
import com.it.restfulwebservice.user.execption.UserNotFoundException;
import com.it.restfulwebservice.user.repository.PostRepository;
import com.it.restfulwebservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import java.util.List;
import java.util.Optional;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@RequestMapping("/jpa")
@RestController
public class UserJpaController {

      private  final UserRepository userRepository;
     private  final PostRepository postRepository;

      @GetMapping("/users")
      public List<User> retrieveAllUsers(){

          return userRepository.findAll();

      }

    @GetMapping("/users/{id}")
    public EntityModel retrieveUser(@PathVariable int id){

        Optional<User> result = userRepository.findById(id);
        //있을경우 true 없을경우 false
        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        //HATEOAS
        EntityModel entityModel=EntityModel.of(result.get());
        WebMvcLinkBuilder linkto= linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkto.withRel("all-user"));

        return entityModel;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){

          userRepository.deleteById(id);

    }

    /**
     * ResponseEntity 응답
     * @Valid 유효성 검사
     * @RequestBody 값을 바로 컨벌팅
     * ServletUriComponentsBuilder 이용하여 Headers의 Location에서 생성된 user를 검색할수 있드록 아이디를 담은 uri을 보내준다.
     * ex) http://localhost:8088/jpa/users/3
     * path()에는 buildAndExpand를 통해 얻은 값이 들어온다.
     * toUri() uri 생성
     * 201 created
     */

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user){

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     *  게시물
     */
    // jpa/users/900001/posts
    @GetMapping("/users/{id}/posts")
    public  List<Post> retrieveAllPostsByUser(@PathVariable int id){

        //사용자 여부확인
        Optional<User> result = userRepository.findById(id);

        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        return result.get().getPosts();

    }
    //상세 게시물
    @GetMapping("/users/{id}/posts/{postid}")
    public  Post retrievePostsByUser(@PathVariable int id,@PathVariable int postid){

        //사용자 여부확인
        Optional<User> result = userRepository.findById(id);

        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        List<Post> posts = result.get().getPosts();
        //사용자의 해당 포스트번호 확인
        boolean result1 = posts.stream().anyMatch(post -> post.getId() == postid);
   if(!result1){
       throw new UserNotFoundException(String.format("POSTID[%s]존재하지않음",postid));
   }
        return postRepository.findById(postid).get();



    }


    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id ,  @RequestBody Post post){

        //사용자 여부확인
        Optional<User> result = userRepository.findById(id);

        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        post.setUser(result.get());
        Post savedPost = postRepository.save(post);

//http://localhost:8088/jpa/users/900001/posts/1 반환
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * delete , update 하기
     */

    @DeleteMapping("/users/{id}/posts/{postId}")
    public  void deletePosts(@PathVariable int id,@PathVariable int postId){

        //사용자 여부확인
        Optional<User> result = userRepository.findById(id);

        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        List<Post> posts = result.get().getPosts();
        for (Post post : posts) {
            postRepository.deleteById(postId);
        }

    }


    @PutMapping("/users/{id}/posts/{postId}")
    public  void updatePosts(@PathVariable int id,@PathVariable int postId,@RequestBody Post post){

        //사용자 여부확인
        Optional<User> result = userRepository.findById(id);

        if(!result.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        //해당 사용자의 포스트를 가져와서 파라미터로 받아온 포스트 번호와 일치할경우 업데이트
        List<Post> posts = result.get().getPosts();

        //해당 포스트가 사용자가 쓴 포스트에 있는지 여부확인
        boolean anyMatch = posts.stream().anyMatch( matchPost -> matchPost.getId() == postId);
        if(!anyMatch){
            throw new UserNotFoundException(String.format("POSTID[%s] is not found in this User",postId));
        }
        for (Post post1 : posts) {
            if(post1.getId()==postId){
                post1.setDescription(post.getDescription());
                postRepository.save(post1);

            }
        }


    }

        
    }


