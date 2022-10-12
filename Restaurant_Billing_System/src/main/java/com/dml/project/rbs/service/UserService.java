package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.User;

public interface UserService {
    public User registerNewUser(User user);
    public String getEncryptedPassword(String password);
    public void initRolesAndUser();

    public User userProfile(String email);

    public String clearOrderHistory(String email);


}
