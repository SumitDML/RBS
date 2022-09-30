package com.dml.project.rbs.controller;

import com.dml.project.rbs.model.request.LoginRequest;
import com.dml.project.rbs.model.response.LoginResponse;
import com.dml.project.rbs.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/RBS")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping({"/login"})
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) throws  Exception{
        LoginResponse returnValue =  loginService.createJwtToken(loginRequest);
        return new ResponseEntity(returnValue, HttpStatus.OK);
    }
}
