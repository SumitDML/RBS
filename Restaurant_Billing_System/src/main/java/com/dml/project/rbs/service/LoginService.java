package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface LoginService {
    Object createJwtToken(LoginRequest loginRequest) throws Exception;
    UserDetails loadUserByUsername(String username);
}
