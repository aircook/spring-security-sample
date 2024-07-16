package com.tistory.aircook.security.repository;

import com.tistory.aircook.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUserid(String userid);

    //userid을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    UserEntity findByUserid(String userid);

}
