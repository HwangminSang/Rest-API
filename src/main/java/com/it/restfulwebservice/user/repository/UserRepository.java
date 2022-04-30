package com.it.restfulwebservice.user.repository;

import com.it.restfulwebservice.user.Dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Repository 데이터베이스와 관련된 bean이라는뜻
 *  JpaRepository<앤티티객체,ID자료형>를 상속
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


}
