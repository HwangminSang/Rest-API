package com.it.restfulwebservice.user.Dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javafx.geometry.Pos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @AllArgsConstructor //모든 필드를 파라미터로 가지는 생성자 추가.
 * @JsonIgnoreProperties(value = {"password","ssn"})  //전달하는값 제어가능.   즉 클라이언트가 요청을 하여도 보내지않음.  //상속받을시 어노테이션이 자식클래스에 적용된다.
 * @NoArgsConstructor //디폴트 생성자
 *
 *  특정 필드값 제어
 * @JsonIgnore 이용 해당 프로펄티를 변환해서 사용자에게 보내지 않는다 !   맴버변수 위에
 * @JsonIgnoreProperties(value = {"password","ssn"}) 클래스 단위에서 사용
 *
 * (서비스단 컨트롤단에서 개별적으로 처리 ! )
 * @JsonFilter("UserInfo") //필터이름 부여
 *
 *  @ApiModel //api문서의 해당 클래스 설명
 *  @ApiModelProperty(notes = "") 해당 필드의 설명
 *  @ApiResponses  -> document문서에서도 해당 도메인이 200 일때 어떤값이 나오는지 볼수있게 설정해준다
 */
@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"password","ssn"})
@NoArgsConstructor
//@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Entity
public class User { //도메인 클래스

//    @NotNull(message = "Id는 필수값 입니다")
//    @Positive(message = "숫자를 입력하셔야 합니다")
    @Id
    @GeneratedValue
    private Integer id;

    //유효성 검사 , 2글자 이상만
    // message를 통해 error메세지 설정
    @Size(min=2 , message = "Name 2글자 이상 입력해주세요")
    @ApiModelProperty(notes = "사용자 이름을 입력해주세요")
    private String name;


    @Past  // 과거데이터만 올수있다.
    @ApiModelProperty(notes = "사용자 등록일을 입력해주세요")
    private Date joinDate;


   // @JsonIgnore
   @ApiModelProperty(notes = "사용자 패스워드를 입력해주세요")
    private String password;
   
   // @JsonIgnore
   @ApiModelProperty(notes = "사용자 주민번호를 입력해주세요")
    private String ssn;



    @OneToMany(mappedBy = "user")
    private List<Post> posts=new ArrayList<>();


    public User(Integer id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
