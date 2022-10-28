package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,String> {
    @Query(value = "select * from role where role_name =:roleName",nativeQuery = true)
    RoleEntity findByRoleName(@Param(value = "roleName") String roleName);

}
