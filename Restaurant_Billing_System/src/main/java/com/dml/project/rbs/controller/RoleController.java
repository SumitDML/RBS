package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.service.RoleService;
import com.dml.project.rbs.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody Role role){
        return roleService.createNewRole(role);
    }
}
