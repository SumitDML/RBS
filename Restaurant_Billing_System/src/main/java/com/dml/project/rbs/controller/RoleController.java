package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.model.request.AddRoles;
import com.dml.project.rbs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RBS")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    @PreAuthorize("hasRole('Admin')")
    public Role createNewRole(@RequestBody Role role){
        return roleService.createNewRole(role);
    }

    @GetMapping({"/getAllRoles"})
    @PreAuthorize("hasRole('Admin')")
    public List<Role> getRole(){
        return roleService.getRoles();
    }
    @PostMapping({"/setRoles"})
    @PreAuthorize("hasRole('Admin')")
    public String setRole(@RequestBody AddRoles addRoles){
        return roleService.setRoles(addRoles);
    }
}
