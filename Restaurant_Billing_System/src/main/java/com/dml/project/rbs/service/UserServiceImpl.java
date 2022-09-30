package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.repository.RoleRepository;
import com.dml.project.rbs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerNewUser(User user){
        Role role  = roleRepository.findById("User").get();
        User user1 =userRepository.findByEmail(user.getEmail());
        if(user1==null){
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRole(roles);
            user.setPassword(getEncryptedPassword(user.getPassword()));
            return userRepository.save(user);
        }else{
            return null;
        }
    }

    public String getEncryptedPassword(String password){
        return passwordEncoder.encode(password);
    }

    public void initRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin Role For CRUD");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("User Role For List and Find");
        roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin@123");
        adminUser.setPassword(getEncryptedPassword("admin@pass"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);

    }
}
