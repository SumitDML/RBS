package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.model.request.AddRoles;

import java.util.List;

public interface RoleService {
    public Role createNewRole(Role role);
    public List<Role> getRoles();

    public String setRoles(AddRoles addRoles);
}
