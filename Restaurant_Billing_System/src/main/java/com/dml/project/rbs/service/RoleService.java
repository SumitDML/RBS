package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.RoleEntity;
import com.dml.project.rbs.model.request.AddRoles;

import java.util.List;

public interface RoleService {
    public RoleEntity createNewRole(RoleEntity roleEntity);
    public List<RoleEntity> getRoles();

    public String setRoles(AddRoles addRoles);
}
