package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.Role;
import com.dml.project.rbs.entity.User;

import java.util.HashSet;
import java.util.Set;

public interface UserService {
    public User registerNewUser(User user);
    public String getEncryptedPassword(String password);
    public void initRolesAndUser();
}
