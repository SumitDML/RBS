package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.RoleEntity;
import com.dml.project.rbs.model.request.AddRoles;
import com.dml.project.rbs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rbs")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    @PreAuthorize("hasRole('Admin')")
    public RoleEntity createNewRole(@RequestBody RoleEntity roleEntity){
        return roleService.createNewRole(roleEntity);
    }

    @GetMapping({"/getAllRoles"})
    @PreAuthorize("hasRole('Admin')")
    public List<RoleEntity> getRole(){
        return roleService.getRoles();
    }
    @PostMapping({"/setRoles"})
    @PreAuthorize("hasRole('Admin')")
    public String setRole(@RequestBody AddRoles addRoles){
        return roleService.setRoles(addRoles);
    }
}
