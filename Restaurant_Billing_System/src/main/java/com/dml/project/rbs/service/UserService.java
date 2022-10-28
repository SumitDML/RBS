package com.dml.project.rbs.service;

import com.dml.project.rbs.dto.UserProfileDto;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.request.SignUpRequest;
import com.dml.project.rbs.model.response.MessageResponse;

public interface UserService {
    public MessageResponse registerNewUser(SignUpRequest signUpRequest);
    public String getEncryptedPassword(String password);
    public void initRolesAndUser();

    public UserProfileDto userProfile(String tokenHeader);

    public MessageResponse clearOrderHistory(String email);


}
