package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    @Query(value = "select * from role where role_name =:roleName",nativeQuery = true)
    Role findByRoleName(@Param(value = "roleName") String roleName);

}
