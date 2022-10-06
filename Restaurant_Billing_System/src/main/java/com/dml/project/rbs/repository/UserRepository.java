package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    User findByEmail(String email);

    @Query(value = "select password from user where email =:Email",nativeQuery = true)
    String getPasswordByEmail(@Param(value = "Email") String email);

//    @Query(value = "select * from items where (name like %:starts%)",nativeQuery = true)
//    List<Item> startsWithName(@Param(value = "starts") String starts);
}
