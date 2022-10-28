package com.dml.project.rbs.service.Impl;

import com.dml.project.rbs.entity.RoleEntity;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.request.AddRoles;
import com.dml.project.rbs.repository.RoleRepository;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public RoleEntity createNewRole(RoleEntity roleEntity){
        return roleRepository.save(roleEntity);
    }

    @Override
    public List<RoleEntity> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public String setRoles(AddRoles addRoles) {
        UserEntity existingUserEntity = userRepository.findByEmail(addRoles.getEmail());
        if(existingUserEntity == null){
            return "Invalid Email!";
        }
        String roles = addRoles.getRole();
        RoleEntity roleEntity = roleRepository.findByRoleName(roles);
        System.out.println(roleEntity);
        if(roleEntity == null){
            return "Invalid Role Entered!";
        }
        existingUserEntity.getRoleEntity().add(roleEntity);
        userRepository.save(existingUserEntity);

        return "Role Added to User having email: "+ addRoles.getEmail();
    }

}
