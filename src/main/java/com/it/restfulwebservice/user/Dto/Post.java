package com.it.restfulwebservice.user.Dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @JsonIgnore이 있으면 해당 값은 json 형태로 보내지 않는다.
 * fetch = FetchType.LAZY 지연로딩
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
      @Id
      @GeneratedValue
       private  Integer id;

       private  String description;

     // User : Post -> 1 : (0~N) Main : Sub -> Parent : Child
    //** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message transform method call failed at JPLISAgent.c line: 844
    // JsonIgnore로 무한참조 해결
     @JsonIgnore
     @ManyToOne(fetch = FetchType.LAZY)

     private  User user;





}
