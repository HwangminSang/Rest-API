package com.it.restfulwebservice.user.Service;

import com.it.restfulwebservice.user.Dto.User;
import com.it.restfulwebservice.user.execption.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service  //빈으로 등록
public class UserDaoService { //통합된 이름 사용 . 데이터 접근을 따로 안하기떄문에.

    private  static List<User> users =new ArrayList<User>();  //데이터베이스 역할
    private  static  int userCount =3;
      static  {
          users.add(new User(1,"HMS1",new Date(),"pass1","810208-1234567"));
          users.add(new User(2,"HMS2",new Date(),"pass2","910208-1234567"));
          users.add(new User(3,"HMS3",new Date(),"pass3","710208-1234567"));
      }
//전체찾기
       public List<User> findAll(){
          return users;
       }
    //한명찾기
       public  User findOne(int id){
            for(User u  :users){
                if(u.getId()==id){
                    return  u;
                }
            }
            return null;
       }
       //저장
       public  User save(User user){
            if(user.getId()==null){
                user.setId(++userCount);
            }
            users.add(user);
            return user;

       }

       //삭제
     public User deleteById(int id){
         Iterator<User> iterator = users.iterator();
         while(iterator.hasNext()){
             User user=iterator.next();
          if(user.getId()== id){
              iterator.remove();
              return user;
           }
         }
        return null;
     }
     //수정

    public User updateUser(int id,User user){

        User user2 = users.stream()
                .filter(user1 -> user1.getId()==id)
                .findFirst()
                .orElseThrow(() ->  new UserNotFoundException(String.format("ID[%S] NOT FOUND", user.getId())));
        user2.setName(user.getName());

        return user2;
      }

}
