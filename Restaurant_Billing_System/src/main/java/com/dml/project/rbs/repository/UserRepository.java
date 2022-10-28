package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
    UserEntity findByEmail(String email);

    @Query(value = "select password from user where email =:Email",nativeQuery = true)
    String getPasswordByEmail(@Param(value = "Email") String email);

//    @Query(value = "select * from items where (name like %:starts%)",nativeQuery = true)
//    List<Item> startsWithName(@Param(value = "starts") String starts);
}
