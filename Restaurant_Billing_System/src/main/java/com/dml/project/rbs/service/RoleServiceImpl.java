package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.request.AddRoles;
import com.dml.project.rbs.repository.RoleRepository;
import com.dml.project.rbs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Role createNewRole(Role role){
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public String setRoles(AddRoles addRoles) {
        User existingUser = userRepository.findByEmail(addRoles.getEmail());
        if(existingUser == null){
            return "Invalid Email!";
        }
        String roles = addRoles.getRole();
        Role role = roleRepository.findByRoleName(roles);
        System.out.println(role);
        if(role == null){
            return "Invalid Role Entered!";
        }
        existingUser.getRole().add(role);
        userRepository.save(existingUser);

        return "Role Added to User having email: "+ addRoles.getEmail();
    }

}
