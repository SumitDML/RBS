package com.dml.project.rbs.controller;

import com.dml.project.rbs.model.response.ResponseModel;
import com.dml.project.rbs.model.request.LoginRequest;
import com.dml.project.rbs.service.Impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/rbs")
@RestController
public class LoginController {
    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @PostMapping({"/login"})
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest){
        try{
            return loginServiceImpl.createJwtToken(loginRequest);
        }
        catch (Exception e) {
            ResponseModel responseModel = new ResponseModel(HttpStatus.BAD_REQUEST,e.getMessage(),null,null);
            return new ResponseEntity(responseModel,HttpStatus.BAD_REQUEST);
        }
    }
}
