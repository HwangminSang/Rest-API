package com.it.restfulwebservice.user.repository;

import com.it.restfulwebservice.user.Dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository  extends JpaRepository<Post,Integer> {

}
