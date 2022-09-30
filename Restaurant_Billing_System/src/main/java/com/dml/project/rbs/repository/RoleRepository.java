package com.dml.project.rbs.repository;

import com.dml.project.rbs.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,String> {
}
